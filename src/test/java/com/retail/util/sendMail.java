package com.retail.util;

import java.io.File;

public class sendMail {

	public static void main(String[] args) throws Exception {
		File report = ZipAndMail.zipFiles();
		ZipAndMail.sendMail(report);

	}

}
