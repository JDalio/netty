package com.mda.learn_netty.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

public class MarshallingEncoder
{
    private static final byte[] LENGTH_PLACEHODER = new byte[4];
    Marshaller marshaller;

    public MarshallingEncoder() throws IOException
    {
        marshaller = MarshallingCodecFactory.buildMarshalling();
    }

    //encode the message and put the length and body in ByteBuf out for the following transport
    protected void encode(Object msg, ByteBuf out) throws Exception
    {
        try
        {
            int lengthPos = out.writerIndex();
            //让出消息长度空间
            out.writeBytes(LENGTH_PLACEHODER);
            ChannelByteBufOutput output = new ChannelByteBufOutput(out);
            marshaller.start(output);
            //use marshalling 写入Body
            marshaller.writeObject(msg);
            marshaller.finish();
            //写入消息长度
            out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
        }
        finally
        {
            marshaller.close();
        }


    }
}
