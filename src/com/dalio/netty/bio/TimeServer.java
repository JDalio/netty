package com.dalio.netty.bio;

import java.io.IOException;
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
            }
        }

    }
}
