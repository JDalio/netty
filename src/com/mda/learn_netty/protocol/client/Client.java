package com.mda.learn_netty.protocol.client;

import com.mda.learn_netty.protocol.codec.MessageDecoder;
import com.mda.learn_netty.protocol.codec.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;

public class Client
{
//    private ScheduledExecutorService executor= Executors.newScheduledThreadPool(1);

    public void connect(String host, int port) throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();

        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast(new MessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast(new MessageEncoder());
                            ch.pipeline().addLast(new LoginAuthReqHandler());
                            ch.pipeline().addLast(new HeartBeatReqHandler());
                        }
                    });
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().closeFuture().sync();

        }
        finally
        {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception
    {
        new Client().connect("127.0.0.1", 8000);
    }
}
