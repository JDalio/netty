package com.dalio.netty.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeServerHandler implements Runnable
{
    private Socket socket;

    public TimeServerHandler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            // BufferedReader包装其他Reader，提供缓冲；InputStreamReader将字节流转化成字符流
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            // PrintWriter将字符打印到OutputStream
            out = new PrintWriter(this.socket.getOutputStream(), true);

            String currentTime = null;
            String body = null;

            while (true)
            {
                try
                {
                    body = in.readLine();
                    if (body == null)
                        break;

                    System.out.println("The time server receive order: " + body);

                    currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";

                    out.println(currentTime);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {

            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
                finally
                {
                    in = null;
                }

            }

            if (out != null)
                out.close();

            if (this.socket != null)
            {
                try
                {
                    this.socket.close();
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
                finally
                {
                    this.socket = null;
                }
            }
        }
    }

}