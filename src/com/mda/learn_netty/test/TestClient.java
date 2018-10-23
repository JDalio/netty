package com.mda.learn_netty.test;

import java.io.IOException;
import java.net.Socket;

public class TestClient
{
    public static void main(String[] args)
    {
        for (int i = 0; i < 30; i++)
        {
            try
            {
                Socket socket = new Socket("127.0.0.1", 9999);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("Client connection:" + (i + 1));

            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {


            }
        }
    }
}
