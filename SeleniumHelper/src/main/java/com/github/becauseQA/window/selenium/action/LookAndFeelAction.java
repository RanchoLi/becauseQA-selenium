package com.github.becauseQA.window.selenium.action;

/*-
 * false
 * Selenium Helper
 * sectionDelimiter
 * Copyright (C) 2016 Alter Hu
 * sectionDelimiter
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class LookAndFeelAction extends UIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6508692467007388962L;
	private LookAndFeelInfo lookAndFeelInfo;

	public LookAndFeelAction(SeleniumHelper mainWindow) {
		super(mainWindow);
		// TODO Auto-generated constructor stub
	}

	public LookAndFeelAction(SeleniumHelper mainWindow, LookAndFeelInfo lookAndFeelInfo) {
		super(mainWindow);
		// TODO Auto-generated constructor stub
		this.lookAndFeelInfo = lookAndFeelInfo;
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("LookAndFeels.accelerator").charAt(0), Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(LONG_DESCRIPTION, res.getString("LookAndFeels.statusbar"));
		putValue(NAME, lookAndFeelInfo.getName());
		putValue(SHORT_DESCRIPTION, res.getString("LookAndFeels.tooltip"));
		putValue(
				SMALL_ICON,
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(
						getClass().getResource(res.getString("LookAndFeels.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
			SwingUtilities.updateComponentTreeUI(mainWindow.frame.getRootPane());
		} catch (RuntimeException re) {
			throw re; // FindBugs
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
