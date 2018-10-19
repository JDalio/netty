package com.mda.learn_netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private final String ROOT_PATH;

    public HttpFileServerHandler(String path)
    {
        this.ROOT_PATH = path;
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString(), CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        //writeAndFlush returns a ChannelFuture, when client receive the msg successfully, ChannelFuture will close the channel.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private String sanitizeUri(String uri)
    {
        try
        {
            uri = URLDecoder.decode(uri, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            try
            {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            }
            catch (UnsupportedEncodingException e1)
            {
                e1.printStackTrace();
            }
        }

        if (!uri.startsWith("/"))
        {
            return null;
        }

        uri = uri.replace('/', File.separatorChar);

        return uri;
    }

    private static void setContentTypeHeader(HttpResponse response, File file)
    {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE,
                mimeTypesMap.getContentType(file.getPath()));
    }

    private static void sendListing(ChannelHandlerContext ctx, File dir)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(CONTENT_TYPE, "text/html;charset=UTF-8");

        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();

        buf.append("<!DOCTYPE html>\r\n");

        buf.append("<html><head><title>");
        buf.append("目录");
        buf.append("</title></head><body>\r\n");

        buf.append("<h2>").append(dirPath).append("</h2>\r\n");

        buf.append("<ul>");
        buf.append("<li><a href=\"../\">..</a></li>\r\n");
        for (File f : dir.listFiles())
        {
            if (f.isHidden() || !f.canRead())
            {
                continue;
            }

            String name = f.getName();
            buf.append("<li><a href=\"").append(name).append("\">").append(name).append("</a></li>\r\n");
        }

        buf.append("</ul></body></html>");

        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception
    {
        final String url = request.uri();
        final String path = ROOT_PATH + sanitizeUri(url);
        final String body = request.content().toString(CharsetUtil.UTF_8);
        final HttpMethod method = request.method();

        System.out.println("Url: " + url + "\nPath: " + path + "\nBody: " + body + "\nMethod: " + method);

        if (!HttpMethod.GET.equals(method))
        {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }


        File file = new File(path);

        if (file.isHidden() || !file.exists())
        {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        if (file.isDirectory())
        {
            sendListing(ctx, file);
        }


        RandomAccessFile randomAccessFile = null;
        try
        {
            randomAccessFile = new RandomAccessFile(file, "r");
        }
        catch (FileNotFoundException e)
        {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        response.headers().set(CONTENT_LENGTH, fileLength);
        setContentTypeHeader(response, file);

        ctx.write(response);

        ChannelFuture sendFileFuture;
        sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener()
        {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception
            {
                if (total < 0)
                {
                    System.err.println("Transfer progress: " + progress);
                } else
                {
                    System.err.println("Transfer progress: " + progress + " / " + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception
            {
                System.out.println("Transfer complete");
            }
        });

        ChannelFuture lastContentFuture = ctx
                .writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!isKeepAlive(request))
        {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
