package log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Printer
{
    private static final PrintStream OUT_CONSOLE = System.out;
    private static PrintWriter       outfile;

    public Printer()
    {
        try
        {
            outfile = new PrintWriter(new BufferedWriter(new FileWriter("log/leboncoin.log", true)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void println(String str)
    {
        OUT_CONSOLE.println(str);
        outfile.println(str);
        outfile.flush();
    }

    public void print(String str)
    {
        OUT_CONSOLE.print(str);
        outfile.print(str);
        outfile.flush();
    }

    public void close()
    {
        outfile.close();
    }

}
