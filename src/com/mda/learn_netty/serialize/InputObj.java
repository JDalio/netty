package com.mda.learn_netty.serialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class InputObj
{
    public static void main(String[] args)
    {
        File file = new File("User1");
        ObjectInputStream ois = null;

        try
        {
            ois = new ObjectInputStream(new FileInputStream(file));
            User1 user1 = null;
            user1 = (User1) ois.readObject();

            System.out.println(user1);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                ois.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
