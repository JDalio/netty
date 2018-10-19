package com.mda.learn_netty.nio;

import java.util.Scanner;

public class InputThread implements Runnable
{
    private String order;
    private Object lock;

    public InputThread(String order)
    {
        this.order = order;
    }

    @Override
    public void run()
    {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (scanner.hasNext())
        {
            synchronized (lock)
            {
                order = scanner.next();
            }
        }
    }
}
