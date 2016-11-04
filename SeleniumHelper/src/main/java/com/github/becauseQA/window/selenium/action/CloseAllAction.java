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
import java.awt.event.InputEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class CloseAllAction extends CloseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CloseAllAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("CloseAllAction.accelerator").charAt(0),
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_MASK));
		putValue(LONG_DESCRIPTION, res.getString("CloseAllAction.statusbar"));
		putValue(NAME, res.getString("CloseAllAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("CloseAllAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource(res.getString("CloseAllAction.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		closeAllTabs();
	}

	public void closeAllTabs() {

	}

}
