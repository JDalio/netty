package com.mda.learn_netty.protocol.codec;

import com.mda.learn_netty.protocol.struct.Header;
import com.mda.learn_netty.protocol.struct.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

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
        if (msg == null || msg.getHeader() == null)
        {
            throw new Exception("The encode message is null");
        }

        ByteBuf sendBuf = Unpooled.buffer();
        Header header = msg.getHeader();
        sendBuf.writeInt(header.getCrcCode());
        sendBuf.writeInt(header.getLength());
        sendBuf.writeLong(header.getSessionID());
        sendBuf.writeByte(header.getType());
        sendBuf.writeByte(header.getPriority());
        sendBuf.writeInt(header.getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;

        for (Map.Entry<String, Object> param : header.getAttachment().entrySet())
        {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(value, sendBuf);
        }

        key = null;
        keyArray = null;
        if (msg.getBody() != null)
        {
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        } else
            sendBuf.writeInt(0);

        sendBuf.setInt(4, sendBuf.readableBytes() - 8);
    }
}