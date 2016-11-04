package com.github.becauseQA.window.selenium.action;

import java.awt.Image;
import java.awt.Toolkit;
import java.text.MessageFormat;
import java.time.LocalDate;

import javax.swing.ImageIcon;

import com.github.becauseQA.window.selenium.dialog.AboutDialog;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;

public class AboutAction extends UIAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2617884699256905588L;
	

	public AboutAction(SeleniumHelper baseFrame) {
		super(baseFrame);
		// TODO Auto-generated constructor stub
		putValue(LONG_DESCRIPTION, res.getString("AboutAction.statusbar"));
		putValue(NAME, res.getString("AboutAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("AboutAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("AboutAction.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		showAbout();
	}

	/**
	 * Display the about dialog.
	 */
	public void showAbout() {

		String copyRightContent = MessageFormat.format(res.getString("AboutDialog.Copyright"),
				LocalDate.now().getYear());
		String title = MessageFormat.format(res.getString("AboutDialog.title"), res.getString("Project.name"));
		Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("Project.icon.image")));
		String licenseContent = res.getString("AboutDialog.License");
		Object[] tickerItems = { copyRightContent };

		AboutDialog dAbout = new AboutDialog(mainWindow.frame, title, image, licenseContent, tickerItems);
		//dAbout.setLocationRelativeTo(mainWindow.frame);
		dAbout.setVisible(true);
	}
}
