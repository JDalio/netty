package com.mda.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer
{
    public static void main(String[] args)
    {
        //prepare a port
        int port = 8080;
        if (args != null && args.length > 0)
        {
            try
            {
                port = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }

        ServerSocket server = null;

        try
        {
            //server 负责监听，一有连接便创建socket交给socket处理
            server = new ServerSocket(port);
            System.out.println("Time server is listening in port: " + port);

            Socket socket = null;
            while (true)
            {
                //没有连接阻塞在此
                socket = server.accept();
                //创建新线程处理socket
                new Thread(new TimeServerHandler(socket)).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (server != null)
            {
                System.out.println("Time server close");
                try
                {
                    server.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }


    }
}
