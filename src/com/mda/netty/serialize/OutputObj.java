package com.mda.netty.serialize;

import java.io.*;

public class OutputObj
{
    public static void main(String[] args)
    {
        User1 user = new User1();
        user.setName("Dalio");
        user.setAge(21);
        System.out.println(user);

        ObjectOutputStream oos = null;

        try
        {
            oos = new ObjectOutputStream(new FileOutputStream("user1"));
            oos.writeObject(user);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                oos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }
}
