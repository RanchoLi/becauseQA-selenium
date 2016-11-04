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
package com.github.becauseQA.window.selenium.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.html.HTMLEditorKit;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.ui.jdialog.JESCDialog;
import com.github.becauseQA.window.utils.CursorUtil;
import com.github.becauseQA.window.utils.PlatformUIUtils;



/**
 * Tip of the Day dialog.
 *
 */
public class DailyTipsDialog extends JESCDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1714239996815977326L;

	private static ResourceBundle res = SeleniumHelper.res;

	private static final String CLOSE_KEY = "CLOSE_KEY";

	private JPanel jpTipOfTheDay;
	private JPanel jpTipMargin;
	private JLabel jlTipMarginBulb;
	private JPanel jpTipBody;
	private JPanel jpTipHeader;
	private JLabel jlTipHeader;
	private JEditorPane jepTip;
	private JScrollPane jspTip;
	private JPanel jpControls;
	private JPanel jpShowTipsOnStartup;
	private JCheckBox jcbShowTipsOnStartup;
	private JPanel jpNavigation;
	private JButton jbPreviousTip;
	private JButton jbNextTip;
	private JButton jbClose;

	private String[] tipsText;
	private int tipIndex;

	/**
	 * Construct tip of the Day dialog
	 *  @param parent
	 *            Parent frame
	 * @param showTipsOnStartup
	 *            Enable show tips on startup checkbox?
	 * @param tips
	 *            Tips resource bundle
	 * @param tipIndex 
	 *            Tips index
	 */
	public DailyTipsDialog(JFrame parent, boolean showTipsOnStartup, ResourceBundle tips, int tipIndex) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);

		this.tipIndex = tipIndex;

		readTips(tips, "DailyTipsAction.TipOfTheDay");

		initComponents(showTipsOnStartup);
	}

	private void initComponents(boolean showTipsOnStartup) {
		jpTipOfTheDay = new JPanel(new BorderLayout());
		jpTipOfTheDay
		.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 5, 10), new LineBorder(Color.LIGHT_GRAY, 1)));

		jpTipMargin = new JPanel(new FlowLayout());
		jpTipMargin.setBackground(Color.LIGHT_GRAY);

		jlTipMarginBulb = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(
				getClass().getResource(res.getString("DailyTipsAction.dialog.image")))));

		jpTipMargin.add(jlTipMarginBulb);

		jpTipBody = new JPanel(new BorderLayout());

		jpTipOfTheDay.add(jpTipMargin, BorderLayout.WEST);
		jpTipOfTheDay.add(jpTipBody, BorderLayout.CENTER);

		jpTipHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpTipHeader.setBackground(Color.WHITE);
		jpTipHeader.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		jlTipHeader = new JLabel(res.getString("DailyTipsAction.jlTipHeader.text"));
		jlTipHeader.setFont(jlTipHeader.getFont().deriveFont(Font.BOLD, 18f));
		jlTipHeader.setBorder(new EmptyBorder(5, 5, 5, 5));

		jpTipHeader.add(jlTipHeader);

		jepTip = new JEditorPane();
		// workaround for rare NPE, see https://community.oracle.com/thread/1478325?start=0&tstart=0
		jepTip.setEditorKit(new HTMLEditorKit());
		jepTip.setContentType("text/html");
		jepTip.setText(getCurrentTip());
		jepTip.setEditable(false);
		jepTip.setBackground(Color.WHITE);
		jepTip.setCaretPosition(0);
		jepTip.setBorder(new EmptyBorder(10, 10, 10, 10));

		jspTip = PlatformUIUtils.createScrollPane(null,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jspTip.setPreferredSize(new Dimension(300, 150));
		jspTip.setBorder(new EmptyBorder(0, 0, 0, 0));

		jspTip.getViewport().add(jepTip);

		jpTipBody.add(jpTipHeader, BorderLayout.NORTH);
		jpTipBody.add(jspTip, BorderLayout.CENTER);

		jpShowTipsOnStartup = new JPanel(new FlowLayout());
		jpShowTipsOnStartup.setBorder(new EmptyBorder(5, 5, 5, 5));

		jcbShowTipsOnStartup = new JCheckBox(res.getString("DailyTipsAction.jcbShowTipsOnStartup.text"), showTipsOnStartup);
		PlatformUIUtils.setMnemonic(jcbShowTipsOnStartup, KeyEvent.VK_N);

		jpShowTipsOnStartup.add(jcbShowTipsOnStartup);

		jpNavigation = new JPanel(new FlowLayout());
		jpNavigation.setBorder(new EmptyBorder(5, 10, 10, 10));

		jbPreviousTip = new JButton(res.getString("DailyTipsAction.jbPreviousTip.text"));
		PlatformUIUtils.setMnemonic(jbPreviousTip, KeyEvent.VK_P);

		jbPreviousTip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CursorUtil.setCursorBusy(DailyTipsDialog.this);
					jepTip.setText(getPreviousTip());
					jepTip.setCaretPosition(0);
				} finally {
					CursorUtil.setCursorFree(DailyTipsDialog.this);
				}
			}
		});

		jbNextTip = new JButton(res.getString("DailyTipsAction.jbNextTip.text"));
		PlatformUIUtils.setMnemonic(jbNextTip,KeyEvent.VK_N);
		jbNextTip.setDefaultCapable(true);

		jbNextTip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CursorUtil.setCursorBusy(DailyTipsDialog.this);
					jepTip.setText(getNextTip());
					jepTip.setCaretPosition(0);
				} finally {
					CursorUtil.setCursorFree(DailyTipsDialog.this);
				}
			}
		});

		jbClose = new JButton(res.getString("DailyTipsAction.jbClose.text"));
		PlatformUIUtils.setMnemonic(jbClose, KeyEvent.VK_E);
		jbClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closePressed();
			}
		});

		jbClose.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				CLOSE_KEY);
		jbClose.getActionMap().put(CLOSE_KEY, new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 307758680783830158L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				closePressed();
			}
		});

		jpNavigation = PlatformUIUtils.createDialogButtonPanel(jbClose, null, new JButton[] { jbPreviousTip, jbNextTip },
				false);
		jpNavigation.setBorder(new EmptyBorder(5, 5, 5, 5));

		jpControls = new JPanel(new BorderLayout());
		jpControls.add(jpShowTipsOnStartup, BorderLayout.WEST);
		jpControls.add(jpNavigation, BorderLayout.EAST);

		setLayout(new BorderLayout());
		add(jpTipOfTheDay, BorderLayout.CENTER);
		add(jpControls, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(jbClose);

		setTitle(res.getString("DailyTipsAction.Title"));

		setResizable(false);

		pack();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jbClose.requestFocus();
			}
		});
	}

	private void readTips(ResourceBundle tips, String tipPrefix) {
		ArrayList<String> tipList = new ArrayList<String>();

		// Look for all properties "<tip prefix>x" where x is a sequential
		// 0-index
		int i = 0;

		while (true) {
			String tip;
			try {
				tip = tips.getString(tipPrefix + i);
			} catch (MissingResourceException ex) {
				break;
			}

			tipList.add(tip);

			i++;
		}

		tipsText = tipList.toArray(new String[tipList.size()]);

		if (tipsText.length == 0) {
			throw new IllegalArgumentException(res.getString("DailyTipsAction.exception.message"));
		}
	}

	private String getNextTip() {
		if (tipIndex + 1 >= tipsText.length) {
			tipIndex = 0;
		} else {
			tipIndex++;
		}

		return tipsText[tipIndex];
	}

	private String getPreviousTip() {
		if (tipIndex - 1 < 0) {
			tipIndex = tipsText.length - 1;
		} else {
			tipIndex--;
		}

		return tipsText[tipIndex];
	}

	private String getCurrentTip() {
		if (tipIndex < 0 || tipIndex >= tipsText.length) {
			tipIndex = 0;
		}

		return tipsText[tipIndex];
	}

	/**
	 * Show tips on startup?
	 *
	 * @return True if so
	 */
	public boolean showTipsOnStartup() {
		return jcbShowTipsOnStartup.isSelected();
	}

	/**
	 * Get the index of the next tip in the sequence.
	 *
	 * @return Tip index
	 */
	public int nextTipIndex() {
		if (tipIndex + 1 >= tipsText.length) {
			return 0;
		}

		return tipIndex + 1;
	}

	private void closePressed() {
		setVisible(false);
		dispose();
	}
}
