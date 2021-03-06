package com.moesol.url;

import java.lang.instrument.Instrumentation;

public class MscapiHookingAgent {
	public static boolean DEBUG = false;
	public static String CONTEXT = "TLS";

	public static void premain(String args, Instrumentation inst) throws Exception {
		System.out.println("MSCAPI Agent hooking SSL with args: " + args);
		if (args != null) {
			processArgs(args);
		}
		Config config = Config.loadFromUserHome();
		maybeSetTrustSystemProperties(config);
		SwingSelectorKeyManager.configureSwingKeyManagerAsDefault();
	}

	private static void maybeSetTrustSystemProperties(Config config) {
		if (config.isUseWindowsTrust()) {
			System.setProperty("javax.net.ssl.trustStoreType", "Windows-ROOT");
			System.setProperty("javax.net.ssl.trustStore", "NONE");
		}
	}

	static void processArgs(String args) {
		String[] split = args.split(",\\s*");
		for (String s : split) {
			if (s.equals("debug")) {
				DEBUG = true;
			}
			if (s.startsWith("context=")) {
				CONTEXT = s.replace("context=", "");
			}
			if (s.equals("help")) {
				showHelp();
			}
		}
	}

	private static void showHelp() {
		System.out.println("Options: debug | context={c} | help");
		System.out.println("{c} -- SSL, SSLv2, SSLv3, TLS, TLSv1, TLSv1.1, TLSv1.2"); 
	}
}
