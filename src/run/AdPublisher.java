package run;

import java.util.List;
import log.Logger;
import mail.MailManager;
import mail.processing.ConfirmEmail;
import persistence.PersistedObject;
import run.main.App;
import utils.NameFactory;
import utils.StringUtils;
import utils.Utils;
import conf.ConfItem;
import conf.DefaultUserConf;
import connectivity.Connect;
import connectivity.ConnectivityException;

public class AdPublisher implements LeboncoinRunner
{
    @Override
    public void run()
    {
        List<ConfItem> confItems = confReader.getConfItems();
        for (ConfItem confItem : confItems)
        {
            Logger.traceINFO("________________________________________________");
            Logger.traceINFO("ad title : " + confItem.adtitle + ", md5 = " + StringUtils.md5(confItem.adtitle));
            PersistedObject po = new PersistedObject(null, StringUtils.md5(confItem.adtitle));
            if (persistence.persist(po))
            {
                try
                {
                    triggerSend(confItem);
                    int fixedWaitTime = 120;
                    int randomWaitTime = random.nextInt(fixedWaitTime) + fixedWaitTime;
                    Logger.traceINFO("New entry inserted : " + confItem.adtitle);
                    Logger.traceINFO("Waiting for " + randomWaitTime + " seconds before attempting the next entry.");
                    Utils.sleep(randomWaitTime);
                }
                catch (ConnectivityException ce)
                {
                    persistence.remove(po);
                    int waitTime = 600;
                    Logger.traceERROR("Restarting the application in " + waitTime + "sec...");
                    Utils.sleep(waitTime);
                    App.restart();
                }
                catch (Exception e)
                {
                    // Something went wrong.
                    persistence.remove(po);
                    App.kill(e);
                }

            }
            else
            {
                Logger.traceINFO("Entry has already been inserted : " + confItem);
            }
        }
    }

    public static void triggerSend(ConfItem confItem) throws ConnectivityException
    {
        try
        {
            send(confItem);
            int waitTime = 10;
            Logger.traceINFO("Waiting " + waitTime + " seconds before checking email of confirmation.");
            Utils.sleep(waitTime);
            MailManager.doAction(new ConfirmEmail());
        }
        catch (ConnectivityException ce)
        {
            throw ce;
        }
        catch (Exception e)
        {
            App.kill(e);
        }
    }

    private static void send(ConfItem confItem) throws ConnectivityException
    {
        Connect deposit = new Connect();
        String email = DefaultUserConf.EMAIL.trim();
        String regionId = confItem.location.getRegionId().trim();
        String departmentId = confItem.location.getDepartmentId().trim();
        String zipCode = confItem.location.getZipCode().trim();
        String category = NameFactory.WINE_CATEGORY_ID.trim();
        String name = DefaultUserConf.NAME.trim();
        String phone = DefaultUserConf.PHONE_NUMBER.trim();
        String subject = confItem.adtitle.trim();
        String body = confItem.description.trim();
        String price = String.valueOf(confItem.limitPrice);
        String imgPath = confItem.linkPhoto1.trim();

        try
        {
            // First time.
            deposit.upload(regionId, departmentId, zipCode, category, name, email, phone, subject, body, price, imgPath);

            // Second time. Should work only the second time. Who knows why?
            boolean success = deposit.upload(regionId, departmentId, zipCode, category, name, email, phone, subject, body, price, imgPath);

            if (!success)
            {
                throw new Exception("Something went wrong. The request has been rejected.");
            }

        }
        catch (Exception e)
        {
            Logger.traceERROR(e);
            throw new ConnectivityException();
        }
    }

}
