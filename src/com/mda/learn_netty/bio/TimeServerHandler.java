package com.mda.learn_netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

// server's task, read order and return results
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

            //PrintWriter contains an 8k cache default
            out = new PrintWriter(this.socket.getOutputStream(), true);

            String currentTime = null;
            String body = null;

            while (true)
            {
                //阻塞操作
                body = in.readLine();
                if (body == null)
                    break;
                System.out.println("Time server receive order: " + body);
                currentTime = "CURRENT TIME".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                out.println(currentTime);
            }
        }
        catch (IOException e)
        {
            try
            {
                in.close();
                out.close();
                this.socket.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
