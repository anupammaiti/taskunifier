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

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.UserUtils;

public class ActionSwitchToUserMenu extends AbstractAction {
	
	private JPopupMenu popupMenu;
	
	public ActionSwitchToUserMenu() {
		this(32, 32);
	}
	
	public ActionSwitchToUserMenu(int width, int height) {
		super(
				Translations.getString("action.switch_user_menu"),
				ImageUtils.getResourceImage("user.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.switch_user_menu"));
		
		this.popupMenu = new JPopupMenu(
				Translations.getString("action.switch_user_menu"));
		
		UserUtils.updateUserList(this.popupMenu);
		
		UserUtils.getInstance().addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				UserUtils.updateUserList(ActionSwitchToUserMenu.this.popupMenu);
			}
			
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component)
			this.popupMenu.show((Component) e.getSource(), 0, 0);
	}
	
	public void showPopupMenu(Component component) {
		this.popupMenu.show(component, 0, 0);
	}
	
}
