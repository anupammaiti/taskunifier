package com.leclercb.taskunifier.gui.components.tasks;

import java.awt.HeadlessException;
import java.awt.print.PrinterException;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;

public interface TaskView {
	
	public abstract Task[] getSelectedTasks();
	
	public abstract void setSelectedTasks(Task[] tasks);
	
	public abstract void setTaskSearcher(TaskSearcher searcher);
	
	public abstract void refreshTasks();
	
	public abstract void printTasks() throws HeadlessException,
			PrinterException;
	
	public abstract int getTaskCount();
	
	public abstract int getDisplayedTaskCount();
	
}
