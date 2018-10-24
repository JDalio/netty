package com.mda.learn_netty.protocol.client;

import com.mda.learn_netty.protocol.struct.Header;
import com.mda.learn_netty.protocol.struct.Message;
import com.mda.learn_netty.protocol.struct.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginAuthReqHandler extends SimpleChannelInboundHandler
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Message message = (Message) msg;

        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value())
        {
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0)
            {
                //login result == 0 代表握手成功
                ctx.close();
            } else
            {
                System.out.println("Login is ok : " + msg);
            }
        } else
        {
            ctx.close();
        }
    }

    private Message buildLoginReq()
    {
        Message message = new Message();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
        cause.printStackTrace();
    }
}
