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
package com.leclercb.taskunifier.gui.components.help;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.Main;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public final class Help {
	
	private Help() {

	}
	
	private static final String HELP_FILES_FOLDER = Main.RESOURCES_FOLDER
			+ File.separator
			+ "help";
	
	public static String getContent(String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");
		
		helpFile = HELP_FILES_FOLDER + File.separator + helpFile;
		StringBuffer content = new StringBuffer();
		
		try {
			InputStreamReader inputStream = new InputStreamReader(
					new FileInputStream(helpFile));
			BufferedReader buffer = new BufferedReader(inputStream);
			
			String line = null;
			while ((line = buffer.readLine()) != null)
				content.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String c = content.toString();
		
		// Replace parameters
		c = c.replace(
				"{resources_folder}",
				new File(Main.RESOURCES_FOLDER).getAbsolutePath());
		
		return c;
	}
	
	public static JDialog getHelpDialog(final String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");
		
		return new HelpDialog(helpFile);
	}
	
	public static Component getHelpButton(final String helpFile) {
		CheckUtils.isNotNull(helpFile, "Help file cannot be null");
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		JButton component = new JButton(Images.getResourceImage(
				"help.png",
				16,
				16));
		component.setBorderPainted(false);
		component.setContentAreaFilled(false);
		component.addActionListener(new HelpActionListener(helpFile));
		
		panel.add(component);
		
		return panel;
	}
	
	private static class HelpActionListener implements ActionListener {
		
		private String helpFile;
		
		public HelpActionListener(String helpFile) {
			this.helpFile = helpFile;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			HelpDialog dialog = new HelpDialog(this.helpFile);
			dialog.setVisible(true);
		}
		
	}
	
	private static class HelpDialog extends JDialog {
		
		public HelpDialog(String helpFile) {
			super(MainFrame.getInstance().getFrame(), true);
			
			this.setTitle(Translations.getString("general.help"));
			this.setSize(600, 600);
			this.setResizable(true);
			this.setLayout(new BorderLayout());
			
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setLocationRelativeTo(MainFrame.getInstance().getFrame());
			
			final JEditorPane pane = new JEditorPane();
			pane.setContentType("text/html");
			pane.setText(getContent(helpFile));
			pane.setEditable(false);
			pane.setCaretPosition(0);
			
			pane.addHyperlinkListener(new HyperlinkListener() {
				
				@Override
				public void hyperlinkUpdate(HyperlinkEvent evt) {
					if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						try {
							pane.setText(getContent(evt.getURL().getFile()));
							pane.setCaretPosition(0);
						} catch (Exception exc) {
							ErrorDialog errorDialog = new ErrorDialog(
									MainFrame.getInstance().getFrame(),
									Translations.getString("error.help_file_not_found"),
									exc,
									true);
							errorDialog.setVisible(true);
						}
					}
				}
				
			});
			
			this.add(
					ComponentFactory.createJScrollPane(pane, false),
					BorderLayout.CENTER);
		}
		
	}
	
}