package com.mda.netty.test;

import java.util.Scanner;

public class Test
{
    public static void main(String[] args)
    {
        Washroom washroom = new Washroom();

        new Thread(new ShitTask(washroom,"gouge"),"gouge-thread").start();
        new Thread(new ShitTask(washroom,"maoye"),"maoye-thread").start();
        new Thread(new ShitTask(washroom,"wnm"),"wnm-thread").start();

        try
        {
            Thread.sleep(1000L);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        new Thread(new RepairTask(washroom),"REPAIR-THREAD").start();


    }
}
