package com.mda.learn_netty.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;

import java.io.IOException;

public class ChannelByteBufInput implements ByteInput
{
    private final ByteBuf buffer;

    public ChannelByteBufInput(ByteBuf buffer)
    {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException
    {
        if (buffer.isReadable())
        {
            return buffer.readByte() & 0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] bytes) throws IOException
    {
        return read(bytes, 0, bytes.length);
    }

    @Override
    public int read(byte[] bytes, int dstIndex, int length) throws IOException
    {
        int available = available();
        if (available == 0)
        {
            return -1;
        }

        length = Math.min(length, available);
        buffer.readBytes(bytes, dstIndex, length);
        return length;
    }

    @Override
    public int available() throws IOException
    {
        return buffer.readableBytes();
    }

    @Override //跳过长度为bytes的内容开始读
    public long skip(long bytes) throws IOException
    {
        int readable = buffer.readableBytes();
        if (readable < bytes)
        {
            bytes = readable;
        }
        buffer.readerIndex((int) (buffer.readerIndex() + bytes));
        return bytes;
    }

    @Override
    public void close() throws IOException
    {

    }
}
