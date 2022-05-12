package com.weathered.sql2rpt.util;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class TextUtil {
	public static String encode(String _str){
		try {
			return DatatypeConverter.printBase64Binary(_str.getBytes("UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}
	
	public String decode(String _str){
		byte[] actual = DatatypeConverter.parseBase64Binary(_str);

		try {
			return (new String(actual, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		String a = null;
		Scanner scanner = null;
		while(true) {
			try {	
				if (scanner == null) scanner = new Scanner(System.in);
				
				System.out.print("Enter a string to encode: ");
				
				a = scanner.nextLine();
				System.out.println(encode(a));
				
				a = null;
				
				System.out.print("Do you want to encode more strings: ");
				a = scanner.nextLine();
				
				if(a!=null) {
					if(a.equalsIgnoreCase("N")) {
						if(scanner!=null) scanner.close();
						return;
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
