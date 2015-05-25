package run;

import java.util.Calendar;
import java.util.Date;
import log.Logger;
import persistence.PersistedObject;
import recovery.Ad;
import recovery.AdRetrieval;
import recovery.UnknownAdException;
import run.main.App;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import conf.ConfItem;
import conf.ConfReader;
import conf.DefaultUserConf;
import connectivity.Client;
import connectivity.LinkFactory;

public class AdRepublisher implements LeboncoinRunner
{
    @Override
    public void run()
    {
        for (PersistedObject pObject : persistence.getPersistedObjects())
        {
            if (pObject.id != null && !pObject.id.isEmpty())
            {
                try
                {
                    Ad ad = AdRetrieval.retrieveAd(pObject.id.trim());
                    Logger.traceINFO("Found Ad : " + ad.name + " posted on " + Logger.sdf.format(ad.dateOfPost));

                    ConfItem item = ConfReader.getConfItemFromHash(confReader, pObject.hash);

                    if (item == null)
                    {
                        Logger.traceINFO("Unable to find conf item for ad " + ad + ". The ad will no longer be republished.");

                        if (removeAd(pObject.id))
                        {
                            Logger.traceINFO("[SUCCESS] Ad removal request has been taken into account.");
                            persistence.remove(pObject);
                        }
                        else
                        {
                            Logger.traceERROR("[ERROR] Cannot remove the ad. Leboncoin has rejected the removal request.");
                        }

                        continue;
                    }

                    Calendar published = Calendar.getInstance();
                    published.setTime(ad.dateOfPost);
                    published.add(Calendar.DAY_OF_MONTH, item.daysCountBeforeRefresh);

                    if (published.getTime().before(new Date()))
                    {
                        // The id will be published at startup because it is no longer in the persistence.
                        Logger.traceINFO("Ad " + ad + " is too old. Starting the removal process.");

                        if (removeAd(pObject.id))
                        {
                            Logger.traceINFO("[SUCCESS] Ad removal request has been taken into account.");
                        }
                        else
                        {
                            Logger.traceERROR("[ERROR] Cannot remove the ad. Leboncoin has rejected the removal request.");
                        }
                    }

                }
                catch (UnknownAdException e)
                {
                    Logger.traceERROR("The ad with id : " + pObject.id + " is not yet published. Waiting before attempting again. Program will exit.");
                    App.kill(e);
                }
            }
        }
    }

    private static boolean removeAd(String adId)
    {
        try
        {
            WebClient client = Client.get();
            String urlDelete = LinkFactory.AD_DELETE_LINK_1 + adId;
            Logger.traceINFO("URL for ad delete is : " + urlDelete);
            final HtmlPage deletePage1 = client.getPage(urlDelete);
            HtmlRadioButtonInput deleteRadioButton = (HtmlRadioButtonInput) deletePage1.getElementById("cmd_delete");
            deleteRadioButton.setChecked(true);

            HtmlSubmitInput continueButton = deletePage1.getElementByName("continue");
            final HtmlPage deletePage2 = continueButton.click();

            if (!deletePage2.asXml().contains("votre mot de passe tient compte des majuscules"))
            {
                Logger.traceERROR("Unknown error. Cannot remove ad with id : " + adId);
            }

            HtmlPasswordInput passwordInput = deletePage2.getElementByName("passwd");
            passwordInput.setValueAttribute(DefaultUserConf.PASSWORD);

            final HtmlSelect causeSelect = deletePage2.getElementByName("delete_reason");
            HtmlOption autreCauseSelect = causeSelect.getOptionByValue("5");
            causeSelect.setSelectedAttribute(autreCauseSelect, true);

            HtmlSubmitInput continueButton2 = deletePage2.getElementByName("continue");
            HtmlPage result = continueButton2.click();

            return result.asXml().contains("Votre annonce sera supprim") && result.asXml().contains("lors de la prochaine mise à jour");
        }
        catch (Exception e)
        {
            Logger.traceERROR(e);
            return false;
        }
    }

}
