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


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class JavaPageObjectCode implements PageObjectCode {
 
	

	@Override
	public String pageObjectFileTemplate(List<Map<String, String>> childNodes,String savedFileName) {
		// TODO Auto-generated method stub
		String scriptContent = "\t\t//Generate Java code from Selenium Recorder tool \n";
		for (Map<String, String> element : childNodes) {
			String elementName = element.get("Name");
			String elementCss = element.get("CSS");
			String elementId = element.get("Id");
			String elementXpath = element.get("XPath");
			String javaScript = "\t\t@FindAll(value = { @FindBy(className = \"" + elementCss + "\"), @FindBy(xpath = \""
					+ elementXpath + "\"), @FindBy(id = \"" + elementId + "\") })\n" + "\t\tprivate WebElement "
					+ elementName + ";";
			scriptContent += javaScript + "\n\n";

		}
		String templeteCode = 
		"import org.openqa.selenium.WebElement;\n"+
		"import org.openqa.selenium.support.FindAll;\n"+
		"import org.openqa.selenium.support.FindBy;\n"+
		"import org.openqa.selenium.support.PageFactory;\n"+
		"import com.github.becauseQA.cucumber.selenium.BaseSteps;\n"+
		"\n\n"+
		"/**\n"+
		" * @ClassName: "+savedFileName+"\n"+
		" * @Description: TODO\n"+
		" * @author: "+System.getProperty("user.name")+"\n"+
		" * @date: "+LocalDateTime.now().toString()+"\n"+
		" * \n"+
		" */\n"+
		"public class "+savedFileName+" extends BaseSteps {\n"+scriptContent+
		"   \n"+
		"	\tpublic "+savedFileName+"() {\n"+
		"		\t// if current impletmented page is ajax page ,you need a make it as a\n"+
		"		\t// timeout page\n"+
		"		\t// AjaxElementLocatorFactory factory=new\n"+
		"		\t// AjaxElementLocatorFactory(driver, 1000);\n"+
		"		\tPageFactory.initElements(driver, this);\n"+
		"		\t// if current impletmented is for mobile device app or browser like\n"+
		"		\t// android or iphone device .etc\n"+
		"		\t// AppiumFieldDecorator mobiledecorator=new AppiumFieldDecorator(driver,\n"+
		"		\t// new TimeOutDuration(500, TimeUnit.SECONDS))\n"+
		"		\t// PageFactory.initElements(mobiledecorator, this);\n"+
		"	\t}\n"+
		"	\n"+
		"}";
		
		return templeteCode;
	}

}
