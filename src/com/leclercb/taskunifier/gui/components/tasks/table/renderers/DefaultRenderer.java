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
package com.leclercb.taskunifier.gui.components.tasks.table.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.main.Main;

public class DefaultRenderer extends DefaultTableCellRenderer {
	
	private Color even;
	private Color odd;
	private Color selected;
	
	public DefaultRenderer() {
		if (Main.SETTINGS.getBooleanProperty("theme.color.enabled")) {
			this.even = Main.SETTINGS.getColorProperty("theme.color.even");
			this.odd = Main.SETTINGS.getColorProperty("theme.color.odd");
		} else {
			this.even = UIManager.getColor("Table.background");
			this.odd = UIManager.getColor("Table.background");
		}
		
		this.selected = UIManager.getColor("Table.selectionBackground");
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		Component component = super.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				row,
				column);
		
		if (value == null) {
			component.setBackground(this.getBackgroundColor(isSelected, row));
			return component;
		}
		
		Task task = ((TaskTable) table).getTask(row);
		
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>(
				component.getFont().getAttributes());
		attributes.put(TextAttribute.STRIKETHROUGH, task.isCompleted());
		component.setFont(component.getFont().deriveFont(attributes));
		
		component.setBackground(this.getBackgroundColor(isSelected, row));
		
		return component;
	}
	
	private Color getBackgroundColor(boolean isSelected, int row) {
		if (isSelected)
			return this.selected;
		
		if (row % 2 == 0)
			return this.even;
		
		return this.odd;
	}
	
}
