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

    protected void encode(Object msg, ByteBuf out) throws Exception
    {

    }
}
