package mail.processing;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import log.Logger;
import persistence.PersistedObject;
import persistence.Persistence;
import utils.StringUtils;

public class VerifyPublicationResultEmail implements MessageProcess
{
    private Persistence persistence;

    @Override
    public void treatMessage(Message message) throws Exception
    {
        String sender = InternetAddress.toString(message.getFrom());
        String subject = message.getSubject();
        if (sender.contains("leboncoin.fr") && subject.contains("est en ligne"))
        {
            // ACCEPTED
            String id = extractAdId(message.getContent().toString());
            String name = extractAdName(subject);
            persistence.update(new PersistedObject(id, StringUtils.md5(name)));
            Logger.traceINFO("[ACCEPTED] Ad {" + name + "} has been accepted! Updating the persistence with new id : " + id);
            message.setFlag(Flag.SEEN, true);
        }
        else if (sender.contains("leboncoin.fr") && subject.contains("sur leboncoin.fr"))
        {
            // REFUSED
            String name = extractAdName(subject);
            Logger.traceERROR("[REJECTED] Ad {" + name + "} has been rejected! Cleaning the ad from the persistence.");
            persistence.remove(new PersistedObject(null, StringUtils.md5(name)));
            message.setFlag(Flag.SEEN, true);
        }
    }

    private static String extractAdId(String mailBody)
    {
        String url = StringUtils.truncBeforeAndOverSymbol(mailBody, "http");
        int id = StringUtils.extractInt(url);
        return String.valueOf(id);
    }

    private static String extractAdName(String subject)
    {
        // Mail subject and not mail body.
        String res = StringUtils.truncBeforeAndOverSymbol(subject, "\"");
        res = StringUtils.truncAfter(res, "\"");
        res = res.replaceAll("\"", "");
        return res.trim();
    }

    public VerifyPublicationResultEmail(Persistence persistence)
    {
        this.persistence = persistence;
    }

}
