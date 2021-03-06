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
package com.leclercb.taskunifier.gui.components.configuration.fields.searcher;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;

import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.api.searchers.coders.NoteSorterXMLCoder;
import com.leclercb.taskunifier.gui.api.searchers.sorters.NoteSorter;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.notesearcheredit.sorter.NoteSorterPanel;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.Main;

public class EditDefaultNoteSorterFieldType extends ConfigurationFieldType.Panel {
	
	private NoteSorter sorter;
	private NoteSorterXMLCoder coder;
	
	public EditDefaultNoteSorterFieldType() {
		this.sorter = Constants.getDefaultNoteSorter();
		this.coder = new NoteSorterXMLCoder();
		this.setPanel(new NoteSorterPanel(this.sorter));
	}
	
	@Override
	public void saveAndApplyConfig() {
		String value = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		try {
			this.coder.encode(output, this.sorter);
			value = new String(output.toByteArray());
		} catch (FactoryCoderException e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while saving default note sorter",
					e);
		}
		
		Main.getSettings().setStringProperty(
				"notesearcher.default_sorter",
				value);
	}
	
}
