package com.github.becauseQA.window.selenium.action;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.github.becauseQA.window.selenium.dialog.PreferencesDialog;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class PreferencesAction extends ExitAction {

	/**
	 * 
	 */

	private static final long serialVersionUID = -2657788918757212064L;

	/**
	 * @param frame
	 *            main frame
	 */
	public PreferencesAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("PreferencesAction.accelerator").charAt(0),
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(LONG_DESCRIPTION, res.getString("PreferencesAction.statusbar"));
		putValue(NAME, res.getString("PreferencesAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("PreferencesAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource(res.getString("PreferencesAction.image")))));
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		// super.doAction();
		showPreferenceDialog();
	}

	public void showPreferenceDialog() {
		// ApplicationSettings applicationSettings =
		// ApplicationSettings.getInstance();

		PreferencesDialog dPreferences = new PreferencesDialog(mainWindow.frame);
		dPreferences.setVisible(true);

		// save the settings
		applicationSettings.setAutoUpdateCheckEnabled(dPreferences.isAutoUpdateOnStartup());
	}
}
