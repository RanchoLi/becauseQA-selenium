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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.ui.jdialog.JESCDialog;
import com.github.becauseQA.window.ui.jdialog.JMessageDialogUtils;
import com.github.becauseQA.window.ui.lookandfeels.LookAndFeelUtils;
import com.github.becauseQA.window.ui.ticker.JTicker;

import net.miginfocom.swing.MigLayout;

/**
 * An About dialog which displays an image of about information, a ticker for
 * acknowledgements and a button to access system information.
 *
 */
public class AboutDialog extends JESCDialog {
	private static final long serialVersionUID = 1L;

	private static ResourceBundle res = SeleniumHelper.res;

	private static final Color background_color = LookAndFeelUtils.isDarkLnf() ? new Color(116, 131, 141)
			: new Color(0, 134, 201);

	private JPanel jpAbout = new JPanel();

	private JLabel lbProjectName = new JLabel(res.getString("Project.name"));
	private JLabel jlIcon;
	private JLabel jlVersion;
	private JLabel jlLicense;

	private JTicker jtkDetails;
	private JButton jbOK;
	private JButton jbCredits;

	/**
	 * Create the about dialog
	 * 
	 * @param parent
	 *            base frame object
	 * @param title
	 *            the dialog title
	 * @param image
	 *            the dialog image icon
	 * @param licenseNotice
	 *            the license notice content
	 * @param tickerItems
	 *            the tick items
	 */
	public AboutDialog(JFrame parent, String title, Image image, String licenseNotice, Object[] tickerItems) {
		super(parent, title, ModalityType.DOCUMENT_MODAL);
		initComponents(image, licenseNotice, tickerItems);
		setLocationRelativeTo(parent);
		// setVisible(true);
	}

	private void initComponents(Image image, String licenseNotice, Object[] tickerItems) {

		// init ticker
		jtkDetails = new JTicker();
		jtkDetails.setIncrement(1);
		jtkDetails.setGap(40);
		jtkDetails.setInterval(20);

		for (int i = 0; i < tickerItems.length; i++) {
			jtkDetails.addItem(tickerItems[i]);
		}

		// prepare other elements
		lbProjectName = new JLabel(res.getString("Project.name"));
		lbProjectName.setFont(lbProjectName.getFont().deriveFont(20f));
		lbProjectName.setForeground(background_color);

		jlIcon = new JLabel(new ImageIcon(image));

		jlVersion = new JLabel("Version " + res.getString("Project.version"));
		jlLicense = new JLabel(licenseNotice);

		jbOK = new JButton(res.getString("AboutDialog.OKtext"));
		jbOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				okPressed();
			}
		});

		jbCredits = new JButton(res.getString("AboutDialog.Creditstext"));
		// PlatformUIUtils.setMnemonic(jbCredits,
		// res.getString("DAbout.jbCredits.mnemonic").charAt(0));
		jbCredits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				openCreditsPageInBrowser();
			}
		});

		// layout
		jpAbout.setLayout(new MigLayout("insets dialog, fill"));
		jpAbout.add(lbProjectName, "top");
		jpAbout.add(jlIcon, "top, right, spany 2, wrap unrel");
		jpAbout.add(jlVersion, "top, wrap para");
		jpAbout.add(jlLicense, "span, wrap unrel");
		jpAbout.add(jtkDetails, "width 100%, span, wrap para:push");

		jpAbout.add(jbCredits, "");
		jpAbout.add(jbOK, "tag ok");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				closeDialog();
			}
		});

		getContentPane().add(jpAbout);
		getRootPane().setDefaultButton(jbOK);
		setResizable(false);
		setIconImage(image);
		pack();

		jtkDetails.start();

		// because in windows laf default button follows focus, OK would not be
		// the default button anymore
		jbOK.requestFocusInWindow();
	}

	protected void openCreditsPageInBrowser() {
		try {
			Desktop.getDesktop().browse(URI.create(res.getString("Project.url.main")));
		} catch (IOException ex) {
			JMessageDialogUtils.error(this, res.getString("Project.name"), MessageFormat.format(
					res.getString("OnlineResourcesAction.NoLaunchBrowser.message"), res.getString("Project.url.main")));
		}
	}

	private void okPressed() {
		closeDialog();
	}

	public void closeDialog() {
		setVisible(false);
		jtkDetails.stop();
		dispose();
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {

					Object[] tickerItems = { "Copyright 2004 -2013 Wayne Grant, 2013 - 2016 Kai Kramer ..." };
					javax.swing.JFrame frame = new javax.swing.JFrame();
					// URL kseIconUrl =
					// AboutDialog.class.getResource("images/about.png");
					Image createImage = Toolkit.getDefaultToolkit().createImage(res.getString("Project.icon.image"));
					AboutDialog dialog = new AboutDialog(frame, "About", createImage,
							"See help for details of the end user license agreement.", tickerItems);
					dialog.setLocationRelativeTo(frame);
					dialog.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent e) {
							System.exit(0);
						}

						@Override
						public void windowDeactivated(java.awt.event.WindowEvent e) {
							System.exit(0);
						}
					});
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
