package tenxertech.autoTesting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class reportSend {

	public static void main(String[] args) throws IOException, EmailException {
		System.out.println("======mailstart==============");
		byte[] fileContent;
		String encodedString;
		
		File screenshot=new File("..\\com.tenxertech.autoTesting\\screenshot\\");
		
		if(screenshot.exists())
		{
			File[] screen=screenshot.listFiles();
		for(File sf:screen)
		{
			fileContent = FileUtils.readFileToByteArray(sf);
			encodedString = Base64.getEncoder().encodeToString(fileContent);
			String ht="<br><figure> <img src='data:image/jpeg;base64,"+encodedString+"' width='100%' height='auto' /> <figcaption style='text-align: center;'>"+sf.getName()+"</figcaption></figure><br>";
			try (FileWriter f = new FileWriter("../com.tenxertech.autoTesting/target/surefire-reports/emailable-report.html", true);
	                BufferedWriter b = new BufferedWriter(f);
	                PrintWriter p = new PrintWriter(b);) {

	            p.println(ht);

	        } catch (IOException i) {
	            i.printStackTrace();
	        }
		}
		
		}
		
		 // Create the attachment
		EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath("..//com.tenxertech.autoTesting//target//surefire-reports//emailable-report.html");
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  attachment.setDescription("solar battery charger testcase report");
		  attachment.setName("report");
		  
		  // Create the email message
		  MultiPartEmail email = new MultiPartEmail();
		  email.setHostName("smtp.gmail.com");
		  email.setSmtpPort(465);
		  email.setSSLOnConnect(true);
		  email.setAuthenticator(new DefaultAuthenticator("autotesting37@gmail.com", "Niwm3TVCViuVpxu"));
		  //email.setSSLOnConnect(true);
		  email.setFrom("autotesting37@gmail.com");
		  email.addTo("abhi2d3y@gmail.com");

		  email.setSubject("Solar Battery Charger Test Report");
		  // add the attachment
		  email.attach(attachment);
		 // email.attach(attachment2);

		  // send the email
		  email.send();
		  System.out.println("=========mailend===========");
	

	}

}
