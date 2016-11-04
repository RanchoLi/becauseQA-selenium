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

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class QuickStartPaneAction extends UIAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5905780711768546770L;

	/**
	 * @param baseFrame
	 *            base frame
	 */
	public QuickStartPaneAction(SeleniumHelper baseFrame) {
		super(baseFrame);
		putValue(LONG_DESCRIPTION, res.getString("QuickStartPaneAction.statusbar"));
		putValue(NAME, res.getString("QuickStartPaneAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("QuickStartPaneAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource(res.getString("QuickStartPaneAction.image")))));
		//putValue(ACTION_COMMAND_KEY, command);
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		showQuickStartPane();

	}

	/**
	 * Display the tip of the day dialog.
	 */
	public void showQuickStartPane() {

		
	}

}
