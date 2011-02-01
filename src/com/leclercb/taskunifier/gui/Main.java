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
package com.leclercb.taskunifier.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.leclercb.commons.api.plugins.PluginLoader;
import com.leclercb.commons.api.properties.ExtendedProperties;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.api.models.coders.ContextFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.FolderFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.GoalFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.LocationFactoryXMLCoder;
import com.leclercb.taskunifier.api.models.coders.TaskFactoryXMLCoder;
import com.leclercb.taskunifier.api.settings.ModelIdSettingsCoder;
import com.leclercb.taskunifier.gui.actions.ActionCheckVersion;
import com.leclercb.taskunifier.gui.actions.ActionReview;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.components.welcome.LanguageDialog;
import com.leclercb.taskunifier.gui.components.welcome.WelcomeDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.lookandfeel.JTattooLookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.resources.Resources;
import com.leclercb.taskunifier.gui.searchers.coder.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.settings.SettingsVersion;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.synchronizer.dummy.DummyGuiPlugin;
import com.leclercb.taskunifier.gui.template.coder.TemplateFactoryXMLCoder;
import com.leclercb.taskunifier.gui.translations.Translations;

public class Main {
	
	public static PluginLoader<SynchronizerGuiPlugin> API_PLUGINS;
	public static ExtendedProperties SETTINGS;
	public static boolean FIRST_EXECUTION;
	public static String RESOURCES_FOLDER;
	public static String DATA_FOLDER;
	
	public static void main(String[] args) {
		try {
			loadResourceFolder();
			loadDataFolder();
			loadSettings();
			loadLocale();
			loadModels();
			loadLookAndFeel();
			loadApiPlugins();
		} catch (Exception e) {
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				String lookAndFeel = SETTINGS.getStringProperty("theme.lookandfeel");
				
				try {
					if (lookAndFeel != null) {
						LookAndFeelDescriptor laf = LookAndFeelUtils.getLookAndFeel(lookAndFeel);
						if (laf != null)
							laf.setLookAndFeel();
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					ErrorDialog errorDialog = new ErrorDialog(
							MainFrame.getInstance().getFrame(),
							e,
							true);
					errorDialog.setVisible(true);
					
					return;
				}
				
				if (FIRST_EXECUTION) {
					new LanguageDialog(null, true).setVisible(true);
					new WelcomeDialog(null, true).setVisible(true);
				}
				
				MainFrame.getInstance().getFrame().setVisible(true);
				new ActionCheckVersion(true).checkVersion();
				
				Boolean showed = Main.SETTINGS.getBooleanProperty("review.showed");
				if (showed == null || !showed)
					new ActionReview().review();
				
				Main.SETTINGS.setBooleanProperty("review.showed", true);
			}
			
		});
	}
	
	private static void loadResourceFolder() throws Exception {
		if (System.getProperty("com.leclercb.taskunifier.resource_folder") == null) {
			RESOURCES_FOLDER = "resources";
		} else {
			RESOURCES_FOLDER = System.getProperty("com.leclercb.taskunifier.resource_folder");
		}
		
		File file = new File(RESOURCES_FOLDER);
		if (!file.exists() || !file.isDirectory())
			throw new Exception(Translations.getString(
					"error.resources_folder_does_not_exist",
					RESOURCES_FOLDER));
	}
	
	private static void loadDataFolder() throws Exception {
		if (System.getProperty("com.leclercb.taskunifier.data_folder") == null) {
			DATA_FOLDER = "data";
		} else {
			DATA_FOLDER = System.getProperty("com.leclercb.taskunifier.data_folder");
		}
		
		File file = new File(DATA_FOLDER);
		if (!file.exists()) {
			/*
			 * int response = JOptionPane.showConfirmDialog( null,
			 * Translations.getString( "general.create_data_folder_question",
			 * DATA_FOLDER),
			 * Translations.getString("general.create_data_folder"),
			 * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			 * 
			 * if (response == JOptionPane.NO_OPTION) throw new
			 * Exception(Translations.getString( "error.data_folder_needed",
			 * Constants.TITLE));
			 */

			if (!file.mkdir())
				throw new Exception(Translations.getString(
						"error.create_data_folder",
						DATA_FOLDER));
			
			FIRST_EXECUTION = true;
			return;
		} else if (!file.isDirectory()) {
			throw new Exception(Translations.getString(
					"error.data_folder_not_a_folder",
					DATA_FOLDER));
		}
		
		FIRST_EXECUTION = false;
	}
	
	private static void loadSettings() throws Exception {
		try {
			SETTINGS = new ExtendedProperties(new Properties());
			
			SETTINGS.addCoder(new ModelIdSettingsCoder());
			
			SETTINGS.load(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "settings.properties"));
			SettingsVersion.updateSettings();
		} catch (FileNotFoundException e) {
			SETTINGS.load(Resources.class.getResourceAsStream("default_settings.properties"));
			
			if (!FIRST_EXECUTION)
				JOptionPane.showMessageDialog(
						null,
						Translations.getString("error.settings_file"),
						"Error",
						JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void loadLocale() throws Exception {
		Translations.changeLocale(SETTINGS.getLocaleProperty("general.locale"));
	}
	
	private static void loadModels() throws Exception {
		try {
			new ContextFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "contexts.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			new FolderFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "folders.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			new GoalFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "goals.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			new LocationFactoryXMLCoder().decode(new FileInputStream(
					DATA_FOLDER + File.separator + "locations.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			new TaskFactoryXMLCoder().decode(new FileInputStream(DATA_FOLDER
					+ File.separator
					+ "tasks.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			new TemplateFactoryXMLCoder(false).decode(new FileInputStream(
					DATA_FOLDER + File.separator + "templates.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			new TaskSearcherFactoryXMLCoder().decode(new FileInputStream(
					DATA_FOLDER + File.separator + "searchers.xml"));
		} catch (FileNotFoundException e) {}
	}
	
	private static void loadLookAndFeel() throws Exception {
		// Substance
		// Substance issues :
		// State tracking must be done on Event Dispatch Thread
		
		/*
		 * try {
		 * Class.forName("org.pushingpixels.substance.api.SubstanceLookAndFeel"
		 * );
		 * 
		 * Map<String, SkinInfo> lafs = SubstanceLookAndFeel.getAllSkins(); for
		 * (SkinInfo laf : lafs.values()) LookAndFeelUtils.addLookAndFeel(new
		 * SubstanceLookAndFeelDescriptor("Substance - " + laf.getDisplayName(),
		 * laf.getClassName())); } catch (ClassNotFoundException exc) {}
		 */

		// JTattoo
		Properties jtattoo = new Properties();
		jtattoo.load(Resources.class.getResourceAsStream("jtattoo_themes.properties"));
		
		for (Object key : jtattoo.keySet())
			LookAndFeelUtils.addLookAndFeel(new JTattooLookAndFeelDescriptor(
					"jTattoo - " + jtattoo.getProperty(key.toString()),
					key.toString()));
	}
	
	public static void loadApiPlugins() {
		API_PLUGINS = new PluginLoader<SynchronizerGuiPlugin>(
				SynchronizerGuiPlugin.class);
		
		API_PLUGINS.addPlugin(new DummyGuiPlugin());
		
		File pluginsFolder = new File(RESOURCES_FOLDER
				+ File.separator
				+ "plugins");
		
		if (pluginsFolder.exists() && pluginsFolder.isDirectory()) {
			File[] pluginFiles = pluginsFolder.listFiles();
			
			for (File file : pluginFiles) {
				if (file.isFile()
						&& FileUtils.getExtention(file.getAbsolutePath()).equals(
								"jar")) {
					try {
						List<SynchronizerGuiPlugin> plugins = API_PLUGINS.loadJar(file);
						
						if (plugins.size() != 0) {
							GuiLogger.getLogger().info(
									"Plugin jar file loaded: "
											+ file.getAbsolutePath());
							
							for (SynchronizerGuiPlugin plugin : plugins)
								GuiLogger.getLogger().info(
										"Plugin loaded: "
												+ plugin.getSynchronizerApi().getApiName());
						} else {
							GuiLogger.getLogger().info(
									"Jar file doesn't contain any valid plugin: "
											+ file.getAbsolutePath());
						}
					} catch (Exception e) {
						GuiLogger.getLogger().warning(
								"Could not load plugin jar file: "
										+ file.getAbsolutePath());
					}
				}
			}
		}
	}
	
	public static void stop() {
		GuiLogger.getLogger().info("Exiting " + Constants.TITLE);
		
		try {
			new ContextFactoryXMLCoder().encode(new FileOutputStream(
					DATA_FOLDER + File.separator + "contexts.xml"));
			new FolderFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER
					+ File.separator
					+ "folders.xml"));
			new GoalFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER
					+ File.separator
					+ "goals.xml"));
			new LocationFactoryXMLCoder().encode(new FileOutputStream(
					DATA_FOLDER + File.separator + "locations.xml"));
			new TaskFactoryXMLCoder().encode(new FileOutputStream(DATA_FOLDER
					+ File.separator
					+ "tasks.xml"));
			new TemplateFactoryXMLCoder(false).encode(new FileOutputStream(
					DATA_FOLDER + File.separator + "templates.xml"));
			new TaskSearcherFactoryXMLCoder().encode(new FileOutputStream(
					DATA_FOLDER + File.separator + "searchers.xml"));
			
			saveSettings();
			
			MainFrame.getInstance().getFrame().setVisible(false);
			MainFrame.getInstance().getFrame().dispose();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		System.exit(0);
	}
	
	public static void saveSettings() throws FileNotFoundException, IOException {
		SETTINGS.store(new FileOutputStream(DATA_FOLDER
				+ File.separator
				+ "settings.properties"), Constants.TITLE + " Settings");
	}
	
}
