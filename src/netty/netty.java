package netty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.DataFormatException;

public class netty
{
    public static void test(String s)
    {
        if(s.equals("hello") && Integer.parseInt(s)==0)
        {
            throw new NumberFormatException();
        }
    }

    public static void function() throws NumberFormatException
    {
        test("hello");
    }
    public static void main(String[] args) throws IOException
    {
        try
        {
            function();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        finally
        {
            System.out.println("finish.");
        }
        File file=new File("netty.java");
        FileOutputStream outputStream=new FileOutputStream(file);
        FileChannel channel = outputStream.getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        String str="java nio";
        buffer.put(str.getBytes());
        buffer.flip();
        channel.write(buffer);
        channel.close();
        outputStream.close();
    }
}
