package com.mda.learn_netty.nio;

public class TimeServer
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
            }
        }

        //开启一个线程作为Server
        MultiplexTimeServer timeServer = new MultiplexTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexTimeServer-001").start();

    }
}
