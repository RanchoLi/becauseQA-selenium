package com.github.becauseQA.window.selenium.preference;

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


import java.io.File;
import java.util.Properties;

public class CurrentDirectoryUtils {

	public static ApplicationSettings applicationSettings = ApplicationSettings.getInstance();

	static {
		initialiseWorkingDirToBeHomeDir();
	}

	public static void initialiseWorkingDirToBeHomeDir() {
		File currentDirectory = applicationSettings.getCurrentDirectory();
		String homeDir = null;
		if (currentDirectory != null) {
			homeDir = currentDirectory.getAbsolutePath();
		} else {
			homeDir = System.getProperty("user.home");
		}

		if (homeDir != null) {
			System.setProperty("user.dir", homeDir);
		}

	}

	/**
	 * Update CurrentSirectory to be the supplied directory.
	 *
	 * @param directory
	 *            Used to set current directory
	 */
	public static void update(File directory) {
		if (directory.isDirectory()) {
			if (directory != null && directory.exists()) {
				Properties sysProps = new Properties(System.getProperties());
				sysProps.setProperty("user.dir", directory.getAbsolutePath());
				System.setProperties(sysProps);
			}
		} else {
			File parentFolder = directory.getParentFile();
			update(parentFolder);
		}
	}

	/**
	 * Get the current directory.
	 *
	 * @return Current directory
	 */
	public static File get() {
		return new File(System.getProperty("user.dir"));
	}

}
