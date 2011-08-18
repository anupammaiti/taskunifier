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
package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;

public class PluginConfigurationPanel extends ConfigurationPanel {
	
	private ConfigurationPanel configPanel;
	
	public PluginConfigurationPanel(
			boolean welcome,
			SynchronizerGuiPlugin plugin) {
		this.initialize(welcome, plugin);
	}
	
	private void initialize(boolean welcome, SynchronizerGuiPlugin plugin) {
		this.setLayout(new BorderLayout());
		
		this.configPanel = plugin.getConfigurationPanel(welcome);
		
		String info = plugin.getName()
				+ " - "
				+ plugin.getAuthor()
				+ " - "
				+ plugin.getVersion();
		JLabel pluginInfo = new JLabel(info, SwingConstants.RIGHT);
		pluginInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.add(this.configPanel, BorderLayout.CENTER);
		this.add(pluginInfo, BorderLayout.SOUTH);
	}
	
	@Override
	public void saveAndApplyConfig() {
		this.configPanel.saveAndApplyConfig();
	}
	
	@Override
	public void cancelConfig() {
		this.configPanel.cancelConfig();
	}
	
}
