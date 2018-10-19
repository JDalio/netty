package com.mda.netty.test;

public class RepairTask implements Runnable
{
    private Washroom washroom;

    public RepairTask(Washroom washroom)
    {
        this.washroom = washroom;
    }

    @Override
    public void run()
    {
        synchronized (washroom.getLock())
        {
            System.out.println("Repairor get lock and working...");

            try
            {
                Thread.sleep(5000L);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            washroom.setAvailable(true);
            washroom.getLock().notifyAll();
            System.out.println("Finish repairing, release lock");
        }
    }
}
