package com.retail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import io.restassured.RestAssured;


public class ZipAndMail {
	
	public static File zipFiles() throws Exception{
		
		 File dir = new File(System.getProperty("user.dir") + "//reports//");


        // Note that this time we are using a File class as an array, instead of String
	    File[] files = dir.listFiles();
	    File lastModified = getFileLastModified(files);
	
	    //File lastModified = Arrays.stream(files).filter(File::isDirectory).max(Comparator.comparing(File::lastModified)).orElse(null);
		String srcFolder=lastModified.getAbsolutePath();
		String destFolder=System.getProperty("user.dir") + "//reports//";
		String zippedFilename="zippedReports.zip";
		
		 Zip.zipDir(srcFolder, destFolder+zippedFilename);
		
		 File zippedFile = new File(destFolder+zippedFilename);
		 return zippedFile;
	}
	
	public static  void sendMail(File report) throws Exception{
		
        Properties properties = new Properties();
		properties.put("mail.smtp.auth","true"); //Do we need authentication to send email. For gmail set to true
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port","587");
		
		
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//project.properties");
		Properties projectData = new Properties();
		projectData.load(fs);
	
		final String myAccount  = projectData.getProperty("senderAccount");
		final String myPassword  = projectData.getProperty("senderPassword");
		
		String recipient = projectData.getProperty("recipientAccount");
		
		
		Session session = Session.getInstance(properties,new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(myAccount,myPassword);
				}
			});
		
		
		Message message  = prepareMessage(session,myAccount,recipient,report);
		Transport.send(message);
	}
	
	
	
	
	public static Message prepareMessage(Session session,String myAccount,String recipient,File report) throws IOException{
        MimeMessage message = new MimeMessage(session);
        try {
			message.setFrom(new InternetAddress(myAccount));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
			message.setSubject("Rest reports");
			
			String htmlText  = "<h1>Please find attached the reports from latest run of rest api testing project </h1> </br>"
					+ " <h2><em>You can find individual test requests in logs folder and extent reports file named index under current directory"
					+ "</em></h2>";
            MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(htmlText,"text/html");
			
			
			 MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			// >C:\testing\Soap_Protocol\zipfiles\htmlreport.zip
			 attachmentBodyPart.attachFile(report);
			 
			 MimeMultipart multiPart = new MimeMultipart();
			 multiPart.addBodyPart(bodyPart);
			 multiPart.addBodyPart(attachmentBodyPart);
			 message.setContent(multiPart);

			return message;
		} catch (MessagingException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
		
	}
	
	private static File getFileLastModified(File[] files) {
	    File fileLastModified = null;
	    long maxLastModified = Long.MIN_VALUE;
	    for (File file : files) {
	        if (file.isDirectory()) {
	            final long lastModified = file.lastModified();
	            if (lastModified > maxLastModified) {
	                fileLastModified = file;
	                maxLastModified = lastModified;
	            }
	        }
	    }
	    return fileLastModified;
	}
	

}