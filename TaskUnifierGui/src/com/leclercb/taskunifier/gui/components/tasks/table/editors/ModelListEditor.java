package com.leclercb.taskunifier.gui.components.tasks.table.editors;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.gui.swing.TUModelList;

public class ModelListEditor<M extends Model> extends AbstractCellEditor implements TableCellEditor {
	
	private TUModelList<M> modelList;
	
	public ModelListEditor(ModelType modelType) {
		this.modelList = new TUModelList<M>(modelType);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column) {
		this.modelList.setModelList((ModelList<M>) value);
		return this.modelList;
	}
	
	@Override
	public Object getCellEditorValue() {
		return this.modelList.getModelList();
	}
	
}
