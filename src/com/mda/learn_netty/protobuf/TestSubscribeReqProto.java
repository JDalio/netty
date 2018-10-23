package com.mda.learn_netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

public class TestSubscribeReqProto
{
    private static byte[] encode(SubscribeReqProto.SubscribeReq req)
    {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException
    {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq()
    {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("Dalio");
        builder.setProductName("Netty Book");
        builder.setAddress("Bupt");
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException
    {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : " + req.toString());

        SubscribeReqProto.SubscribeReq req1 = decode(encode(req));
        System.out.println("Assert equal : -->" + req1.equals(req));
    }
}
