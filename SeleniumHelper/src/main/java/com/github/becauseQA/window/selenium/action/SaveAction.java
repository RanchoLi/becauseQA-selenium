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

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class SaveAction extends SaveAsAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SaveAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("SaveAction.accelerator").charAt(0), Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(LONG_DESCRIPTION, res.getString("SaveAction.statusbar"));
		putValue(NAME, res.getString("SaveAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("SaveAction.tooltip"));
		putValue(
				SMALL_ICON,
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(
						getClass().getResource(res.getString("SaveAction.image")))));
	}

	

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}

}
