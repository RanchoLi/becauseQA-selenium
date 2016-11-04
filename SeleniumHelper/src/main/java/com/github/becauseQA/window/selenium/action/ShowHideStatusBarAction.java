/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2016 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.becauseQA.window.selenium.action;

import java.awt.BorderLayout;
import java.awt.Container;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

/**
 * Action to show/hide the status bar.
 *
 */
public class ShowHideStatusBarAction extends UIAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2305571525673457417L;

	/**
	 * Construct action.
	 *
	 * @param baseFrame
	 *            main ui frame
	 */
	public ShowHideStatusBarAction(SeleniumHelper baseFrame) {
		super(baseFrame);
		putValue(LONG_DESCRIPTION, res.getString("ShowHideStatusBarAction.statusbar"));
		putValue(NAME, res.getString("ShowHideStatusBarAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("ShowHideStatusBarAction.tooltip"));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub

		Container contentPane = mainWindow.frame.getContentPane();
		boolean statusBarShown = false;
		int componentCount = contentPane.getComponentCount();
		for (int i = 0; i < componentCount; i++) {
			if (contentPane.getComponent(i).equals(mainWindow.statusBar)) {
				statusBarShown = true;
				break;
			}
		}

		if (statusBarShown) {
			mainWindow.frame.getContentPane().remove(mainWindow.statusBar);
			applicationSettings.setShowStatusBar(false);
		} else {
			mainWindow.frame.getContentPane().add(mainWindow.statusBar, BorderLayout.SOUTH);
			applicationSettings.setShowStatusBar(true);
		}
		mainWindow.frame.validate();
	}
}
