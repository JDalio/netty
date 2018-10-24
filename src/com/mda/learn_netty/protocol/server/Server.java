package com.mda.learn_netty.protocol.server;

import com.mda.learn_netty.protocol.codec.MessageDecoder;
import com.mda.learn_netty.protocol.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server
{
    public void bind(int port) throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<ServerSocketChannel>()
                    {
                        @Override
                        protected void initChannel(ServerSocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new MessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast(new MessageEncoder());
                            ch.pipeline().addLast(new LoginAuthRespHandler());
                            ch.pipeline().addLast(new HeartBeatRespHandler());

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
        new Server().bind(8000);
    }
}
