package com.mda.learn_netty.protocol.client;

import com.mda.learn_netty.protocol.struct.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.awt.*;

public class LoginAuthReqHandler extends SimpleChannelInboundHandler
{

    //    private static final Log LOG=LogFactory.getLog(LoginAuthReqHandler.class)
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Message message = (Message) msg;

        if (message.getHeader() != null && message.getHeader().getType() == TrayIcon.MessageType.)
    }
}
