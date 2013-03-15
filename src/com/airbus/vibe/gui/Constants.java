package com.airbus.vibe.gui;

import org.eclipse.osgi.util.NLS;

public class Constants extends NLS {
	private static final String BUNDLE_NAME = "com.airbus.vibe.gui.constants"; //$NON-NLS-1$
	public static String app_dir;
	public static String plat_dir;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Constants.class);
	}

	private Constants() {
	}
}
