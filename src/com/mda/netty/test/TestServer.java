package com.mda.netty.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer
{
    private ServerSocket server=null;
    private int serverPort=9999;
    private int backlog=3;

    public TestServer()
    {
        try
        {
            server=new ServerSocket(serverPort,backlog);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void service()
    {
        int count=1;
        while(true)
        {
            Socket socket=null;

            try
            {
                socket=server.accept();
                System.out.println("Connected: "+count++);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        TestServer server=new TestServer();
        server.service();
    }
}
