package com.mda.learn_netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient
{
    public static void main(String[] args)
    {
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

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("current time");

            System.out.println("Send order succeed");
            //阻塞
            String resp = in.readLine();
            System.out.println("Now is " + resp);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                in.close();
                out.close();
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
