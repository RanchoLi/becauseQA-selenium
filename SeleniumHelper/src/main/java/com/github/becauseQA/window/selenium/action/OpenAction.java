package com.github.becauseQA.window.selenium.action;

import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.ui.jdialog.jfilechooser.FileChooserFactory;
import com.github.becauseQA.window.ui.systemtray.SystemTrayUtils;

public class OpenAction extends UIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpenAction(SeleniumHelper frame) {
		super(frame);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("OpenAction.accelerator").charAt(0),
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(LONG_DESCRIPTION, res.getString("OpenAction.statusbar"));
		putValue(NAME, res.getString("OpenAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("OpenAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("OpenAction.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		SystemTrayUtils.displayMessage("test title", "this is a message from someone", MessageType.ERROR);
		SystemTrayUtils.displayMessage("test title", "this is a message from someone", MessageType.NONE);
		

	}

	public void openFileDialog() {
		JFileChooser chooser = FileChooserFactory.getLibFileChooser();
		String userDir = System.getProperty("user.dir");
		File currentDir = new File(userDir);
		chooser.setCurrentDirectory(currentDir);
		chooser.setDialogTitle(res.getString("OpenDialog.title"));
		chooser.setMultiSelectionEnabled(false);

		int rtnValue = chooser.showOpenDialog(mainWindow.frame);
		if (rtnValue == JFileChooser.APPROVE_OPTION) {
			//File openFile = chooser.getSelectedFile();
			// do some thing
			//quickStartFrame.setStatusBarText(openFile.getAbsolutePath());
		}

	}
}
