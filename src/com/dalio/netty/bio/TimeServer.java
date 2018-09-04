package com.dalio.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer
{
    public static void main(String[] args) throws IOException
    {
        int port = 8080;

        if (args != null && args.length > 0)
        {
            try
            {
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }

        ServerSocket server = null;
        try
        {
            server = new ServerSocket(port);
            System.out.println("The server socker is start in port: " + port);
            Socket socket = null;
            while (true)
            {
                socket = server.accept();
                // 一旦有链接，开启一新线程处理链接
                new Thread(new TimeServerHandler(socket)).start();
            }
        }
        finally
        {
            if (server != null)
            {
                System.out.println("The time server is closed.");
                server.close();
                server = null;
            }
        }

    }
}

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
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            out = new PrintWriter(this.socket.getOutputStream(), true);

            String currentTime = null;
            String body = null;

            while (true)
            {
                try
                {
                    body = in.readLine();
                    if (body == null)
                    {
                        break;
                    }

                    System.out.println("The time server receive order: " + body);

                    currentTime = "QUERY TIMER ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";

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

            if (in != null)
            {
                try
                {
                    in.close();
                    in = null;
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }

            }

            if (out != null)
            {
                try
                {
                    out.close();
                    out = null;
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
            }

            if (this.socket != null)
            {
                try
                {
                    this.socket.close();
                    this.socket = null;
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }

            }
        }
    }

}