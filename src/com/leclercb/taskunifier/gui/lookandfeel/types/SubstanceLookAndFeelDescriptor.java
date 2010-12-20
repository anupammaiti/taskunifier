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
package com.leclercb.taskunifier.gui.lookandfeel.types;

import java.awt.Window;

import javax.swing.JFrame;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import com.leclercb.taskunifier.gui.lookandfeel.LookAndFeelDescriptor;
import com.leclercb.taskunifier.gui.lookandfeel.exc.LookAndFeelException;

public class SubstanceLookAndFeelDescriptor extends LookAndFeelDescriptor {
	
	public SubstanceLookAndFeelDescriptor(String name, String identifier) {
		super(name, identifier);
	}
	
	@Override
	public void setLookAndFeel(Window window) throws LookAndFeelException {
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			SubstanceLookAndFeel.setSkin(this.getIdentifier());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LookAndFeelException(
					"Error while setting look and feel",
					e);
		}
	}
	
}
