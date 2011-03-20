package com.leclercb.taskunifier.gui.commons.comparators;

import java.util.Comparator;

import com.leclercb.commons.api.utils.CompareUtils;
import com.leclercb.taskunifier.api.models.Model;

public class ModelComparator implements Comparator<Model> {
	
	@Override
	public int compare(Model m1, Model m2) {
		String s1 = m1 == null ? null : m1.getTitle().toLowerCase();
		String s2 = m2 == null ? null : m2.getTitle().toLowerCase();
		
		int result = CompareUtils.compare(s1, s2);
		
		if (result == 0)
			result = CompareUtils.compare(m1, m2);
		
		return result;
	}
	
}