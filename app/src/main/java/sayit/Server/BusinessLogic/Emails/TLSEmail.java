package sayit.Server.BusinessLogic.Emails;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class TLSEmail {

	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL: smtp.gmail.com (use authentication)
	   Use Authentication: Yes
	   Port for TLS/STARTTLS: 587
	 */

	/**
	 * 
	 * @param fromEmail requires valid gmail id
	 * @param password correct password for gmail id
	 * @param toEmail can be any email id
	 * @param SMTPHost SMTP Host
	 * @param TLSPort TLS Port
	 * @return true if email successfully sent, false otherwise
	 */
	public boolean TLSSendEmail(String fromEmail, String password, String toEmail, String SMTPHost, String TLSPort, String subject, String body) {
		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTPHost);
		props.put("mail.smtp.port", TLSPort); 
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		
		return EmailUtil.sendEmail(session, toEmail, subject, body);
		
	}

	
}