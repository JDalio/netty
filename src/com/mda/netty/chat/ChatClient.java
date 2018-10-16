package com.mda.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient
{
    private final String host;
    private final int port;

    public ChatClient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();

        try
        {
            Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class).handler(new ChatClientInitizlizer());

            Channel channel = bootstrap.connect(host, port).sync().channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (true)
            {
                channel.writeAndFlush(in.readLine() + "\r\n");
            }

        }
        finally
        {
            group.shutdownGracefully();
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
            new ChatClient("39.107.65.148", port).run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
