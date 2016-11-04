package com.github.becauseQA.window.selenium.preference;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JTabbedPane;

/*
 * put into the registeration in windows: HKEY_USERS\S-1-5-21-515967899-2049760794-1417001333-22929\Software\JavaSoft\Prefs
 */

public class ApplicationSettings {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static ApplicationSettings applicationSettings;

	private static final String PREFS_NODE = "/com/github/becausetesting";
	private static final String PREFS_KEY_SHOWSTATUSBAR = "showstatusbar";
	private static final String PREFS_KEY_SHOWTOOLBAR = "showtoolbar";

	private static final String PREFS_KEY_TABLAYOUT = "tablayout";

	private static final int RECENTFILES_SIZE = 6;
	private static final String PREFS_KEY_RECENTFILES = "recentfile";
	private static final String PREFS_KEY_CURRENTDIR = "currentdirectory";

	private static final String PREFS_KEY_LICENSE_AGREED = "licenseagreed";

	private static final String PREFS_KEY_SHOWTIPS = "showtipsonstartup";
	private static final String PREFS_KEY_NEXTTIP_INDEX = "nexttipindex";

	private static final String PREFS_KEY_DEFAULTDN = "defaultdn";
	private static final String PREFS_KEY_SSLHOSTS = "sslhosts";
	private static final String PREFS_KEY_SSLPORTS = "sslports";

	private static final String PREFS_KEY_AUTOUPDATE_ENABLED = "autoupdatecheckenabled";
	private static final String PREFS_KEY_AUTOUPDATE_LASTCHECK = "autoupdatechecklastcheck";
	private static final String PREFS_KEY_AUTOUPDATE_INTERVAL = "autoupdatecheckinterval";

	private boolean showToolBar;
	private boolean showStatusBar;

	private int tabLayout;

	private File[] recentFiles;
	private File currentDirectory;

	private boolean licenseAgreed;

	private boolean showTipsOnStartUp;
	private int nextTipIndex;

	private String defaultDN;
	private String sslHosts;
	private String sslPorts;

	private boolean autoUpdateCheckEnabled;
	private Date autoUpdateCheckLastCheck;
	private int autoUpdateCheckInterval;

	public ApplicationSettings() {
		// TODO Auto-generated constructor stub
		load();

	}

	/**
	 * Get singleton instance of application settings. If first call the
	 * application settings are loaded.
	 *
	 * @return Application settings
	 */
	public static synchronized ApplicationSettings getInstance() {
		if (applicationSettings == null) {
			applicationSettings = new ApplicationSettings();
		}

		return applicationSettings;
	}

	public void load() {
		Preferences preferences = getUserRootPreferences();
		showToolBar = preferences.getBoolean(PREFS_KEY_SHOWTOOLBAR, true);
		showStatusBar = preferences.getBoolean(PREFS_KEY_SHOWSTATUSBAR, true);

		tabLayout = preferences.getInt(PREFS_KEY_TABLAYOUT, JTabbedPane.WRAP_TAB_LAYOUT);

		recentFiles = getRecentFiles(preferences);
		// Current directory
		String currentDirectoryStr = preferences.get(PREFS_KEY_CURRENTDIR, null);
		if (currentDirectoryStr != null) {
			try {
				currentDirectory = new File(currentDirectoryStr).getCanonicalFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		licenseAgreed = preferences.getBoolean(PREFS_KEY_LICENSE_AGREED, false);

		// Tip of the day
		showTipsOnStartUp = preferences.getBoolean(PREFS_KEY_SHOWTIPS, true);
		nextTipIndex = preferences.getInt(PREFS_KEY_NEXTTIP_INDEX, 0);

		// Default distinguished name
		defaultDN = preferences.get(PREFS_KEY_DEFAULTDN, "");

		// SSL host names and ports for "Examine SSL"
		sslHosts = preferences.get(PREFS_KEY_SSLHOSTS, "www.google.com;www.amazon.com");
		sslPorts = preferences.get(PREFS_KEY_SSLPORTS, "443");

		// auto update check
		autoUpdateCheckEnabled = preferences.getBoolean(PREFS_KEY_AUTOUPDATE_ENABLED, true);
		autoUpdateCheckInterval = preferences.getInt(PREFS_KEY_AUTOUPDATE_INTERVAL, 14);
		try {
			autoUpdateCheckLastCheck = DATE_FORMAT
					.parse(preferences.get(PREFS_KEY_AUTOUPDATE_LASTCHECK, DATE_FORMAT.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void save() {

		Preferences preferences = getUserRootPreferences();

		// User interface
		preferences.putBoolean(PREFS_KEY_SHOWTOOLBAR, showToolBar);
		preferences.putBoolean(PREFS_KEY_SHOWSTATUSBAR, showStatusBar);
		preferences.putInt(PREFS_KEY_TABLAYOUT, tabLayout);

		// Recent files
		clearExistingRecentFiles(preferences);
		for (int i = 1; i <= recentFiles.length; i++) {
			preferences.put(PREFS_KEY_RECENTFILES + i, recentFiles[i - 1].toString());
		}

		// Current directory
		String absolutePath = CurrentDirectoryUtils.get().getAbsolutePath();
		preferences.put(PREFS_KEY_CURRENTDIR, absolutePath);

		// Licensing
		preferences.putBoolean(PREFS_KEY_LICENSE_AGREED, licenseAgreed);

		// Tip of the day
		preferences.putBoolean(PREFS_KEY_SHOWTIPS, showTipsOnStartUp);
		preferences.putInt(PREFS_KEY_NEXTTIP_INDEX, nextTipIndex);

		// Default distinguished name
		preferences.put(PREFS_KEY_DEFAULTDN, defaultDN);

		// SSL host names and ports for "Examine SSL"
		preferences.put(PREFS_KEY_SSLHOSTS, getSslHosts());
		preferences.put(PREFS_KEY_SSLPORTS, getSslPorts());

		// auto update check settings
		preferences.putBoolean(PREFS_KEY_AUTOUPDATE_ENABLED, isAutoUpdateCheckEnabled());
		preferences.putInt(PREFS_KEY_AUTOUPDATE_INTERVAL, getAutoUpdateCheckInterval());
		preferences.put(PREFS_KEY_AUTOUPDATE_LASTCHECK, DATE_FORMAT.format(getAutoUpdateCheckLastCheck()));

	}

	private void clearExistingRecentFiles(Preferences preferences) {
		// Clear all existing recent files (new list may be shorter than the
		// existing one)
		for (int i = 1; i <= RECENTFILES_SIZE; i++) {
			String recentFile = preferences.get(PREFS_KEY_RECENTFILES + i, null);

			if (recentFile == null) {
				break;
			} else {
				preferences.remove(PREFS_KEY_RECENTFILES + i);
			}
		}
	}

	/**
	 * @param preferences
	 *            the preference object
	 * @return the file arrays
	 */
	private File[] getRecentFiles(Preferences preferences) {
		// Clear all existing recent files (new list may be shorter than the
		// existing one)
		ArrayList<File> recentFilesList = new ArrayList<File>();
		for (int i = 1; i <= RECENTFILES_SIZE; i++) {
			String recentFile = preferences.get(PREFS_KEY_RECENTFILES + i, null);

			if (recentFile == null) {
				break;
			} else {
				File canonicalPath = null;
				try {
					canonicalPath = new File(recentFile).getCanonicalFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				recentFilesList.add(canonicalPath);
			}
		}
		return recentFilesList.toArray(new File[recentFilesList.size()]);
	}

	/**
	 * @return get the root preference
	 */
	private Preferences getUserRootPreferences() {
		// Get underlying Java preferences
		Preferences preferences = Preferences.userRoot().node(PREFS_NODE);
		return preferences;
	}

	/**
	 * Clear application settings in persistent store.
	 *
	 * @throws BackingStoreException
	 *             If a failure occurred in the backing store
	 */
	public void clear() throws BackingStoreException {
		Preferences preferences = getUserRootPreferences();
		preferences.clear();
	}

	public boolean isShowToolBar() {
		return showToolBar;
	}

	public void setShowToolBar(boolean showToolBar) {
		this.showToolBar = showToolBar;
	}

	public boolean isShowStatusBar() {
		return showStatusBar;
	}

	public void setShowStatusBar(boolean showStatusBar) {
		this.showStatusBar = showStatusBar;
	}

	/**
	 * @return the tabLayout
	 */
	public int getTabLayout() {
		return tabLayout;
	}

	/**
	 * @param tabLayout
	 *            the tabLayout to set
	 */
	public void setTabLayout(int tabLayout) {
		this.tabLayout = tabLayout;
	}

	/**
	 * @return the recentFiles
	 */
	public File[] getRecentFiles() {
		return recentFiles;
	}

	/**
	 * @param recentFiles
	 *            the recentFiles to set
	 */
	public void setRecentFiles(File[] recentFiles) {
		this.recentFiles = recentFiles;
	}

	/**
	 * @return the currentDirectory
	 */
	public File getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * @param currentDirectory
	 *            the currentDirectory to set
	 */
	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	/**
	 * @return the licenseAgreed
	 */
	public boolean isLicenseAgreed() {
		return licenseAgreed;
	}

	/**
	 * @param licenseAgreed
	 *            the licenseAgreed to set
	 */
	public void setLicenseAgreed(boolean licenseAgreed) {
		this.licenseAgreed = licenseAgreed;
	}

	/**
	 * @return the showTipsOnStartUp
	 */
	public boolean isShowTipsOnStartUp() {
		return showTipsOnStartUp;
	}

	/**
	 * @param showTipsOnStartUp
	 *            the showTipsOnStartUp to set
	 */
	public void setShowTipsOnStartUp(boolean showTipsOnStartUp) {
		this.showTipsOnStartUp = showTipsOnStartUp;
	}

	/**
	 * @return the nextTipIndex
	 */
	public int getNextTipIndex() {
		return nextTipIndex;
	}

	/**
	 * @param nextTipIndex
	 *            the nextTipIndex to set
	 */
	public void setNextTipIndex(int nextTipIndex) {
		this.nextTipIndex = nextTipIndex;
	}

	/**
	 * @return the defaultDN
	 */
	public String getDefaultDN() {
		return defaultDN;
	}

	/**
	 * @param defaultDN
	 *            the defaultDN to set
	 */
	public void setDefaultDN(String defaultDN) {
		this.defaultDN = defaultDN;
	}

	/**
	 * @return the sslHosts
	 */
	public String getSslHosts() {
		return sslHosts;
	}

	/**
	 * @param sslHosts
	 *            the sslHosts to set
	 */
	public void setSslHosts(String sslHosts) {
		this.sslHosts = sslHosts;
	}

	/**
	 * @return the sslPorts
	 */
	public String getSslPorts() {
		return sslPorts;
	}

	/**
	 * @param sslPorts
	 *            the sslPorts to set
	 */
	public void setSslPorts(String sslPorts) {
		this.sslPorts = sslPorts;
	}

	/**
	 * @return the autoUpdateCheckEnabled
	 */
	public boolean isAutoUpdateCheckEnabled() {
		return autoUpdateCheckEnabled;
	}

	/**
	 * @param autoUpdateCheckEnabled
	 *            the autoUpdateCheckEnabled to set
	 */
	public void setAutoUpdateCheckEnabled(boolean autoUpdateCheckEnabled) {
		this.autoUpdateCheckEnabled = autoUpdateCheckEnabled;
	}

	/**
	 * @return the autoUpdateCheckLastCheck
	 */
	public Date getAutoUpdateCheckLastCheck() {
		return autoUpdateCheckLastCheck;
	}

	/**
	 * @param autoUpdateCheckLastCheck
	 *            the autoUpdateCheckLastCheck to set
	 */
	public void setAutoUpdateCheckLastCheck(Date autoUpdateCheckLastCheck) {
		this.autoUpdateCheckLastCheck = autoUpdateCheckLastCheck;
	}

	/**
	 * @return the autoUpdateCheckInterval
	 */
	public int getAutoUpdateCheckInterval() {
		return autoUpdateCheckInterval;
	}

	/**
	 * @param autoUpdateCheckInterval
	 *            the autoUpdateCheckInterval to set
	 */
	public void setAutoUpdateCheckInterval(int autoUpdateCheckInterval) {
		this.autoUpdateCheckInterval = autoUpdateCheckInterval;
	}

}
