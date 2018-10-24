package com.mda.learn_netty.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

public class ChannelByteBufOutput implements ByteOutput
{
    private final ByteBuf buffer;

    public ChannelByteBufOutput(ByteBuf buffer)
    {
        this.buffer = buffer;
    }

    ByteBuf getBuffer()
    {
        return buffer;
    }

    @Override
    public void write(int i) throws IOException
    {
        buffer.writeByte(i);
    }

    @Override
    public void write(byte[] bytes) throws IOException
    {
        buffer.writeBytes(bytes);
    }

    @Override
    public void write(byte[] bytes, int srcIndex, int length) throws IOException
    {
        buffer.writeBytes(bytes, srcIndex, length);
    }

    @Override
    public void close() throws IOException
    {

    }

    @Override
    public void flush() throws IOException
    {

    }
}
