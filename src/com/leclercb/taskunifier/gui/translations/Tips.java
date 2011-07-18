/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.translations;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.leclercb.commons.api.utils.ResourceBundleUtils;
import com.leclercb.taskunifier.gui.main.Main;

public final class Tips {
	
	private Tips() {

	}
	
	private static final String bundleFolder = Main.RESOURCES_FOLDER
			+ File.separator
			+ "translations";
	
	private static final String bundleName = "Tips";
	
	private static final String defaultBundle = bundleFolder
			+ File.separator
			+ bundleName
			+ ".properties";
	
	private static Map<Locale, File> locales;
	private static Properties defaultProperties;
	private static Properties properties;
	
	static {
		try {
			locales = ResourceBundleUtils.getAvailableLocales(new File(
					bundleFolder), bundleName);
		} catch (Exception e) {
			e.printStackTrace();
			locales = new HashMap<Locale, File>();
		}
		
		try {
			defaultProperties = new Properties();
			defaultProperties.load(new FileInputStream(defaultBundle));
		} catch (Exception e) {
			e.printStackTrace();
			defaultProperties = null;
		}
	}
	
	protected static void setLocale(Locale locale) {
		File file = locales.get(locale);
		
		if (file == null) {
			properties = null;
			return;
		}
		
		try {
			properties = new Properties();
			properties.load(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
			properties = null;
		}
	}
	
	public static Properties getProperties() {
		if (properties != null)
			return properties;
		
		return defaultProperties;
	}
	
}
