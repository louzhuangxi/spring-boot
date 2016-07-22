package org.h819.web.i18n;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * 获得 local,具体参见web应用
 * @author jianghui
 *
 */
public class Locales {
	private Locale current;

	public void setCurrent(Locale cur) {
		this.current = cur;
	}

	public Map<String, Locale> getLocales() {
		Map<String, Locale> locales = new Hashtable<String, Locale>();
		ResourceBundle bundle = ResourceBundle.getBundle("globalMessages",
				current);
		
		//可以添加其他的支持语言
		locales.put(bundle.getString("language.usen"), Locale.US);
		locales.put(bundle.getString("language.zhcn"), Locale.CHINA);
		locales.put(bundle.getString("language.nlnl"), new Locale("nl","NL"));
		return locales;
	}

	public Locale getCurrent() {
		return current;
	}
}
