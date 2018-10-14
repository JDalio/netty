package com.mda.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

//server 线程，负责轮询多路复用器，处理多个客户端的并发接入
public class MultiplexTimeServer implements Runnable
{
    private Selector selector;
    private ServerSocketChannel servChannel;

    private volatile boolean stop;

    public MultiplexTimeServer(int port)
    {
        try
        {
            servChannel = ServerSocketChannel.open();
            servChannel.configureBlocking(false);

            selector = Selector.open();
            System.out.println(selector);
            /*public InetSocketAddress( int var1)
            {
                this(InetAddress.anyLocalAddress(), var1);
            }*/
            //server在port端口上绑定所有Local Addressf, accept不过来时，有1024个缓冲
            servChannel.socket().bind(new InetSocketAddress(port), 1024);
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The server is start in port: " + port);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void stop()
    {
        this.stop = true;
    }

    private void doWrite(SocketChannel channel, String response) throws IOException
    {
        if (response != null && response.trim().length() > 0)
        {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();

            channel.write(writeBuffer);
        }
    }

    private void handleInput(SelectionKey key) throws IOException
    {
        if (key.isValid())
        {
            if (key.isAcceptable())
            {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();

                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable())
            {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0)
                {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);

                    String body = new String(bytes, "UTF-8");
                    System.out.println("Time Server Receive : " + body);

                    String currentTime = "QUERY TIME".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    doWrite(sc, currentTime);
                } else if (readBytes < 0)
                {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    @Override
    public void run()
    {
        while (!stop)
        {
            try
            {
                //1s后，开始捕获已经可以进行IO操作的channel
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();

                SelectionKey key = null;
                //处理已经连接的channel
                while (it.hasNext())
                {
                    key = it.next();
                    it.remove();

                    try
                    {
                        handleInput(key);
                    }
                    catch (IOException e)
                    {
                        if (key != null)
                        {
                            key.cancel();
                            if (key.channel() != null)
                            {
                                key.channel().close();
                            }
                        }
                    }
                }


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (selector != null)
        {
            try
            {
                selector.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}
