package com.mda.learn_netty.protocol.server;

import com.mda.learn_netty.protocol.struct.Header;
import com.mda.learn_netty.protocol.struct.Message;
import com.mda.learn_netty.protocol.struct.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends SimpleChannelInboundHandler
{
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String[] whiteList = {"127.0.0.1", "39.107.65.148"};


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Message message = (Message) msg;

        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value())
        {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            Message loginResp = null;

            if (nodeCheck.containsKey(nodeIndex))
            {
                loginResp = buildResponse((byte) -1);
            } else
            {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whiteList)
                {
                    if (WIP.equals(ip))
                    {
                        isOK = true;
                        break;
                    }
                }

                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);

                if (isOK)
                {
                    nodeCheck.put(nodeIndex, true);

                }
            }

            ctx.writeAndFlush(loginResp);
        } else
        {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
        cause.printStackTrace();
    }

    private Message buildResponse(byte result)
    {
        Message message = new Message();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
