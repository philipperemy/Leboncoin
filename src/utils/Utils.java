package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import log.Logger;

public class Utils
{
    public static void sleep(int sec)
    {
        try
        {
            Thread.sleep(sec * 1000);
        }
        catch (InterruptedException e)
        {
            Logger.traceERROR(e);
        }
    }

    public static void debug(String content)
    {
        try
        {
            File file = new File("debug.twt");

            // if file doesnt exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
