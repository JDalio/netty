package netty;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class netty
{
    public static void main(String[] args)
    {
        netty nt=new netty();
//        nt.readFileByFileInputStream("README.md");
    }

//    public void readFileByFileInputStream(String path)
//    {
//        FileInputStream fileInputStream = null;
//        try
//        {
//            fileInputStream = new FileInputStream(path);
//            int n=1024;
//            byte buf[]=new byte[n];
//            System.out.println("readFileByFileInputStream's results: ");
//            while((fileInputStream.read(buf,0,n) != -1) && n>0)
//            {
//                System.out.println(new String(buf));
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            try
//            {
//                fileInputStream.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//    public void writeFileByBufferedWriter(String path)
//    {
//        File file=new File(path);
//        if(file.isFile())
//        {
//            BufferedWriter bufWriter=null;
//            FileWriter fileWriter=null;
//            try
//            {
//                fileWriter = new FileWriter(file,true);
//                bufWriter = new BufferedWriter(fileWriter);
//                bufWriter.write(".");
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            finally
//            {
//                try
//                {
//                    bufWriter.close();
//                    fileWriter.close();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//
//    }

//    public void readFileByFileRader(String path)
//    {
//        FileReader fileReader = null;
//        try
//        {
//            fileReader=new FileReader(path);
//            char[] buf = new char[1024];
//            int temp=0;
//            System.out.println("readFileByFileReader's result:");
//            while((temp=fileReader.read(buf))!= -1)
//            {
//                System.out.print(new String(buf,0,temp));
//            }
//            System.out.println();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            if(fileReader!=null)
//            {
//                try
//                {
//                    fileReader.close();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//    public void readFileByBufferedReader(String path)
//    {
//        File file=new File(path);
//        if(file.isFile())
//        {
//            BufferedReader bufferedReader=null;
//            FileReader fileReader=null;
//            try
//            {
//                fileReader = new FileReader(file);
//                bufferedReader=new BufferedReader(fileReader);
//                String line=bufferedReader.readLine();
//                System.out.println("readFileByBufferedReader's result:");
//                while(line != null)
//                {
//                    System.out.println(line);
//                    line=bufferedReader.readLine();
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            finally
//            {
//                try
//                {
//                    fileReader.close();
//                    bufferedReader.close();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
