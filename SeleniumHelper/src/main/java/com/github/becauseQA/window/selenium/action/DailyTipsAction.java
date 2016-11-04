package com.github.becauseQA.window.selenium.action;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

import com.github.becauseQA.window.selenium.dialog.DailyTipsDialog;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;



public class DailyTipsAction extends UIAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5905780711768546770L;
	private SeleniumHelper baseFrame;

	/**
	 * @param baseFrame base frame
	 */
	public DailyTipsAction(SeleniumHelper baseFrame) {
		super(baseFrame);
		// TODO Auto-generated constructor stub
		this.baseFrame=baseFrame;
		putValue(LONG_DESCRIPTION, res.getString("DailyTipsAction.statusbar"));
		putValue(NAME, res.getString("DailyTipsAction.name"));
		putValue(SHORT_DESCRIPTION, res.getString("DailyTipsAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource(res.getString("DailyTipsAction.image")))));
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		showTipOfTheDay();

	}

	/**
	 * Display the tip of the day dialog.
	 */
	public void showTipOfTheDay() {
		DailyTipsDialog dTipOfTheDay = new DailyTipsDialog(baseFrame.frame, applicationSettings.isShowTipsOnStartUp(), res,
				applicationSettings.getNextTipIndex());

		dTipOfTheDay.setLocationRelativeTo(baseFrame.frame);
		dTipOfTheDay.setVisible(true);

		applicationSettings.setShowTipsOnStartUp(dTipOfTheDay.showTipsOnStartup());
		applicationSettings.setNextTipIndex(dTipOfTheDay.nextTipIndex());
	}

}
