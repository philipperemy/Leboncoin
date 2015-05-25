package connectivity;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import log.Logger;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

public class Client
{
    private static volatile WebClient webClient;

    public synchronized static WebClient get() throws FileNotFoundException
    {
        if (webClient == null)
        {
            Logger.traceINFO_NoNewLine("Initialization of the client...");
            webClient = getLeboncoin();
            System.setErr(new PrintStream("NUL"));
            Logger.traceINFO_NoBaseLine(" [DONE]");
        }

        return webClient;
    }

    public synchronized static void reset()
    {
        webClient.closeAllWindows();
        webClient = null;
    }

    private static WebClient getLeboncoin()
    {
        final WebClient client = new WebClient(BrowserVersion.FIREFOX_17);
        try
        {
            client.getOptions().setJavaScriptEnabled(true);
            client.getOptions().setAppletEnabled(false);
            client.getOptions().setCssEnabled(false);
            client.getOptions().setPrintContentOnFailingStatusCode(true);
            client.getOptions().setPopupBlockerEnabled(true);
            client.getOptions().setThrowExceptionOnScriptError(false);
            client.getOptions().setUseInsecureSSL(true);
        }
        catch (Exception e)
        {
            Logger.traceERROR(e);
        }

        return client;
    }

}
