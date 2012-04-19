package com.leclercb.taskunifier.gui.main.frames;

import java.awt.event.KeyEvent;
import java.io.Serializable;

import com.leclercb.commons.api.utils.CheckUtils;

public class ShortcutKey implements Serializable {
	
	private int keyCode;
	private int modifiers;
	
	public ShortcutKey(int keyCode, int modifiers) {
		CheckUtils.isNotNull(modifiers);
		
		if (modifiers == 0)
			throw new IllegalArgumentException(
					"At least one modifier must be provided");
		
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}
	
	public int getKeyCode() {
		return this.keyCode;
	}
	
	public int getModifiers() {
		return this.modifiers;
	}
	
	public char getKeyChar() {
		char character = KeyEvent.getKeyText(this.keyCode).charAt(0);
		
		if (this.keyCode == KeyEvent.VK_SPACE)
			character = ' ';
		
		return character;
	}
	
}
