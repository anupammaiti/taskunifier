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
package com.leclercb.taskunifier.gui.components.searcheredit.sorter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leclercb.taskunifier.gui.api.searchers.TaskSorter;
import com.leclercb.taskunifier.gui.api.searchers.TaskSorter.TaskSorterElement;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.utils.Images;

public class TaskSorterPanel extends JPanel {
	
	private TaskSorter sorter;
	private TaskSorterTable table;
	
	private JButton addButton;
	private JButton removeButton;
	
	public TaskSorterPanel(TaskSorter sorter) {
		this.sorter = sorter;
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		this.table = new TaskSorterTable(this.sorter);
		this.table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent event) {
						if (event.getValueIsAdjusting())
							return;
						
						if (TaskSorterPanel.this.table.getSelectedRow() == -1) {
							TaskSorterPanel.this.removeButton.setEnabled(false);
						} else {
							TaskSorterPanel.this.removeButton.setEnabled(true);
						}
					}
					
				});
		
		tablePanel.add(this.table.getTableHeader(), BorderLayout.NORTH);
		tablePanel.add(this.table, BorderLayout.CENTER);
		
		this.add(tablePanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.initializeButtons(buttonsPanel);
	}
	
	private void initializeButtons(JPanel buttonsPanel) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("ADD")) {
					TaskSorterPanel.this.sorter.addElement(new TaskSorterElement(
							0,
							TaskColumn.TITLE,
							SortOrder.ASCENDING));
				} else {
					TaskSorterPanel.this.table.getCellEditor().stopCellEditing();
					TaskSorterElement element = ((TaskSorterTableModel) TaskSorterPanel.this.table.getModel()).getTaskSorterElement(TaskSorterPanel.this.table.getSelectedRow());
					TaskSorterPanel.this.sorter.removeElement(element);
				}
			}
			
		};
		
		this.addButton = new JButton(Images.getResourceImage("add.png", 16, 16));
		this.addButton.setActionCommand("ADD");
		this.addButton.addActionListener(listener);
		buttonsPanel.add(this.addButton);
		
		this.removeButton = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		this.removeButton.setActionCommand("REMOVE");
		this.removeButton.addActionListener(listener);
		this.removeButton.setEnabled(false);
		buttonsPanel.add(this.removeButton);
	}
	
}
