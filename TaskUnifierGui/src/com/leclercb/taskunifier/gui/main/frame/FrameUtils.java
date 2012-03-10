package com.leclercb.taskunifier.gui.main.frame;

import com.leclercb.taskunifier.gui.components.views.ViewList;

public final class FrameUtils {
	
	private FrameUtils() {
		
	}
	
	public static FrameView getCurrentFrameView() {
		int frameId = ViewList.getInstance().getCurrentView().getFrameId();
		return getFrameView(frameId);
	}
	
	public static FrameView getFrameView(int frameId) {
		if (frameId == MainFrame.getInstance().getFrameId())
			return MainFrame.getInstance();
		
		for (SubFrame subFrame : SubFrame.getSubFrames()) {
			if (frameId == subFrame.getFrameId())
				return subFrame;
		}
		
		return null;
	}
	
}
