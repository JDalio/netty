package client.dalio.netty.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient
{
    public static void main(String[] args)
    {
        // 设置端口
        int port = 8080;
        if (args != null && args.length > 0)
        {
            try
            {
                port = Integer.parseInt(args[0]);

            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }

        // 定义端口，输入和输出
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("QUERY TIME ORDER");
            System.out.println("Send order to server succeed.");
            String resp = in.readLine();
            System.out.println("Now is " + resp);
        }
        catch (Exception ee)
        {
        }
        finally
        {
            if (out != null)
                out.close();

            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
                in = null;
            }

            if (socket != null)
            {
                try
                {
                    socket.close();
                }
                catch (Exception ee)
                {
                    ee.printStackTrace();
                }
                socket = null;
            }
        }

    }
}


