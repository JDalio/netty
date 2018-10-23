package com.mda.learn_netty.test;

import java.util.Random;

class Initable
{
    static final int staticFinal = 47;
    static final int staticFinal2 = Test.rand.nextInt(1000);

    static
    {
        System.out.println("Initializing Initable");
    }
}

class Initable2
{
    static final int staticFinal = 47;
    static
    {
        System.out.println("Initializing Initable2");
    }
}

class Initable3
{
    static final int staticFinal = 74;
    static
    {
        System.out.println("Initializing Initable3");
    }
}
public class Test
{
    public static Random rand=new Random(47);

    public static void main(String[] args) throws Exception
    {
        Class intclass=Integer.class;
        intclass=double.class;
    }
}
