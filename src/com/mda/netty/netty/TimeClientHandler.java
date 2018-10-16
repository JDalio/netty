package com.mda.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

public class TimeClientHandler extends SimpleChannelInboundHandler
{
    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

    private int counter;
    private byte[] req;

    public TimeClientHandler()
    {
        req = ("QUERY TIME" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        ByteBuf message = null;
        for (int i = 0; i < 100; i++)
        {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        String body = (String) msg;

        System.out.println(body + " The counter is: " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        logger.warning("Unexcepted exception from downstream: " + cause.getMessage());
        ctx.close();
    }
}
