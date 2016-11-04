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

/*-
 * false
 * commons-window
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
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.github.becauseQA.window.selenium.ui.PaneUtils;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.utils.PlatformUIUtils;

import javafx.embed.swing.JFXPanel;

/**
 * Action to show help.
 *
 */
@SuppressWarnings("restriction")
public class HelpAction extends UIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3524295070365056100L;

	/**
	 * Construct action.
	 *
	 * @param mainWindow
	 *            main frame
	 */
	public HelpAction(SeleniumHelper mainWindow) {
		super(mainWindow);

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		putValue(LONG_DESCRIPTION, res.getString("HelpAction.statusbar"));
		putValue(NAME, res.getString("HelpAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("HelpAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("HelpAction.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		// PlatformUIUtils.openEmail("alterhu2020@gmail.com",
		// "Help%20from%20"+res.getString("project.artifactid"), "Hello,");
		String url = res.getString("Project.url.help");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFXPanel WebBrowserEngine = PlatformUIUtils.createWebBrowserEngine(mainWindow.frame, url,
						mainWindow.statusBarContent, null, mainWindow.statusBarProgressBar);
				PaneUtils.addTabbedPane(mainWindow, url, WebBrowserEngine);
				//mainWindow.frame.validate();
				mainWindow.frame.repaint();
				mainWindow.quickStartPaneAction.setEnabled(false);
			}
		});

	}
}
