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
package com.leclercb.taskunifier.gui.components.batchaddtask;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.commons.models.TemplateModel;
import com.leclercb.taskunifier.gui.commons.renderers.TemplateListCellRenderer;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class BatchAddTaskDialog extends JDialog {
	
	private JTextArea answerTextArea;
	private JComboBox templateComboBox;
	
	public BatchAddTaskDialog(Frame frame, boolean modal) {
		super(frame, modal);
		this.initialize();
	}
	
	private void initialize() {
		this.setTitle(Translations.getString("general.batch_add_tasks"));
		this.setSize(500, 300);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		if (this.getOwner() != null)
			this.setLocationRelativeTo(this.getOwner());
		
		JPanel panel = null;
		
		panel = new JPanel(new BorderLayout(20, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.add(panel, BorderLayout.NORTH);
		
		JLabel icon = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
		panel.add(icon, BorderLayout.WEST);
		
		JLabel question = new JLabel(
				Translations.getString("batch_add_tasks.insert_task_titles"));
		panel.add(question, BorderLayout.CENTER);
		
		panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
		
		this.answerTextArea = new JTextArea();
		this.answerTextArea.setEditable(true);
		
		JPanel templatePanel = new JPanel();
		templatePanel.setLayout(new BorderLayout());
		
		this.templateComboBox = new JComboBox();
		this.templateComboBox.setModel(new TemplateModel());
		this.templateComboBox.setRenderer(new TemplateListCellRenderer());
		
		templatePanel.add(new JLabel(Translations.getString("general.template")
				+ ": "), BorderLayout.WEST);
		templatePanel.add(this.templateComboBox, BorderLayout.CENTER);
		
		panel.add(
				ComponentFactory.createJScrollPane(this.answerTextArea, true),
				BorderLayout.CENTER);
		panel.add(templatePanel, BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel(buttonsPanel);
	}
	
	private void initializeButtonsPanel(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == "OK") {
					String answer = BatchAddTaskDialog.this.answerTextArea.getText();
					Template template = (Template) BatchAddTaskDialog.this.templateComboBox.getSelectedItem();
					
					if (answer == null)
						return;
					
					MainFrame.getInstance().getSearcherView().selectDefaultTaskSearcher();
					
					String[] titles = answer.split("\n");
					
					Synchronizing.setSynchronizing(true);
					
					List<Task> tasks = new ArrayList<Task>();
					for (String title : titles) {
						title = title.trim();
						
						if (title.length() == 0)
							continue;
						
						Task task = TaskFactory.getInstance().create("");
						
						if (template != null)
							template.applyToTask(task);
						
						task.setTitle(title);
						
						tasks.add(task);
					}
					
					Synchronizing.setSynchronizing(false);
					
					MainFrame.getInstance().getTaskView().setSelectedTasks(
							tasks.toArray(new Task[0]));
					
					BatchAddTaskDialog.this.dispose();
				}
				
				if (event.getActionCommand() == "CANCEL") {
					BatchAddTaskDialog.this.dispose();
				}
			}
			
		};
		
		JButton okButton = new JButton(Translations.getString("general.ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		buttonsPanel.add(okButton);
		
		JButton cancelButton = new JButton(
				Translations.getString("general.cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(listener);
		buttonsPanel.add(cancelButton);
		
		this.getRootPane().setDefaultButton(okButton);
	}
	
}
