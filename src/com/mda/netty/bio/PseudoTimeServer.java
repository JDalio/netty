package com.mda.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PseudoTimeServer
{
    public static void main(String[] args)
    {
        int port = 8080;
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

        //创建监听
        ServerSocket server = null;

        try
        {
            server = new ServerSocket(port);
            System.out.println("Time Server is running in port: " + port);

            //创建socket
            Socket socket = null;
            //创建执行线程池
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);

            while (true)
            {
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                server.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
