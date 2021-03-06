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
package com.leclercb.taskunifier.gui.components.notesearcheredit;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.components.notesearcheredit.filter.NoteFilterElementPanel;
import com.leclercb.taskunifier.gui.components.notesearcheredit.filter.NoteFilterElementTreeNode;
import com.leclercb.taskunifier.gui.components.notesearcheredit.filter.NoteFilterPanel;
import com.leclercb.taskunifier.gui.components.notesearcheredit.searcher.NoteSearcherPanel;
import com.leclercb.taskunifier.gui.components.notesearcheredit.sorter.NoteSorterPanel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class NoteSearcherEditPanel extends JPanel implements TreeSelectionListener {
	
	private NoteSearcher searcher;
	
	private NoteSearcherPanel searcherPanel;
	private NoteFilterElementPanel elementPanel;
	private NoteSorterPanel sorterPanel;
	private NoteFilterPanel filterPanel;
	
	public NoteSearcherEditPanel(NoteSearcher searcher) {
		this.searcher = searcher;
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.searcherPanel = new NoteSearcherPanel(this.searcher);
		this.elementPanel = new NoteFilterElementPanel();
		this.sorterPanel = new NoteSorterPanel(this.searcher.getSorter());
		
		this.filterPanel = new NoteFilterPanel(this.searcher.getFilter());
		this.filterPanel.getTree().addTreeSelectionListener(this);
		
		this.searcherPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.elementPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.sorterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel filterPanel = new JPanel(new BorderLayout());
		filterPanel.add(this.filterPanel, BorderLayout.CENTER);
		filterPanel.add(this.elementPanel, BorderLayout.SOUTH);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab(
				Translations.getString("searcheredit.tab.general"),
				this.searcherPanel);
		
		tabbedPane.addTab(
				Translations.getString("searcheredit.tab.sorter"),
				this.sorterPanel);
		
		tabbedPane.addTab(
				Translations.getString("searcheredit.tab.filter"),
				filterPanel);
		
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void close() {
		this.elementPanel.saveElement();
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent evt) {
		this.elementPanel.saveElement();
		
		if (this.filterPanel.getTree().getSelectionCount() != 0) {
			TreeNode node = (TreeNode) this.filterPanel.getTree().getLastSelectedPathComponent();
			
			if (node instanceof NoteFilterElementTreeNode) {
				this.elementPanel.setElement(((NoteFilterElementTreeNode) node).getElement());
				return;
			}
		}
		
		this.elementPanel.setElement(null);
	}
	
}
