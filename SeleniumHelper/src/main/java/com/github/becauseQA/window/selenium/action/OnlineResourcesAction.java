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

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class OnlineResourcesAction extends UIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5191152982201052402L;

	public OnlineResourcesAction(SeleniumHelper frame) {
		super(frame);
		// TODO Auto-generated constructor stub
		putValue(LONG_DESCRIPTION, res.getString("OnlineResourcesAction.statusbar"));
		putValue(NAME, res.getString("OnlineResourcesAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("OnlineResourcesAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("OnlineResourcesAction.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}

	

}
