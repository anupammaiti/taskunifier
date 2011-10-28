package com.leclercb.taskunifier.gui.threads.autosave;

import com.leclercb.taskunifier.gui.actions.ActionSave;

public class AutoSaveThread extends Thread {
	
	public AutoSaveThread() {
		
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(30 * 60 * 1000);
				
				ActionSave.save();
			}
		} catch (InterruptedException e) {
			
		}
	}
	
}