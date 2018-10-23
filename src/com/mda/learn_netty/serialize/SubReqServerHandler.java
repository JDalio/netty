package com.mda.learn_netty.serialize;

import com.mda.learn_netty.protobuf.SubscribeReqProto;
import com.mda.learn_netty.protobuf.SubscribeRespProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SubReqServerHandler extends SimpleChannelInboundHandler
{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channel establish");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if ("Dalio".equalsIgnoreCase(req.getUserName()))
        {
            System.out.println("[ "
                    + req.getProductName() + " " + req.getAddress() + " ]");
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }

    private SubscribeRespProto.SubscribeResp resp(int subReqID)
    {
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode(0);
        builder.setDesc("Netty book ordered.");

        return builder.build();
    }


}
