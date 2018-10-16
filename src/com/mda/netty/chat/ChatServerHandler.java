package com.mda.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String>
{
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "离线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "异常");
        ctx.close();
        throw new RuntimeException(cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception
    {
        Channel incoming = ctx.channel();
        System.out.println("[" + incoming.remoteAddress() + "]" + msg + "\n");
        for (Channel channel : channels)
        {
            if (channel != incoming)
            {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
            } else
            {
                channel.writeAndFlush("[You]" + msg + "\n");
            }
        }
    }
}
