package com.mda.school;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocketServer
{
    private ChannelGroup channels;
    private ExecutorService channelsExecutor;

    public WebSocketServer()
    {
        this.channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.channelsExecutor = Executors.newFixedThreadPool(1);

        channelsExecutor.submit(new WebSocketServerPushHandler(channels));
    }

    public void run(int port) throws Exception
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
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("loging",new LoggingHandler(LogLevel.INFO));
                            //将请求和应答编码或解码为http消息
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            //将http消息多个部分组成http消息
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            //异步发送大文件而不占用太多资源
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());

                            pipeline.addLast("handler", new WebSocketServerHandler(channels));
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
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
        if (args != null && args.length > 0)
        {
            port = Integer.valueOf(args[0]);
        }

        new WebSocketServer().run(port);
    }
}
