package log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    private static final String    infoKey  = "INFO";
    private static final String    errorKey = "ERROR";

    private static final Printer   out      = new Printer();

    public static SimpleDateFormat sdf      = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static String getCallingClass()
    {
        final Throwable t = new Throwable();
        final StackTraceElement methodCaller = t.getStackTrace()[3];
        String filename = methodCaller.getFileName();
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    private static String baseLogMessage(String key, String msg)
    {
        return sdf.format(new Date()) + " [" + key + "] [" + getCallingClass() + "] " + msg;
    }

    public static void traceINFO(String msg)
    {
        out.println(baseLogMessage(infoKey, msg));
    }

    public static void traceINFO_NoNewLine(String msg)
    {
        out.print(baseLogMessage(infoKey, msg));
    }

    public static void traceINFO_NoBaseLine(String msg)
    {
        out.println(msg);
    }

    public static void traceERROR(String msg)
    {
        out.println(baseLogMessage(errorKey, msg));
    }

    public static void traceINFO(int level, long[] array)
    {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(level + " : ");
        for (int i = 0; i < array.length; i++)
        {
            if (i == array.length - 1)
            {

                sBuilder.append(array[i]);
            }
            else
            {
                sBuilder.append(array[i] + ", ");
            }
        }
        traceINFO(sBuilder.toString());
    }

    public static void traceINFO(String... msgs)
    {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < msgs.length; i++)
        {
            if (i == msgs.length - 1)
            {

                sBuilder.append(msgs[i]);
            }
            else
            {
                sBuilder.append(msgs[i] + ", ");
            }
        }
        traceINFO(sBuilder.toString());
    }

    public static void traceERROR(Exception e)
    {
        if (e != null)
        {
            out.println(baseLogMessage(errorKey, e.getMessage()));
            e.printStackTrace(System.out);
        }
    }

    public static void endLogging()
    {
        out.close();
    }
}
