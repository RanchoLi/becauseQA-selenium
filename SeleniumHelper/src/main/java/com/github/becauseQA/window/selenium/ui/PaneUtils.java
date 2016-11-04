package com.github.becauseQA.window.selenium.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.github.becauseQA.window.ui.jmenu.JPopupMenuUtils;
import com.github.becauseQA.window.ui.jtab.ClosableTab;

public class PaneUtils {

	private static DraggedTabbedPane tabbedPane;

	/**
	 * @param mainWindow
	 *            the main ui interface object
	 */
	public static void addQuickStartPane(SeleniumHelper mainWindow) {
		JFrame frame = mainWindow.frame;
		Container contentPane = frame.getContentPane();

		// first remove the existing center component
		removeCenterPane(frame);
		QuickStartPane quickStartPane = new QuickStartPane(mainWindow);
		contentPane.add(quickStartPane, BorderLayout.CENTER);

	}

	/**
	 * @param mainWindow
	 *            the main ui interface object
	 */
	public static void addTabbedPane(SeleniumHelper mainWindow, String title, JComponent bodyPane) {

		JFrame frame = mainWindow.frame;
		Container contentPane = frame.getContentPane();

		// first remove the existing center component
		removeCenterPane(frame);
		if (tabbedPane == null)
			tabbedPane = new DraggedTabbedPane(frame);
		tabbedPane.addTab(title, bodyPane);
		ClosableTab closableTab = new ClosableTab(title, mainWindow.closeAction);
		// tabbedPane.setComponentAt(index, component);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, closableTab);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

		JPopupMenuUtils.TabbedPanePopupMenu(tabbedPane, mainWindow, mainWindow.saveAction, mainWindow.saveAllAction,
				mainWindow.pasteAction, mainWindow.closeAction, mainWindow.closeAllAction);

		contentPane.add(tabbedPane, BorderLayout.CENTER);

	}

	/**
	 * @param frame
	 *            the frame object
	 */
	public static Component getCenterPane(JFrame frame) {
		boolean findPane = false;
		Component returnComponent = null;
		Component foundComponent = null;
		Container contentPane = frame.getContentPane();
		int componentCount = contentPane.getComponentCount();
		for (int k = 0; k < componentCount; k++) {
			foundComponent = contentPane.getComponent(k);
			if (foundComponent instanceof QuickStartPane || foundComponent instanceof DraggedTabbedPane) {
				findPane = true;
				break;
			}
		}
		if (findPane) {
			returnComponent = foundComponent;
		}
		return returnComponent;
	}

	/**
	 * @param frame
	 *            the frame object
	 */
	public static boolean findTabbedPane(JFrame frame) {
		boolean findPane = false;
		Component foundComponent = null;
		Container contentPane = frame.getContentPane();
		int componentCount = contentPane.getComponentCount();
		for (int k = 0; k < componentCount; k++) {
			foundComponent = contentPane.getComponent(k);
			if (foundComponent instanceof DraggedTabbedPane) {
				findPane = true;
				break;
			}
		}

		return findPane;
	}

	/**
	 * @param frame
	 *            the frame object
	 */
	private static void removeCenterPane(JFrame frame) {
		Component centerPane = getCenterPane(frame);
		if (centerPane != null)
			frame.remove(centerPane);

	}
}
