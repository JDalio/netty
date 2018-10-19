package com.mda.school;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>
{
    private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

    //    Base class for server side web socket opening and closing handshakes
    private WebSocketServerHandshaker handshaker;

    private ChannelGroup channelGroup;

    public WebSocketServerHandler(ChannelGroup channelGroup)
    {
        this.channelGroup = channelGroup;
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception
    {
        if (!"websocket".equals(req.headers().get("Upgrade")))
        {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
//Auto-detects the version of the Web Socket protocol in use and creates a new proper WebSocketServerHandshaker.
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8000/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null)
        {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else
        {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame)
    {
        if (frame instanceof CloseWebSocketFrame)
        {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        if (frame instanceof PingWebSocketFrame)
        {
            System.out.println("Receive Client's Ping Frame");
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (!(frame instanceof TextWebSocketFrame))
        {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }

        String request = ((TextWebSocketFrame) frame).text();
        if (logger.isLoggable(Level.FINE))
        {
            logger.fine(String.format("%s received %s", ctx.channel(), request));
        }
        ctx.channel().writeAndFlush(new TextWebSocketFrame(request + ", 欢迎使用Netty WebSocket服务，现在时刻：" + new Date().toString()));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res)
    {
        if (res.status().code() != 200)
        {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            res.headers().set(CONTENT_LENGTH, res.content().readableBytes());
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200)
        {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        Channel incoming = ctx.channel();
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        Channel incoming = ctx.channel();
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " 在线\n");
        channelGroup.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        Channel incoming = ctx.channel();
        System.out.println("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        //http建立连接
        if (msg instanceof FullHttpRequest)
        {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        //WebSocket交换数据
        else if (msg instanceof WebSocketFrame)
        {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        ctx.close();
    }
}
