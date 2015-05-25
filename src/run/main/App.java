package run.main;

import log.Logger;
import run.AdPublisher;
import run.AdRepublisher;
import run.AdRetrieveID;
import run.DeleteAdMailCleaner;
import run.MonitoringTask;
import connectivity.Client;

public class App
{
    public static void main(String[] args)
    {
        start();
        stop();
    }

    public static void start()
    {
        new DeleteAdMailCleaner().run();
        new AdRetrieveID().run();
        new AdPublisher().run();
        new AdRepublisher().run();
        new MonitoringTask().run();
    }
    
    public static void stop()
    {
    	Logger.endLogging();
    }

    public static void restart()
    {
        Client.reset();
        start();
    }

    public static void kill()
    {
        kill(null);
    }

    public static void kill(Exception e)
    {
        Logger.traceERROR(e);
        Logger.traceERROR("Killing the app...");
        System.exit(-1);
    }
}
