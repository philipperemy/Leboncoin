package mail.processing;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import log.Logger;

public class CleanDeletingAdEmail implements MessageProcess
{
    @Override
    public void treatMessage(Message message) throws Exception
    {
        String sender = InternetAddress.toString(message.getFrom());
        String subject = message.getSubject();
        if (sender.contains("leboncoin.fr") && subject.contains("Suppression de votre annonce"))
        {
            Logger.traceINFO("Received : " + subject);
            Logger.traceINFO("Cleaning this email");
            message.setFlag(Flag.SEEN, true);
        }
    }

}
