package com.mda.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;

public class TimeClientHandler extends SimpleChannelInboundHandler
{
    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

    private final ByteBuf firstmsg;

    public TimeClientHandler()
    {
        byte[] req = "QUERY TIME".getBytes();
        firstmsg = Unpooled.wrappedBuffer(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.writeAndFlush(firstmsg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        ByteBuf buf = (ByteBuf) msg;

        byte[] resp = new byte[buf.readableBytes()];
        buf.readBytes(resp);

        String body = new String(resp, "UTF-8");

        System.out.println(body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        logger.warning("Unexcepted exception from downstream: " + cause.getMessage());
        ctx.close();
    }
}
