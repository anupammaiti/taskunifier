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
package com.leclercb.taskunifier.gui.components.synchronize;

import java.util.Calendar;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.progress.DefaultProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public class SynchronizeWorker extends SwingWorker<Void, Void> {
	
	private boolean silent;
	private ProgressMessageListener handler;
	
	public SynchronizeWorker(boolean silent) {
		this(silent, null);
	}
	
	public SynchronizeWorker(boolean silent, ProgressMessageListener handler) {
		this.silent = silent;
		this.handler = handler;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		ProgressMonitor monitor = Constants.PROGRESS_MONITOR;
		Connection connection = null;
		Synchronizer synchronizer = null;
		
		if (this.handler != null)
			monitor.addListChangeListener(this.handler);
		
		try {
			if (!Synchronizing.setSynchronizing(true))
				return null;
			
			monitor.addMessage(new DefaultProgressMessage(
					Translations.getString("synchronizer.set_proxy")));
			
			SynchronizerUtils.initializeProxy();
			
			if (SynchronizerUtils.getPlugin().needsLicense()) {
				monitor.addMessage(new DefaultProgressMessage(
						Translations.getString("synchronizer.checking_license")));
				
				if (!SynchronizerUtils.getPlugin().checkLicense()) {
					monitor.addMessage(new DefaultProgressMessage(
							Translations.getString(
									"synchronizer.wait_no_license",
									Constants.WAIT_NO_LICENSE_TIME)));
					
					monitor.addMessage(new DefaultProgressMessage(
							Translations.getString(
									"general.go_to_serial",
									SynchronizerUtils.getPlugin().getName())));
					
					Thread.sleep(Constants.WAIT_NO_LICENSE_TIME * 1000);
				}
			}
			
			monitor.addMessage(new DefaultProgressMessage(
					Translations.getString(
							"synchronizer.connecting",
							SynchronizerUtils.getPlugin().getSynchronizerApi().getApiName())));
			
			connection = SynchronizerUtils.getPlugin().getSynchronizerApi().getConnection(
					Main.SETTINGS);
			
			connection.loadParameters(Main.SETTINGS);
			connection.connect();
			connection.saveParameters(Main.SETTINGS);
			
			synchronizer = SynchronizerUtils.getPlugin().getSynchronizerApi().getSynchronizer(
					Main.SETTINGS,
					connection);
			
			SynchronizerChoice choice = Main.SETTINGS.getEnumProperty(
					"synchronizer.choice",
					SynchronizerChoice.class);
			
			synchronizer.loadParameters(Main.SETTINGS);
			synchronizer.synchronize(choice, monitor);
			synchronizer.saveParameters(Main.SETTINGS);
			
			connection.disconnect();
			
			Main.SETTINGS.setCalendarProperty(
					"synchronizer.last_synchronization_date",
					Calendar.getInstance());
		} catch (final SynchronizerException e) {
			monitor.addMessage(new DefaultProgressMessage(e.getMessage()));
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					e.printStackTrace();
					
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							e.getMessage(),
							null,
							null,
							(e.isExpected() ? e : null),
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
				}
				
			});
			
			return null;
		} catch (final Throwable t) {
			monitor.addMessage(new DefaultProgressMessage(t.getMessage()));
			
			if (!this.silent) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						t.printStackTrace();
						
						ErrorInfo info = new ErrorInfo(
								Translations.getString("general.error"),
								t.getMessage(),
								null,
								null,
								t,
								null,
								null);
						
						JXErrorPane.showDialog(
								MainFrame.getInstance().getFrame(),
								info);
					}
					
				});
			}
			
			return null;
		} finally {
			SynchronizerUtils.removeProxy();
		}
		
		Thread.sleep(1000);
		
		return null;
	}
	
	@Override
	protected void done() {
		Constants.PROGRESS_MONITOR.clear();
		SynchronizerUtils.removeOldCompletedTasks();
		Synchronizing.setSynchronizing(false);
	}
	
}
