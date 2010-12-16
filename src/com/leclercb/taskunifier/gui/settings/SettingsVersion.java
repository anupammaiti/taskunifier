/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.settings;

import com.leclercb.taskunifier.api.settings.Settings;
import com.leclercb.taskunifier.gui.logger.GuiLogger;

public final class SettingsVersion {

	private SettingsVersion() {

	}

	public static void updateSettings() {
		String version = Settings.getStringProperty("general.version");

		if (version == null)
			version = "0.5.2";

		if (version.equals("0.5.2"))
			version = updateSettings_0_5_2();

		Settings.setStringProperty("general.version", "0.6");
	}

	private static String updateSettings_0_5_2() {
		GuiLogger.getLogger().info("Update settings from version 0.5.2 to 0.6");

		Settings.setStringProperty("date.date_format", "dd/MM/yyyy");
		Settings.setStringProperty("date.time_format", "HH:mm");

		Settings.setStringProperty("theme.lookandfeel", "com.jtattoo.plaf.luna.LunaLookAndFeel$Default");

		Settings.removeProperty("date.simple_time_format");
		Settings.removeProperty("date.date_time_format");

		return "0.6";
	}

}
