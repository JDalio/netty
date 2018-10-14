package com.mda.netty.nio;

public class TimeClient
{
    public static void main(String[] args)
    {
        int port = 8000;

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

        for (int i = 0; i < 1000; i++)
        {
            new Thread(new TimeClientHandler("39.107.65.148", port), "TimeClient-" + i).start();
            System.out.println("Client Number: " + i);
            try
            {
                Thread.sleep(30);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
