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

public class RubyPageObjectCode implements PageObjectCode {

	
	@Override
	public String pageObjectFileTemplate(List<Map<String, String>> childNodes,String savedFileName) {
		// TODO Auto-generated method stub
		String commentLine="# encoding=utf-8\n# Generated code from Selenium Recorder Tool,write your code below\n";
		String pageObjectName=savedFileName+" = \"test\"";
		return commentLine+pageObjectName;
	}

}
