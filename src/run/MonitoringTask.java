package run;

import java.util.ArrayList;
import java.util.List;
import log.Logger;
import persistence.PersistedObject;
import recovery.AdRetrieval;
import recovery.UnknownAdException;
import utils.FormatConsoleTable;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import conf.ConfItem;
import conf.ConfReader;
import connectivity.Client;
import connectivity.LinkFactory;

public class MonitoringTask implements LeboncoinRunner
{

    @Override
    public void run()
    {
        FormatConsoleTable nicelyFormattedConsoleTable = new FormatConsoleTable();
        Logger.traceINFO("Generating the report, Please wait...");
        List<ConfItem> foundItems = new ArrayList<>();
        for (PersistedObject pObject : persistence.getPersistedObjects())
        {
            ConfItem item = ConfReader.getConfItemFromHash(confReader, pObject.hash);

            if (item != null)
            {
                foundItems.add(item);

                if (pObject.id == null || pObject.id.isEmpty())
                {
                    nicelyFormattedConsoleTable.print(item.adtitle, item.limitPrice + " EUR", "VALIDATION_PENDING");
                }
                else
                {
                    try
                    {
                        AdRetrieval.retrieveAd(pObject.id);

                        if (checkAdOnline(pObject.id))
                        {
                            nicelyFormattedConsoleTable.print(item.adtitle, item.limitPrice + " EUR", "ONLINE");
                        }
                        else
                        {
                            nicelyFormattedConsoleTable.print(item.adtitle, item.limitPrice + " EUR", "PENDING_DELETED");
                            persistence.remove(pObject);
                        }
                    }
                    catch (UnknownAdException e)
                    {
                        nicelyFormattedConsoleTable.print(item.adtitle, item.limitPrice + " EUR", "OFFLINE");
                    }
                }
            }
        }

        List<ConfItem> allItems = confReader.getConfItems();
        allItems.removeAll(foundItems);

        for (ConfItem item : allItems)
        {
            nicelyFormattedConsoleTable.print(item.adtitle, item.limitPrice + " EUR", "DELETED");
        }

        nicelyFormattedConsoleTable.flush();
    }

    public static boolean checkAdOnline(String adId)
    {
        try
        {
            WebClient client = Client.get();
            String urlDelete = LinkFactory.AD_DELETE_LINK_1 + adId;
            final HtmlPage deletePage1 = client.getPage(urlDelete);
            HtmlRadioButtonInput deleteRadioButton = (HtmlRadioButtonInput) deletePage1.getElementById("cmd_delete");
            deleteRadioButton.setChecked(true);

            HtmlSubmitInput continueButton = deletePage1.getElementByName("continue");
            final HtmlPage deletePage2 = continueButton.click();

            if (deletePage2.asXml().contains("dans quelques minutes") && deletePage2.asXml().contains("Vous ne pouvez pas"))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            Logger.traceERROR(e);
        }

        return true;
    }

}
