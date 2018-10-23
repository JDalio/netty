package com.mda.learn_netty.test;

public class ShitTask implements Runnable
{

    private Washroom washroom;
    private String name;

    public ShitTask(Washroom washroom, String name)
    {
        this.washroom = washroom;
        this.name = name;
    }

    @Override
    public void run()
    {
        synchronized (washroom.getLock())
        {
            System.out.println("Shit Get Lock "+name);

            while(!washroom.isAvailable())
            {
                try
                {
                    washroom.getLock().wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            System.out.println("Shit Finish "+name);
        }
    }
}
