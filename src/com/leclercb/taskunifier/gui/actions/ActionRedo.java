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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.undo.IRedoListener;
import com.leclercb.taskunifier.gui.undo.IUndoListener;

public class ActionRedo extends AbstractAction implements UndoableEditListener, IUndoListener, IRedoListener {
	
	public ActionRedo() {
		this(32, 32);
	}
	
	public ActionRedo(int width, int height) {
		super(
				Translations.getString("action.name.redo"),
				Images.getResourceImage("redo.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.redo"));
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_Y,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		this.updateAction();
		
		Constants.UNDO_MANAGER.addUndoListener(this);
		Constants.UNDO_MANAGER.addRedoListener(this);
		Constants.UNDO_EDIT_SUPPORT.addUndoableEditListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Constants.UNDO_MANAGER.redo();
	}
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		this.updateAction();
	}
	
	@Override
	public void undoPerformed(ActionEvent event) {
		this.updateAction();
	}
	
	@Override
	public void redoPerformed(ActionEvent event) {
		this.updateAction();
	}
	
	private void updateAction() {
		this.setEnabled(Constants.UNDO_MANAGER.canRedo());
		this.putValue(NAME, Constants.UNDO_MANAGER.getRedoPresentationName());
	}
	
}
