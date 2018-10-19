package com.mda.learn_netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer
{
    private static final String DEFAULT_PATH = "C:\\Users\\22672\\Documents";

    public void run(final int port, final String url) throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            //Decodes ByteBufs into HttpRequests and HttpContents.
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            //Encodes an HttpResponse or an HttpContent into a ByteBuf.
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            //A ChannelHandler that aggregates an HttpMessage and its following HttpContents into a single FullHttpRequest or FullHttpResponse
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("string-encoder", new StringEncoder());
                            ch.pipeline().addLast("string-decoder", new StringDecoder());
                            //A ChannelHandler that adds support for writing a large data stream asynchronously neither spending a lot of memory nor getting OutOfMemoryError. Large data streaming such as file transfer requires complicated state management in a ChannelHandler implementation. ChunkedWriteHandler manages such complicated states so that you can send a large data stream without difficulties.
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception
    {
        int port = 8000;
        if (args.length > 0)
        {
            try
            {
                port = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }

        String path = DEFAULT_PATH;
        if (args.length > 1)
        {
            path = args[1];
        }

        new HttpFileServer().run(port, path);
    }
}
