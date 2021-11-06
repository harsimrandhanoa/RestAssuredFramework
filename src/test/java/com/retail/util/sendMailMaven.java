package com.retail.util;

import java.io.File;

public class sendMailMaven {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//GmailAppMaven.sendMail("dhanoahs3@gmail.com");

		//GmailAppAttachmentMaven.sendMail("dhanoahs3@gmail.com");
		File report = ZipAndMail.zipFiles();
		ZipAndMail.sendMail("dhanoahs3@gmail.com",report);

	}


}
