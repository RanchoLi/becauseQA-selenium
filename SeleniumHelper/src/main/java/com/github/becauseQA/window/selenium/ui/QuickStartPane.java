/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2016 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.becauseQA.window.selenium.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.github.becauseQA.window.ui.jframe.JQuickStartButton;
import com.github.becauseQA.window.ui.jframe.JQuickStartLabel;
import com.github.becauseQA.window.ui.lookandfeels.LookAndFeelUtils;

/**
 * Quick Start pane. Displays quick start buttons for common start functions of
 * the application. Also a drop target for opening files.
 *
 */
public class QuickStartPane extends JPanel implements DropTargetListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6831200509358512328L;

	private static ResourceBundle res = SeleniumHelper.res;

	private Color color1;
	private Color color2;
	// set dark or light colors (depending on active LaF)
	private static final boolean IS_DARK_LAF = LookAndFeelUtils.isDarkLnf();
	private static final Color GRADIENT_COLOR_1 = IS_DARK_LAF ? new Color(85, 85, 85) : Color.WHITE;
	private static final Color GRADIENT_COLOR_2 = IS_DARK_LAF ? new Color(60, 63, 65) : Color.LIGHT_GRAY;
	private static final Color TEXT_COLOR = IS_DARK_LAF ? new Color(116, 131, 141) : new Color(0, 134, 201);
	private static final Color TEXT_ROLLOVER_COLOR = IS_DARK_LAF ? new Color(141, 141, 124) : new Color(135, 31, 120);

	private SeleniumHelper mainwindow;

	private JPanel quickstartPanel;
	private JQuickStartLabel headerLabel;
	private JQuickStartButton quickStartButton;
	private JPanel jpNonResizeCenterHorizontally;

	private Toolkit toolKit;

	/**
	 * Construct Quick Start pane.
	 *
	 * @param mainwindow
	 *            main ui frame
	 */
	public QuickStartPane(SeleniumHelper mainwindow) {
		color1 = GRADIENT_COLOR_1;
		color2 = GRADIENT_COLOR_2;

		this.mainwindow = mainwindow;

		// Make this pane a drop target and its own listener
		new DropTarget(this, this);

		initComponents();
	}

	private void initComponents() {
		GridBagLayout gbl_quickstartPanel = new GridBagLayout();
		gbl_quickstartPanel.columnWeights = new double[]{0.0, 0.0, 0.0};
		quickstartPanel = new JPanel(gbl_quickstartPanel);
		quickstartPanel.setOpaque(false);
		String projectName = MessageFormat.format(res.getString("QuickStartPane.header"),
				res.getString("Project.name"));
		headerLabel = new JQuickStartLabel(projectName);
		headerLabel.setForeground(Color.BLACK);
		headerLabel.setFont(headerLabel.getFont().deriveFont(20f));
		GridBagConstraints quickstartHeader = new GridBagConstraints();
		quickstartHeader.gridheight = 1;
		quickstartHeader.gridwidth = 3;
		quickstartHeader.gridx = 0;
		quickstartHeader.gridy = 0;
		quickstartHeader.insets = new Insets(0, 0, 20, 0);

		quickstartPanel.add(headerLabel, quickstartHeader);

		toolKit = Toolkit.getDefaultToolkit();

		addQuickStartItem("QuickStartPane.newFile.image", "QuickStartPane.newFile.rollover.image",
				"QuickStartPane.newFile.text", 1, 0, new Insets(0, 0, 10, 10), mainwindow.newAction);
		
		addQuickStartItem("QuickStartPane.openFile.image", "QuickStartPane.openFile.rollover.image",
				"QuickStartPane.openFile.text", 1, 1, new Insets(0, 10, 10, 10), mainwindow.openAction);
		addQuickStartItem("QuickStartPane.help.image", "QuickStartPane.help.rollover.image", "QuickStartPane.help.text",
				1, 2, new Insets(0, 10, 10, 10), mainwindow.helpAction);
		
		
		

		// Put in panel to prevent resize of controls and center them
		// horizontally
		jpNonResizeCenterHorizontally = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jpNonResizeCenterHorizontally.setOpaque(false);
		jpNonResizeCenterHorizontally.add(quickstartPanel);

		// Set pane's layout to center controls vertically
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());
		add(jpNonResizeCenterHorizontally);
	}

	public void addQuickStartItem(String image, String rolloverimage, String text, int columnNumber, int rowNumber,
			Insets location, Action action) {
		// add the components
		Class<? extends QuickStartPane> classLoader =getClass();
		
		ImageIcon helpImage = new ImageIcon(toolKit.createImage(classLoader.getResource(res.getString(image))));
		ImageIcon helpImageRollOver = new ImageIcon(
				toolKit.createImage(classLoader.getResource(res.getString(rolloverimage))));
		quickStartButton = new JQuickStartButton(action, res.getString(text), helpImage, helpImageRollOver, TEXT_COLOR,
				TEXT_ROLLOVER_COLOR);
		quickStartButton.setOpaque(false);

		GridBagConstraints quickstartGrid = new GridBagConstraints();
		quickstartGrid.fill = GridBagConstraints.VERTICAL;
		quickstartGrid.anchor = GridBagConstraints.NORTH;
		quickstartGrid.gridheight = 1;
		quickstartGrid.gridwidth = 1;
		quickstartGrid.gridx = rowNumber;
		quickstartGrid.gridy = columnNumber;
		quickstartGrid.insets = location;

		quickstartPanel.add(quickStartButton, quickstartGrid);

	}

	/**
	 * Paint component with gradient.
	 *
	 * @param g
	 *            The graphics object on which to paint
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
		g2d.setPaint(gradient);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	@Override
	public void drop(DropTargetDropEvent evt) {
		// DroppedFileHandler.drop(evt, kseFrame);
	}

	@Override
	public void dragEnter(DropTargetDragEvent evt) {
	}

	@Override
	public void dragExit(DropTargetEvent evt) {
	}

	@Override
	public void dragOver(DropTargetDragEvent evt) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent evt) {
	}
}
