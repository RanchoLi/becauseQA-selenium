package com.github.becauseQA.window.selenium.action;

import java.awt.event.ActionEvent;

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

/*-
 * #%L
 * commons-window
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 Alter Hu
 * %%
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

import java.util.ResourceBundle;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import com.github.becauseQA.window.selenium.preference.ApplicationSettings;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public abstract class UIAction extends AbstractAction {

	private static final long serialVersionUID = 2149492622498782720L;
	protected static ResourceBundle res = SeleniumHelper.res;
	protected static final Logger log = Logger.getLogger(UIAction.class);

	protected ApplicationSettings applicationSettings = ApplicationSettings.getInstance();
	public SeleniumHelper mainWindow;

	public UIAction(SeleniumHelper mainWindow) {
		// TODO Auto-generated constructor stub
		this.mainWindow = mainWindow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		doAction();

	}

	public abstract void doAction();
}
