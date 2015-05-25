package mail.processing;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import log.Logger;
import utils.StringUtils;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import connectivity.Client;

public class ConfirmEmail implements MessageProcess
{

    @Override
    public void treatMessage(Message message) throws Exception
    {
        String sender = InternetAddress.toString(message.getFrom());
        String subject = message.getSubject();
        if (sender.contains("leboncoin.fr") && subject.contains("Activez votre annonce"))
        {
            Logger.traceINFO("Found confirmation mail - " + subject);
            String url = extractConfirmationUrl(message.getContent().toString());

            Logger.traceINFO("Sending confirmation : " + url);
            HtmlPage confirmationPage = Client.get().getPage(url);
            String confirmationStr = confirmationPage.asXml();

            if (confirmationStr.contains("Vous nous avez déjà envoyé une confirmation"))
            {
                Logger.traceERROR("Mail already confirmed. This is a huge error.");
            }

            Logger.traceINFO("Confirmation sent for mail - " + subject);
            message.setFlag(Flag.SEEN, true);
        }
    }

    private static String extractConfirmationUrl(String mailBody)
    {
        String url = StringUtils.truncBefore(mailBody, "http");
        url = StringUtils.truncAfter(url, "\r");
        return url.trim();
    }

}
