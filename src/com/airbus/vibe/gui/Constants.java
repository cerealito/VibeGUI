package com.airbus.vibe.gui;


public class Constants {
	public static final String vibe_d   = ".";
	public static final String app_dir  = vibe_d   + "/APPLICATIONS/A350";
	public static final String dalo_exe = vibe_d   + "/COMMON/DALO/V_CURRENT/dalo.py";
	public static final String ksh_d = vibe_d   + "/EXTERNAL_APPLICATIONS_V2/LAUNCHER/SH";
	public static final String tsar_exe = ksh_d + "/launch_TSARIN_4.ksh";
	public static final String fallback_home = vibe_d + "/TMP";
	public static final String cm_exe_name = "\"D2B_CommunityMgr.exe\"";
	public static final String ping_cmd ="task /FO csv /nh /FI \"IMAGENAME eq D2B_CommunityMgr.exe\"";
	
}
