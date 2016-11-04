package com.github.becauseQA.window.selenium.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.github.becauseQA.apache.commons.FileUtils;
import com.github.becauseQA.command.CommandUtils;
import com.github.becauseQA.window.selenium.preference.ApplicationSettings;
import com.github.becauseQA.window.selenium.preference.CurrentDirectoryUtils;
import com.github.becauseQA.window.swingworker.AbstractSwingTasker;
import com.github.becauseQA.window.ui.jdialog.JESCDialog;
import com.github.becauseQA.window.ui.jdialog.JMessageDialogUtils;
import com.github.becauseQA.window.ui.jlabel.JLabelUtils;
import com.github.becauseQA.window.utils.CursorUtil;

import net.miginfocom.swing.MigLayout;

public class UpdateManagerDialog extends JESCDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3922561861869181426L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private boolean cancelled = false;
	private static final String CANCEL_KEY = "CANCEL_KEY";
	private JTextField textField;
	private JPanel headerPanel;
	private JPanel productInfoPanel;
	private JPanel destinationPanel;
	private JLabel lblProgressbar;
	private String latestBuild;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UpdateManagerDialog dialog = new UpdateManagerDialog(null, "test", "1.2333","JiraHelper-1.0.2-20160729-setup.exe");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public UpdateManagerDialog(JFrame parent, String title, String newVersion,String latestBuild) {
		super(parent, title, ModalityType.DOCUMENT_MODAL);
		this.frame=parent;
		this.latestBuild=latestBuild;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBackground(Color.WHITE);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			headerPanel = new JPanel();
			headerPanel.setLayout(new MigLayout("", "[522px]", "[393px]"));
		}
		{
			destinationPanel = new JPanel();
			destinationPanel
					.setBorder(new TitledBorder(null, res.getString("UpdateManagerDialog.installerlocation.title"),
							TitledBorder.LEADING, TitledBorder.TOP, null, null));
			destinationPanel.setLayout(new MigLayout("", "[165px][63px][86px][91px][][][][]", "[23px][][]"));
			{
				JCheckBox chckbxSaveLocation = new JCheckBox(
						res.getString("UpdateManagerDialog.installerlocation.label.location"));
				chckbxSaveLocation.setSelected(true);
				chckbxSaveLocation.setHorizontalAlignment(SwingConstants.LEFT);
				destinationPanel.add(chckbxSaveLocation, "cell 0 0,alignx left,aligny top");
			}
			{
				JLabel lblFileLocation = new JLabel(
						res.getString("UpdateManagerDialog.installerlocation.label.filelocation"));
				destinationPanel.add(lblFileLocation, "cell 0 1,alignx left,aligny center");
			}
			{
				textField = new JTextField();
				String path = CurrentDirectoryUtils.get().getAbsolutePath();
				textField.setText(path);
				destinationPanel.add(textField, "cell 0 2 5 1,growx,aligny center");
				textField.setColumns(10);
			}
		}
		{
			JLabel lblLogo = new JLabel();
			Image image = Toolkit.getDefaultToolkit()
					.createImage(getClass().getResource(res.getString("Project.icon.image")));
			lblLogo.setIcon(new ImageIcon(image));
			lblLogo.setHorizontalAlignment(SwingConstants.RIGHT);
			headerPanel.add(lblLogo, "flowx,cell 0 0");
		}

		{
			JLabel lblNewVersionHeader = new JLabel(res.getString("UpdateManagerDialog.installerlocation.header"));
			lblNewVersionHeader.setVerticalAlignment(SwingConstants.BOTTOM);
			lblNewVersionHeader.setFont(new Font("Tahoma", Font.PLAIN, 22));
			headerPanel.add(lblNewVersionHeader, "cell 0 0");
		}

		String url = res.getString("project.url.revisionhistory");
		String revisionText = res.getString("UpdateManagerDialog.productinfo.label.revisionhistory");
		{
			JButton btnNewButton = new JButton(res.getString("UpdateManagerDialog.installerlocation.browserbtn"));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {

					// get the file location
					JFileChooser fileChooser = new JFileChooser(textField.getText());
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					int returnBtn = fileChooser.showOpenDialog(parent);
					if (returnBtn == JFileChooser.APPROVE_OPTION) {
						String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
						textField.setText(absolutePath);
					}

				}
			});
			destinationPanel.add(btnNewButton, "cell 5 2,alignx left,aligny top");
		}
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.add(headerPanel);
		{
			productInfoPanel = new JPanel();
			productInfoPanel.setBorder(new TitledBorder(null, res.getString("UpdateManagerDialog.productinfo.title"),
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			productInfoPanel.setLayout(new MigLayout("", "[][][]", "[][][][][][][]"));
			{
				JLabel lblName = new JLabel(res.getString("UpdateManagerDialog.productinfo.label.name"));
				productInfoPanel.add(lblName, "cell 0 0");
			}
		}
		{
			JLabel lblProjectName = new JLabel();
			lblProjectName.setText(res.getString("Project.name"));
			productInfoPanel.add(lblProjectName, "cell 1 0 2 1");
		}
		{
			JLabel lblCurrentVersion = new JLabel("Current Version:");
			productInfoPanel.add(lblCurrentVersion, "cell 0 1");
		}
		{
			JLabel lblCurrentVersion = new JLabel("");
			lblCurrentVersion.setText(res.getString("Project.version"));
			productInfoPanel.add(lblCurrentVersion, "cell 1 1 2 1");
		}
		{
			JLabel lblNewVersion = new JLabel("New Version:");
			productInfoPanel.add(lblNewVersion, "cell 0 2");
		}
		{
			JLabel lblNewVersion = new JLabel("");
			lblNewVersion.setText(newVersion);
			productInfoPanel.add(lblNewVersion, "cell 1 2 2 1");
		}
		{
			JLabel lblProductState = new JLabel("Product State:");
			productInfoPanel.add(lblProductState, "cell 0 3");
		}
		{
			JLabel lblRelease = new JLabel();
			lblRelease.setText(res.getString("Project.state"));
			lblRelease.setHorizontalAlignment(SwingConstants.CENTER);
			productInfoPanel.add(lblRelease, "cell 1 3 2 1");
		}
		{
			JLabel lblFileSize = new JLabel("File Size:");
			productInfoPanel.add(lblFileSize, "cell 0 4");
		}
		{
			String kbSize = res.getString("Project.filesize");
			Double size = Double.parseDouble(kbSize) / 1024;
			String fileSize = new DecimalFormat("###.##").format(size);
			JLabel lblSize = new JLabel();
			lblSize.setText(fileSize + " MB");
			productInfoPanel.add(lblSize, "cell 1 4 2 1");
		}

		JLabel lblReleaseNote = new JLabel();
		lblReleaseNote.setText(revisionText);
		CursorUtil.setCursorHand(lblReleaseNote);
		JLabelUtils.openUrl(lblReleaseNote, url);
		productInfoPanel.add(lblReleaseNote, "cell 0 5,alignx left");
		contentPanel.add(productInfoPanel);
		contentPanel.add(destinationPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton(res.getString("UpdateManagerDialog.UpdateButton"));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						// disabled the button
						okButton.setEnabled(false);
						lblProgressbar.setVisible(true);
						try {
							CursorUtil.setCursorBusy(UpdateManagerDialog.this);
							okPressed();
						} finally {
							CursorUtil.setCursorFree(UpdateManagerDialog.this);
						}
						// okButton.setEnabled(true);
					}
				});

				lblProgressbar = new JLabel();
				lblProgressbar.setVisible(false);

				lblProgressbar.setIcon(
						new ImageIcon(getClass().getResource(res.getString("UpdateManagerDialog.loading.image"))));
				buttonPane.add(lblProgressbar);
				// okButton.setActionCommand("OK");
				buttonPane.add(okButton);

			}
			{
				JButton cancelButton = new JButton(res.getString("UpdateManagerDialog.CancelButton"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						cancelPressed();
					}
				});
				cancelButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
						.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CANCEL_KEY);
				cancelButton.getActionMap().put(CANCEL_KEY, new AbstractAction() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -5389007416249536775L;

					@Override
					public void actionPerformed(ActionEvent evt) {
						cancelPressed();
					}
				});
				// cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				cancelPressed(); // No save of settings
			}
		});
		setIconImage(Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource(res.getString("UpdateManagerDialog.image"))));
		setSize(540, 598);
		setLocationRelativeTo(parent);
		getRootPane().setDefaultButton(okButton);
		okButton.requestFocusInWindow();
		//
	}

	public String getInstallLocation() {
		return textField.getText().trim();
	}

	public void closeDialog() {
		setVisible(false);
		dispose();
	}

	private void okPressed() {
		// download the resources
		//ProgressMonitor progressMonitor = new ProgressMonitor(this, "Downloading installer...", "", 0, 100);
		DownloadInstaller downloadInstaller = new DownloadInstaller(frame);
		downloadInstaller.execute();

	}

	private void cancelPressed() {
		cancelled = true;
		closeDialog();
	}

	public boolean isCancelled() {
		return cancelled;
	}

	class DownloadInstaller extends AbstractSwingTasker<Void, String> {

		public DownloadInstaller(JFrame frame) {
			super(frame);
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			setProgress(10);
			okButton.setEnabled(false);
			lblProgressbar.setVisible(true);
			String sourceurl = res.getString("project.url.download");
			boolean isValidUrl = sourceurl.endsWith("/");
			String downloadurl = null;
			if (isValidUrl) {
				downloadurl = sourceurl+latestBuild;
			}else{
				downloadurl = sourceurl+"/"+latestBuild;
			}
			String destinationPath = textField.getText() + File.separator + latestBuild;
			File destinationFolder = new File(destinationPath);
			publish("Begin to download the installer: " + destinationPath);
			setProgress(15);
			try {
				FileUtils.copyURLToFile(new URL(downloadurl), destinationFolder);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				log.error(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e);
			}
			setProgress(100);
			lblProgressbar.setVisible(false);
			String downloadfile = MessageFormat.format(res.getString("UpdateManagerDialog.download.finishmessage"),
					destinationPath);
			int confirm = JMessageDialogUtils.confirm(UpdateManagerDialog.this,
					res.getString("UpdateManagerDialog.download.finishtitle"), downloadfile);
			okButton.setEnabled(true);
			// lblProgressbar.setVisible(false);
			if (confirm == 0) {
				// launch the installer
				String[] commands = { destinationPath };
				CommandUtils.runCommandUsingJavaRuntime(commands, false);
				// new ExitAction(mainWindow).exitApplication(false);
				ApplicationSettings.getInstance().save();
				System.exit(0);
			}

			return super.doInBackground();
		}

		@Override
		protected void process(List<String> chunks) {
			// TODO Auto-generated method stub
			super.process(chunks);
		}

	}
}
