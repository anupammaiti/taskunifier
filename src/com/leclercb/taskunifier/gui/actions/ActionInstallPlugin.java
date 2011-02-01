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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionInstallPlugin extends AbstractAction {
	
	public ActionInstallPlugin() {
		this(32, 32);
	}
	
	public ActionInstallPlugin(int width, int height) {
		super(
				Translations.getString("action.name.install_plugin"),
				Images.getResourceImage("download.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.install_plugin"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		this.installPlugin();
	}
	
	public void installPlugin() {
		File file = null;
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return Translations.getString("general.jar_files");
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				
				String extention = FileUtils.getExtention(f.getName());
				
				return "jar".equals(extention);
			}
			
		});
		
		int result = fileChooser.showOpenDialog(MainFrame.getInstance().getFrame());
		
		if (result == JFileChooser.APPROVE_OPTION)
			file = fileChooser.getSelectedFile();
		
		if (file == null || !file.exists())
			return;
		
		if (FileUtils.getExtention(file.getAbsolutePath()).equals("jar")) {
			try {
				File tmpFile = File.createTempFile("tu_plugin_", ".jar");
				
				FileUtils.copyFile(file, tmpFile);
				
				List<SynchronizerGuiPlugin> plugins = Main.API_PLUGINS.loadJar(
						tmpFile,
						false);
				
				if (plugins.size() == 0) {
					throw new Exception(
							Translations.getString("error.no_valid_plugin"));
				}
				
				if (plugins.size() > 1) {
					throw new Exception(
							Translations.getString("error.more_than_one_plugin"));
				}
				
				File outFile = new File(Main.RESOURCES_FOLDER
						+ File.separator
						+ "plugins"
						+ File.separator
						+ plugins.get(0).getId()
						+ ".jar");
				
				if (outFile.exists()) {
					JOptionPane.showMessageDialog(
							MainFrame.getInstance().getFrame(),
							Translations.getString("action.install_plugin.plugin_already_installed"),
							Translations.getString("general.information"),
							JOptionPane.INFORMATION_MESSAGE);
					
					return;
				}
				
				outFile.createNewFile();
				
				FileUtils.copyFile(file, outFile);
				
				plugins = Main.API_PLUGINS.loadJar(outFile);
				
				GuiLogger.getLogger().info(
						"Plugin jar file loaded: " + outFile.getAbsolutePath());
				
				for (SynchronizerGuiPlugin plugin : plugins)
					GuiLogger.getLogger().info(
							"Plugin loaded: "
									+ plugin.getSynchronizerApi().getApiName());
				
				JOptionPane.showMessageDialog(
						MainFrame.getInstance().getFrame(),
						Translations.getString("action.install_plugin.plugin_installed"),
						Translations.getString("general.information"),
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				GuiLogger.getLogger().warning(
						"Could not load plugin jar file: "
								+ file.getAbsolutePath());
				
				ErrorDialog dialog = new ErrorDialog(
						MainFrame.getInstance().getFrame(),
						e,
						true);
				dialog.setVisible(true);
			}
		}
	}
	
}
