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
package com.leclercb.taskunifier.gui.components.taskfiles.table;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;

import javax.swing.table.TableColumn;

import org.jdesktop.swingx.table.DefaultTableColumnModelExt;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.gui.components.taskfiles.TaskFilesColumn;

public class TaskFilesTableColumnModel extends DefaultTableColumnModelExt {
	
	public TaskFilesTableColumnModel() {
		this.initialize();
	}
	
	private void initialize() {
		TaskFilesColumn[] columns = TaskFilesColumn.values();
		Arrays.sort(columns, new Comparator<TaskFilesColumn>() {
			
			@Override
			public int compare(TaskFilesColumn c1, TaskFilesColumn c2) {
				return CompareUtils.compare(c1.getOrder(), c2.getOrder());
			}
			
		});
		
		for (int i = 0; i < columns.length; i++)
			this.addColumn(new TaskFilesTableColumn(columns[i]));
	}
	
	public TaskFilesColumn getTaskFilesColumn(int col) {
		return (TaskFilesColumn) this.getColumn(col).getIdentifier();
	}
	
	@Override
	public void moveColumn(int columnIndex, int newIndex) {
		super.moveColumn(columnIndex, newIndex);
		
		int i = 1;
		Enumeration<TableColumn> columns = this.getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			((TaskFilesColumn) column.getIdentifier()).setOrder(i++);
		}
	}
	
}
