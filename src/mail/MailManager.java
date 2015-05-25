package mail;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import conf.DefaultUserConf;
import log.Logger;
import mail.processing.MessageProcess;

public class MailManager {

	public synchronized static boolean doAction(MessageProcess f) {
		return doActionGmail(f, 0);
	}

	static int MAX_ATTEMPS = 10;

	public synchronized static boolean doActionGmail(MessageProcess f,
			int attempts) {
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", DefaultUserConf.EMAIL,
					DefaultUserConf.PASSWORD);

			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);

			// Get directory
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message messages[] = inbox.search(unseenFlagTerm);

			for (int i = 0; i < messages.length; i++) {
				f.treatMessage(messages[i]);
			}

			// Close connection
			inbox.close(true);
			store.close();

			return true;
		} catch (Exception e) {
			Logger.traceERROR(e);
			Logger.traceERROR("Attempts : " + attempts + " / " + MAX_ATTEMPS);
			if (attempts > MAX_ATTEMPS) {
				return false;
			} else {
				doActionGmail(f, attempts++);
			}
		}
		return false;
	}

	public synchronized static boolean doActionAEI(MessageProcess f,
			int attempts) {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "ssl0.ovh.net");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									"philippe.remy%junior-aei.com", "phremy");
						}
					});

			Store store = session.getStore("imap");
			store.connect("ssl0.ovh.net", "philippe.remy%junior-aei.com",
					"phremy");

			// Get folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			// Get directory
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message messages[] = inbox.search(unseenFlagTerm);

			for (int i = 0; i < messages.length; i++) {
				f.treatMessage(messages[i]);
			}

			// Close connection
			inbox.close(true);
			store.close();

			return true;
		} catch (Exception e) {
			Logger.traceERROR(e);
			Logger.traceERROR("Attempts : " + attempts + " / " + MAX_ATTEMPS);
			if (attempts > MAX_ATTEMPS) {
				return false;
			} else {
				doActionAEI(f, attempts++);
			}
		}

		return false;
	}

}
