package run;

import mail.MailManager;
import mail.processing.CleanDeletingAdEmail;

public class DeleteAdMailCleaner implements LeboncoinRunner
{
    @Override
    public void run()
    {
        MailManager.doAction(new CleanDeletingAdEmail());
    }
}
