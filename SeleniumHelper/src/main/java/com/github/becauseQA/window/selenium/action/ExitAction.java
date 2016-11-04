package com.github.becauseQA.window.selenium.action;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.utils.RestartUtils;


public class ExitAction extends SaveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	/**
	 * @param frame base windows ui
	 */
	public ExitAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
		putValue(LONG_DESCRIPTION, res.getString("ExitAction.statusbar"));
		putValue(NAME, res.getString("ExitAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("ExitAction.tooltip"));
		putValue(
				SMALL_ICON,
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(
						getClass().getResource(res.getString("ExitAction.image")))));
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		super.doAction();
		exitApplication(false);
	}
	
	
	/**
	 * Exit the application and optionally restart.
	 *
	 * @param restart
	 *            Restart application if true
	 */
	public void exitApplication(boolean restart) {
		// Will any KeyStores be closed by exit?
	
		// Save dynamic application settings
		//File currentDir = CurrentDirectoryUtils.get();
		//applicationSettings.setCurrentDirectory(currentDir);
		applicationSettings.save();

		if (restart) {
			RestartUtils.restart();
		}

		System.exit(0);
	}
	
}
