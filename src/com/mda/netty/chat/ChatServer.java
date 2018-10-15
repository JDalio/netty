package com.mda.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

public class ChatServer
{
    private int port;

    public ChatServer(int port)
    {
        this.port = port;
    }

    public void run() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChatServerInitializer());

            System.out.println("Chat Server is starting");

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args)
    {
        int port = 8000;
        if (args != null && args.length > 0)
        {
            port = Integer.valueOf(args[0]);
        }

        try
        {
            new ChatServer(port).run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
