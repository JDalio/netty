package com.mda.learn_netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer
{
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>
    {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception
        {
            //inbound event is processed by the ChildChannelHandler at last
            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new StringDecoder());
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }

    public void bind(int port) throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap b = null;


        try
        {
            b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

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
            try
            {
                port = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            new TimeServer().bind(port);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
