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
package com.leclercb.taskunifier.gui.components.configuration.api;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXColorSelectionButton;

import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public interface ConfigurationFieldType<ComponentType extends JComponent, ValueType> {
	
	public static class Panel implements ConfigurationFieldType<JPanel, Void> {
		
		private JPanel panel;
		
		public Panel(JPanel panel) {
			this.panel = panel;
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JPanel getFieldComponent() {
			return this.panel;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
	}
	
	public static class Separator extends JSeparator implements ConfigurationFieldType<JSeparator, Void> {
		
		public Separator() {

		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JSeparator getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
	}
	
	public static class Label extends JLabel implements ConfigurationFieldType<JLabel, Void> {
		
		public Label(String label) {
			super(label);
			this.setEnabled(false);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JLabel getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
	}
	
	public static class Button extends JButton implements ConfigurationFieldType<JButton, Void> {
		
		public Button(Action action) {
			super(action);
		}
		
		public Button(String label, ActionListener listener) {
			super(label);
			this.addActionListener(listener);
		}
		
		public Button(String label, Icon icon, ActionListener listener) {
			super(label, icon);
			this.addActionListener(listener);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JButton getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
	}
	
	public static class CheckBox extends JCheckBox implements ConfigurationFieldType<JCheckBox, Boolean> {
		
		public CheckBox(Boolean selected) {
			this.setSelected(selected);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JCheckBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Boolean getFieldValue() {
			return this.isSelected();
		}
		
	}
	
	public static class Spinner extends JSpinner implements ConfigurationFieldType<JSpinner, Object> {
		
		public Spinner() {

		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JSpinner getFieldComponent() {
			return this;
		}
		
		@Override
		public Object getFieldValue() {
			return this.getValue();
		}
		
	}
	
	public static class StarCheckBox extends JCheckBox implements ConfigurationFieldType<JCheckBox, Boolean> {
		
		public StarCheckBox(Boolean selected) {
			this.setIcon(Images.getResourceImage("checkbox_star.png", 18, 18));
			this.setSelectedIcon(Images.getResourceImage(
					"checkbox_star_selected.png",
					18,
					18));
			
			this.setSelected(selected);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JCheckBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Boolean getFieldValue() {
			return this.isSelected();
		}
		
	}
	
	public static class ComboBox extends JComboBox implements ConfigurationFieldType<JComboBox, Object> {
		
		public ComboBox(ComboBoxModel model, Object selectedItem) {
			super(model);
			this.setSelectedItem(selectedItem);
		}
		
		public ComboBox(Object[] items, Object selectedItem) {
			super(items);
			this.setSelectedItem(selectedItem);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JComboBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Object getFieldValue() {
			return this.getSelectedItem();
		}
		
	}
	
	public static class TextArea extends JTextArea implements ConfigurationFieldType<JTextArea, String> {
		
		public TextArea(String text) {
			super(text, 5, 20);
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JTextArea getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
	}
	
	public static class TextField extends JTextField implements ConfigurationFieldType<JTextField, String> {
		
		public TextField(String text) {
			super(text);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JTextField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
	}
	
	public static class FormattedTextField extends JFormattedTextField implements ConfigurationFieldType<JFormattedTextField, String> {
		
		public FormattedTextField(AbstractFormatter formatter, String text) {
			super(formatter);
			this.setValue(text);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JFormattedTextField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
	}
	
	public static class PasswordField extends JPasswordField implements ConfigurationFieldType<JPasswordField, String> {
		
		public PasswordField(String password) {
			super(password);
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JPasswordField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return new String(this.getPassword());
		}
		
	}
	
	public static class ColorChooser implements ConfigurationFieldType<JXColorSelectionButton, Color> {
		
		private JXColorSelectionButton component;
		
		public ColorChooser(Color color) {
			this.component = new JXColorSelectionButton();
			this.component.setPreferredSize(new Dimension(24, 24));
			this.component.setBorder(BorderFactory.createEmptyBorder());
		}
		
		@Override
		public void initializeFieldComponent() {

		}
		
		@Override
		public JXColorSelectionButton getFieldComponent() {
			return this.component;
		}
		
		@Override
		public Color getFieldValue() {
			return this.component.getBackground();
		}
		
	}
	
	public abstract void initializeFieldComponent();
	
	public abstract ComponentType getFieldComponent();
	
	public abstract ValueType getFieldValue();
	
}
