package com.github.becauseQA.window.selenium.action;

import java.awt.Component;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.ImageIcon;

import org.dom4j.Node;

import com.github.becauseQA.http.HttpsCert;
import com.github.becauseQA.window.selenium.dialog.UpdateManagerDialog;
import com.github.becauseQA.window.selenium.preference.CurrentDirectoryUtils;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.swingworker.AbstractSwingTasker;
import com.github.becauseQA.window.ui.jdialog.JMessageDialogUtils;
import com.github.becauseQA.xml.XMLUtils;

public class CheckForUpdateAction extends UIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5191152982201052402L;

	public CheckForUpdateAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(LONG_DESCRIPTION, res.getString("CheckForUpdateAction.statusbar"));
		putValue(NAME, res.getString("CheckForUpdateAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("CheckForUpdateAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource(res.getString("CheckForUpdateAction.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		checkAvailableUpdate();
	}

	public void checkAvailableUpdate() {
		CheckVersionTask checkVersion = new CheckVersionTask(mainWindow.frame);
		checkVersion.execute();
	}

	/**
	 * Compares two version strings.
	 * 
	 * Use this instead of String.compareTo() for a non-lexicographical
	 * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
	 * 
	 *  It does not work if "1.10" is supposed to be equal to "1.10.0".
	 * 
	 * @param newVersion
	 *            a string of ordinal numbers separated by decimal points.
	 * @param oldVersion
	 *            a string of ordinal numbers separated by decimal points.
	 * @return The result is a negative integer if str1 is _numerically_ less
	 *         than str2. The result is a positive integer if str1 is
	 *         _numerically_ greater than str2. The result is zero if the
	 *         strings are _numerically_ equal.
	 */
	public static int versionCompare(String newVersion, String oldVersion) {
		String[] vals1 = newVersion.split("\\.");
		String[] vals2 = oldVersion.split("\\.");
		int i = 0;
		// set index to first non-equal ordinal or length of shortest version
		// string
		while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
			i++;
		}
		// compare first non-equal ordinal number
		if (i < vals1.length && i < vals2.length) {
			int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}
		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		return Integer.signum(vals1.length - vals2.length);
	}

	class CheckVersionTask extends AbstractSwingTasker<Void, String> {

		public CheckVersionTask(Component component) {
			super(component);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			URL versionXml;
			String versionMessage;
			log.info(res.getString("UpdateManagerDialog.progressmessage"));
			mainWindow.setStatusBarText(res.getString("UpdateManagerDialog.progressmessage"));
			try {
				HttpsCert.ignoreCert();
				versionXml = new URL(res.getString("project.url.version"));
				XMLUtils.read(versionXml);
				Node latestVersionNode = XMLUtils.getXPathNode("//metadata/versioning/latest");
				String latestVersion = latestVersionNode.getText().trim();
				String currentVersion = res.getString("Project.version").trim();
				log.info("Found latest version is " + latestVersion);
				setProgress(100);
				int compareVersions = versionCompare(latestVersion, currentVersion);
				if (compareVersions > 0) {
					versionMessage = MessageFormat
							.format(res.getString("UpdateManagerDialog.progressmessage.newversion"), latestVersion);
					mainWindow.setStatusBarText(versionMessage);
					log.info(versionMessage);
					String latestBuild = XMLUtils.getXPathNode("//metadata/versioning/latestBuild").getText().trim();
					UpdateManagerDialog updateManagerDialog = new UpdateManagerDialog(mainWindow.frame,
							res.getString("UpdateManagerDialog.title"), latestVersion,latestBuild);

					updateManagerDialog.setVisible(true);
					CurrentDirectoryUtils.update(new File(updateManagerDialog.getInstallLocation()));

					//
				} else {
					versionMessage = MessageFormat.format(res.getString("UpdateManagerDialog.progressmessage.noupdate"),
							currentVersion);
					JMessageDialogUtils.info(mainWindow.frame, res.getString("UpdateManagerDialog.title"),
							versionMessage);
					mainWindow.setStatusBarText(versionMessage);
					log.info(versionMessage);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
			return super.doInBackground();
		}

	}
}
