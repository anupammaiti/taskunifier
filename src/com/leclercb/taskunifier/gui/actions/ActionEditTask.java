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
package com.leclercb.taskunifier.gui.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSelectionListener;
import com.leclercb.taskunifier.gui.components.tasks.TaskView;
import com.leclercb.taskunifier.gui.components.tasks.edit.TaskEditDialog;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionEditTask extends AbstractAction {
	
	public ActionEditTask(TaskView taskView) {
		this(taskView, 32, 32);
	}
	
	public ActionEditTask(TaskView taskView, int width, int height) {
		super(
				Translations.getString("action.name.edit_task"),
				Images.getResourceImage("edit.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.edit_task"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_E,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		taskView.addTaskSelectionChangeListener(new TaskSelectionListener() {
			
			@Override
			public void taskSelectionChange(TaskSelectionChangeEvent event) {
				ActionEditTask.this.setEnabled(event.getSelectedTasks().length == 1);
			}
			
		});
		
		this.setEnabled(taskView.getSelectedTasks().length == 1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ActionEditTask.editTask();
	}
	
	public static void editTask() {
		Task[] tasks = MainFrame.getInstance().getTaskView().getSelectedTasks();
		
		if (tasks.length != 1)
			return;
		
		TaskEditDialog dialog = new TaskEditDialog(
				tasks[0],
				MainFrame.getInstance().getFrame(),
				true);
		dialog.setVisible(true);
	}
	
}
