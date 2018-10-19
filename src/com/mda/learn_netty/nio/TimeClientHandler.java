package com.mda.learn_netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandler implements Runnable
{
    private String host;
    private int port;

    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop;

    public TimeClientHandler(String host, int port)
    {
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;

        try
        {
            selector = Selector.open();
            System.out.println(selector);

            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void doConnect() throws IOException
    {
        //连接成功，发送请求；连接失败，监听，等待服务器来连接
        if (socketChannel.connect(new InetSocketAddress(host, port)))
        {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doSend("QUERY TIME");
            System.out.println("Connect successfully, Send order: QUERY TIME");
        } else
        {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            System.out.println("Waiting for connect");
        }
    }

    private void doSend(String order) throws IOException
    {
        byte[] req = order.getBytes();
        ByteBuffer writeBuf = ByteBuffer.allocate(req.length);
        writeBuf.put(req);
        writeBuf.flip();
        socketChannel.write(writeBuf);

        if (!writeBuf.hasRemaining())
        {
            System.out.println("Send order successfully");
        }
    }


    public void handleInput(SelectionKey key) throws IOException
    {
        if (key.isValid())
        {
            SocketChannel sc = (SocketChannel) key.channel();

            //连接事件，之前连接失败的情况
            if (key.isConnectable())
            {
                if (sc.finishConnect())
                {
                    sc.register(selector, SelectionKey.OP_READ);
                    doSend("QUERY TIME");
                } else
                {
                    System.exit(1);
                }
            }

            //读事件，写成功返回
            if (key.isReadable())
            {
                ByteBuffer readBf = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBf);

                if (readBytes > 0)
                {
                    readBf.flip();
                    byte[] bytes = new byte[readBf.remaining()];
                    readBf.get(bytes);

                    String body = new String(bytes, "UTF-8");
                    System.out.println(Thread.currentThread().getName() + ": " + body);
                    this.stop = true;

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
        // 1、connect the server -> interest read event ->send query
        try
        {
            doConnect();
        }
        catch (IOException e)
        {
            System.out.println("Connect failed");
            e.printStackTrace();
            System.exit(1);
        }

        while (!stop)
        {
            try
            {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();

                SelectionKey key = null;
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
                        e.printStackTrace();
                    }

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.exit(1);
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
