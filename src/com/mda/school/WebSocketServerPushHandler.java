package com.mda.school;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;

import java.util.Date;

public class WebSocketServerPushHandler implements Runnable
{
    private ChannelGroup channelGroup;

    public WebSocketServerPushHandler(ChannelGroup channelGroup)
    {
        this.channelGroup = channelGroup;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                if (!channelGroup.isEmpty())
                {
                    ByteBuf buf = Unpooled.copiedBuffer(new Date().toString(), CharsetUtil.UTF_8);
                    channelGroup.writeAndFlush(new TextWebSocketFrame("欢迎使用Netty WebSocket服务，现在时刻：" + new Date().toString()));
                }

                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
