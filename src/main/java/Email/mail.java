package Email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class mail {

	public static void main(String[] args) throws EmailException {
		// TODO Auto-generated method stub

		System.out.println("======mailstart==============");
		 // Create the attachment
		  EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath("..//maven.selenium.testng//target//surefire-reports//emailable-report.html");
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  attachment.setDescription("solar battery charger testcase report");
		  attachment.setName("report");

		  // Create the email message
		  MultiPartEmail email = new MultiPartEmail();
		  email.setHostName("smtp.gmail.com");
		  email.setSmtpPort(465);
		  email.setSSLOnConnect(true);
		  email.setAuthenticator(new DefaultAuthenticator("12jarvis89@gmail.com", "@code339#mark2"));
		  //email.setSSLOnConnect(true);
		  email.setFrom("12jarvis89@gmail.com");
		  email.addTo("abhi2d3y@gmail.com");

		  email.setSubject("Solar Battery Charger Test Report");
		 // email.setMsg("");

		  // add the attachment
		  email.attach(attachment);

		  // send the email
		  email.send();
		  System.out.println("=========mailend===========");
	}

}
