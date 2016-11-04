package com.github.becauseQA.window.selenium.dialog;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.github.becauseQA.window.selenium.preference.ApplicationSettings;
import com.github.becauseQA.window.selenium.ui.SeleniumHelper;
import com.github.becauseQA.window.ui.jdialog.JESCDialog;
import com.github.becauseQA.window.utils.CursorUtil;
import com.github.becauseQA.window.utils.PlatformUIUtils;

import net.miginfocom.swing.MigLayout;

public class PreferencesDialog extends JESCDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6801255947598414455L;
	protected static ResourceBundle res =SeleniumHelper.res;
	ApplicationSettings applicationSettings = ApplicationSettings.getInstance();
	private static final String CANCEL_KEY = "CANCEL_KEY";
	private JPanel dialogButtonPanel;
	private JPanel generalPanel;
	private JTabbedPane tabbedPane;
	private JCheckBox chckbxCheckUpdateOn;

	public PreferencesDialog(JFrame frame) {
		super(frame, res.getString("PreferencesDialog.title"), ModalityType.DOCUMENT_MODAL);
		initComponments();
		setLocationRelativeTo(frame);
		// setVisible(true);
		// pack();
	}

	public void initComponments() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JButton btnSave = new JButton(res.getString("PreferencesDialog.OKText"));

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					CursorUtil.setCursorBusy(PreferencesDialog.this);
					okPressed();
				} finally {
					CursorUtil.setCursorFree(PreferencesDialog.this);
				}
			}
		});
		JButton btnCancel = new JButton(res.getString("PreferencesDialog.CancelText"));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelPressed();
			}
		});
		btnCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				CANCEL_KEY);
		btnCancel.getActionMap().put(CANCEL_KEY, new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5389007416249536775L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelPressed();
			}
		});

		dialogButtonPanel = PlatformUIUtils.createDialogButtonPanel(btnSave, btnCancel, false);
		getContentPane().add(dialogButtonPanel, BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				cancelPressed(); // No save of settings
			}
		});
		// setResizable(false);
		setSize(600, 400); // setSize need to before setLocationRelativeTo
		Image preferenceImage = Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource(res.getString("PreferencesDialog.image")));
		setIconImage(preferenceImage);

		getRootPane().setDefaultButton(btnSave);
		btnSave.requestFocusInWindow();

		initGeneralTab();

	}

	public void initGeneralTab() {

		generalPanel = new JPanel();

		URL generalIcon = getClass().getResource(res.getString("PreferencesDialog.general.image"));
		tabbedPane.addTab(res.getString("PreferencesDialog.general.title"), new ImageIcon(generalIcon), generalPanel,
				res.getString("PreferencesDialog.general.tips"));
		generalPanel.setLayout(new MigLayout("", "[151px]", "[23px][]"));
		
				// the componments
				chckbxCheckUpdateOn = new JCheckBox(res.getString("PreferencesDialog.chckbxCheckUpdateOn.text")); //$NON-NLS-1$
				chckbxCheckUpdateOn.setSelected(applicationSettings.isAutoUpdateCheckEnabled());
				generalPanel.add(chckbxCheckUpdateOn, "cell 0 1,alignx center,aligny center");

	}

	private void okPressed() {
		closeDialog();
	}

	private void cancelPressed() {
		closeDialog();
	}

	public void closeDialog() {
		setVisible(false);
		dispose();
	}

	public boolean isAutoUpdateOnStartup() {
		return chckbxCheckUpdateOn.isSelected();
	}
}
