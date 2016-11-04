package com.github.becauseQA.window.selenium.action;

import java.awt.Toolkit;
import java.awt.event.InputEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class SaveAllAction extends SaveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SaveAllAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("SaveAllAction.accelerator").charAt(0),
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_MASK));
		putValue(LONG_DESCRIPTION, res.getString("SaveAllAction.statusbar"));
		putValue(NAME, res.getString("SaveAllAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("SaveAllAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("SaveAllAction.image")))));
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		super.doAction();
	}

}
