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
package com.leclercb.taskunifier.gui.components.taskfiles;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.taskunifier.api.models.FileGroup.FileItem;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;

public enum TaskFilesColumn {
	
	LINK(String.class, Translations.getString("general.file.link"), true),
	FILE(String.class, Translations.getString("general.file"), true),
	OPEN(Void.class, Translations.getString("general.open"), false);
	
	public static final String PROP_ORDER = "order";
	public static final String PROP_WIDTH = "width";
	public static final String PROP_VISIBLE = "visible";
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private Class<?> type;
	private String label;
	private boolean editable;
	
	private int order;
	private int width;
	private boolean visible;
	
	private TaskFilesColumn(Class<?> type, String label, boolean editable) {
		this.propertyChangeSupport = new PropertyChangeSupport(
				TaskFilesColumn.class);
		
		this.setType(type);
		this.setLabel(label);
		this.setEditable(editable);
		
		this.setOrder(Main.getSettings().getIntegerProperty(
				"taskfilescolumn."
						+ TaskFilesColumn.this.name().toLowerCase()
						+ ".order",
				0));
		
		this.setWidth(Main.getSettings().getIntegerProperty(
				"taskfilescolumn."
						+ TaskFilesColumn.this.name().toLowerCase()
						+ ".width",
				200));
		
		this.setVisible(Main.getSettings().getBooleanProperty(
				"taskfilescolumn."
						+ TaskFilesColumn.this.name().toLowerCase()
						+ ".visible",
				true));
		
		Main.getSettings().addPropertyChangeListener(
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().startsWith(
								"taskfilescolumn."
										+ TaskFilesColumn.this.name().toLowerCase())) {
							if (evt.getNewValue() == null)
								return;
							
							if (evt.getPropertyName().equals(
									"taskfilescolumn."
											+ TaskFilesColumn.this.name().toLowerCase()
											+ ".order"))
								TaskFilesColumn.this.setOrder(Integer.parseInt(evt.getNewValue().toString()));
							
							if (evt.getPropertyName().equals(
									"taskfilescolumn."
											+ TaskFilesColumn.this.name().toLowerCase()
											+ ".width"))
								TaskFilesColumn.this.setWidth(Integer.parseInt(evt.getNewValue().toString()));
							
							if (evt.getPropertyName().equals(
									"taskfilescolumn."
											+ TaskFilesColumn.this.name().toLowerCase()
											+ ".visible"))
								TaskFilesColumn.this.setVisible(Boolean.parseBoolean(evt.getNewValue().toString()));
						}
					}
					
				});
	}
	
	public Class<?> getType() {
		return this.type;
	}
	
	private void setType(Class<?> type) {
		this.type = type;
	}
	
	public int getOrder() {
		return this.order;
	}
	
	public void setOrder(int order) {
		if (order == this.getOrder())
			return;
		
		int oldOrder = this.getOrder();
		this.order = order;
		Main.getSettings().setIntegerProperty(
				"taskfilescolumn." + this.name().toLowerCase() + ".order",
				order);
		this.propertyChangeSupport.firePropertyChange(
				PROP_ORDER,
				oldOrder,
				order);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	private void setLabel(String label) {
		this.label = label;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		if (width == this.getWidth())
			return;
		
		int oldWidth = this.getWidth();
		this.width = width;
		Main.getSettings().setIntegerProperty(
				"taskfilescolumn." + this.name().toLowerCase() + ".width",
				width);
		this.propertyChangeSupport.firePropertyChange(
				PROP_WIDTH,
				oldWidth,
				width);
	}
	
	public boolean isEditable() {
		return this.editable;
	}
	
	private void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		if (visible == this.isVisible())
			return;
		
		boolean oldVisible = this.isVisible();
		this.visible = visible;
		Main.getSettings().setBooleanProperty(
				"taskfilescolumn." + this.name().toLowerCase() + ".visible",
				visible);
		this.propertyChangeSupport.firePropertyChange(
				PROP_VISIBLE,
				oldVisible,
				visible);
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	public Object getProperty(FileItem item) {
		if (item == null)
			return null;
		
		switch (this) {
			case LINK:
				return item.getLink();
			case FILE:
				return item.getFile();
			case OPEN:
				return null;
			default:
				return null;
		}
	}
	
	public void setProperty(FileItem item, Object value) {
		if (item == null)
			return;
		
		switch (this) {
			case LINK:
				item.setLink((String) value);
				break;
			case FILE:
				item.setFile((String) value);
				break;
			case OPEN:
				break;
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
}
