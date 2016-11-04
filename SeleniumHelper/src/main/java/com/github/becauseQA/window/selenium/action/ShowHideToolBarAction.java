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
 * Action to show/hide the tool bar.
 *
 */
public class ShowHideToolBarAction extends UIAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6036699944767612044L;

	/**
	 * Construct action.
	 *
	 * @param baseFrame
	 *            base frame
	 */
	public ShowHideToolBarAction(SeleniumHelper baseFrame) {
		super(baseFrame);

		putValue(LONG_DESCRIPTION, res.getString("ShowHideToolBarAction.statusbar"));
		putValue(NAME, res.getString("ShowHideToolBarAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("ShowHideToolBarAction.tooltip"));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub

		Container contentPane = mainWindow.frame.getContentPane();
		boolean toolBarShown = false;
		for (int i = 0; i < contentPane.getComponentCount(); i++) {
			if (contentPane.getComponent(i).equals(mainWindow.toolBar)) {
				toolBarShown = true;
				break;
			}
		}

		if (toolBarShown) {
			//mainWindow.frame.getContentPane().remove(mainWindow.quickStartPane);
			mainWindow.frame.getContentPane().remove(mainWindow.toolBar);
			applicationSettings.setShowToolBar(false);
		} else {
			mainWindow.frame.getContentPane().add(mainWindow.toolBar, BorderLayout.NORTH);
			applicationSettings.setShowToolBar(true);
		}
		mainWindow.frame.validate();
	}
}
