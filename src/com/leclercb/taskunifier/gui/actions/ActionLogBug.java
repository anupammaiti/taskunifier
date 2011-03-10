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

import javax.swing.AbstractAction;

import com.leclercb.commons.gui.utils.BrowserUtils;
import com.leclercb.taskunifier.gui.components.error.ErrorDialog;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ActionLogBug extends AbstractAction {
	
	public ActionLogBug() {
		super(Translations.getString("action.name.log_bug"));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.description.log_bug"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		this.logBug();
	}
	
	public void logBug() {
		try {
			BrowserUtils.openDefaultBrowser(Constants.BUG_URL);
		} catch (Exception e) {
			ErrorDialog dialog = new ErrorDialog(
					MainFrame.getInstance().getFrame(),
					null,
					e,
					false);
			dialog.setVisible(true);
		}
	}
	
}
