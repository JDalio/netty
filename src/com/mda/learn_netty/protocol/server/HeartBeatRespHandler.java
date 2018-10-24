package com.mda.learn_netty.protocol.server;

import com.mda.learn_netty.protocol.struct.Header;
import com.mda.learn_netty.protocol.struct.Message;
import com.mda.learn_netty.protocol.struct.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HeartBeatRespHandler extends SimpleChannelInboundHandler
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Message message = (Message) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value())
        {
            Message heartBeat = buildHeartBeat();
            ctx.writeAndFlush(heartBeat);
        } else
        {
            ctx.close();
        }
    }


    private Message buildHeartBeat()
    {
        Message message = new Message();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
