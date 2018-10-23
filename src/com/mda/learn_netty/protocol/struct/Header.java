package com.mda.learn_netty.protocol.struct;

import java.util.HashMap;
import java.util.Map;

public class Header
{
    //校验码
    // (1)0xabef 固定值，表示该消息为netty消息
    // (2)主版本号 (3)此版本号
    private int crcCode = 0xabef0101;
    //header + body
    private int length;
    //session id
    private long sessionID;
    //message type
    private byte type;
    //message priority
    private byte priority;
    //attachment
    private Map<String, Object> attachment = new HashMap<>();

    public final int getCrcCode()
    {
        return crcCode;
    }

    public final void setCrcCode(int crcCode)
    {
        this.crcCode = crcCode;
    }

    public final int getLength()
    {
        return length;
    }

    public final void setLength(int length)
    {
        this.length = length;
    }

    public final long getSessionID()
    {
        return sessionID;
    }

    public final void setSessionID(long sessionID)
    {
        this.sessionID = sessionID;
    }

    public final byte getType()
    {
        return type;
    }

    public final void setType(byte type)
    {
        this.type = type;
    }

    public final byte getPriority()
    {
        return priority;
    }

    public final void setPriority(byte priority)
    {
        this.priority = priority;
    }

    public final Map<String, Object> getAttachment()
    {
        return attachment;
    }

    public final void setAttachment(Map<String, Object> attachment)
    {
        this.attachment = attachment;
    }

    @Override
    public String toString()
    {
        return "Header [crcCode=" + crcCode + ", length=" + length + ", sessionID=" + sessionID + ", type=" + type + ", priority=" + priority + ", attachemnt=" + attachment + "]";
    }
}
