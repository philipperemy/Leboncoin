package mail.processing;

import javax.mail.Message;

public interface MessageProcess
{
    void treatMessage(Message message) throws Exception;
}
