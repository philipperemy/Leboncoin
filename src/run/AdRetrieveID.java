package run;

import mail.MailManager;
import mail.processing.VerifyPublicationResultEmail;

public class AdRetrieveID implements LeboncoinRunner
{
    @Override
    public void run()
    {
        MailManager.doAction(new VerifyPublicationResultEmail(persistence));
    }

}
