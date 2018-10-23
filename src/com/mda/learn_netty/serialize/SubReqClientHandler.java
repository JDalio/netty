package com.mda.learn_netty.serialize;

import com.mda.learn_netty.protobuf.SubscribeReqProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SubReqClientHandler extends SimpleChannelInboundHandler
{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        for (int i = 0; i < 2; i++)
        {
            ctx.write(subReq(i));
            System.out.println("Write " + subReq(i));
        }
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        System.out.println("Receive server response : [" + msg + "]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }

    private SubscribeReqProto.SubscribeReq subReq(int i)
    {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setAddress("北京邮电大学");
        builder.setProductName("公共服务");
        builder.setSubReqID(i);
        builder.setUserName("Dalio");
        return builder.build();
    }
}
