package com.mda.learn_netty.protocol.codec;

import com.mda.learn_netty.protocol.struct.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class MessageEncoder extends MessageToMessageEncoder<Message>
{

    private MarshallingEncoder marshallingEncoder;

    public MessageEncoder() throws Exception
    {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception
    {

    }
}
