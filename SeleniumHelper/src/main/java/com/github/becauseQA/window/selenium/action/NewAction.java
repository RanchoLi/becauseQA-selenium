package com.github.becauseQA.window.selenium.action;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.github.becauseQA.window.selenium.ui.PaneUtils;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.selenium.ui.SeleniumRecorderPanel;

public class NewAction extends UIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NewAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("NewAction.accelerator").charAt(0),
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(LONG_DESCRIPTION, res.getString("NewAction.statusbar"));
		putValue(NAME, res.getString("NewAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("NewAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Toolkit.getDefaultToolkit()
				.createImage(NewAction.class.getResource(res.getString("NewAction.image")))));
		putValue(SELECTED_KEY, false);
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SeleniumRecorderPanel seleniumRecorderPanel=new SeleniumRecorderPanel();
				PaneUtils.addTabbedPane(mainWindow, "Selenium Recorder", seleniumRecorderPanel);
				
				mainWindow.frame.repaint();
				mainWindow.frame.validate();
			}
		});
		
	}

}
