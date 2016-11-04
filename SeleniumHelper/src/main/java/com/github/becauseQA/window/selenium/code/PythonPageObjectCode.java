package com.github.becauseQA.window.selenium.code;

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


import java.util.List;
import java.util.Map;

public class PythonPageObjectCode implements PageObjectCode {

	@Override
	public String pageObjectFileTemplate(List<Map<String, String>> childNodes, String savedFileName) {
		// TODO Auto-generated method stub
		String commentline="\"\"\"\n Generated code from Selenium Recorder Tool,write your code below \n\"\"\"\n";
		String importPackages="import os\n\n";
		String classContent = "class " + savedFileName
				+ "(object):\n\n\t\tdef __init__(self, driver):\n\t\t\t# init function";
		String mainFunction = "if __name__ == '__main__':\n\t\t\t#Put your main function here";
		return commentline+importPackages+classContent+ "\n\n\n\n\n" + mainFunction;
	}

}
