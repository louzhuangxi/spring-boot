package org.h819.web.i18n;

import java.util.Locale;
/**
 * 
 * 本函数用来列出所有的 国家和语言代码
 * 
 * */
public class LocaleList {
	public static void main(String[] args) {
		Locale[] localeList = Locale.getAvailableLocales();
		for (int i = 0; i < localeList.length; i++) {
			System.out.println("...............................");
			System.out.println(localeList[i].getDisplayCountry() + "(国家代号)"
					+ localeList[i].getCountry() + " ; "
					+ localeList[i].getDisplayLanguage() + "(语言代号)"
					+ localeList[i].getLanguage());
		}
	}
}
