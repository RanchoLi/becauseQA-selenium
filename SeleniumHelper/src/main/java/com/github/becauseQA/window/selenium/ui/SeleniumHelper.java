package com.github.becauseQA.window.selenium.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.github.becauseQA.window.selenium.action.AboutAction;
import com.github.becauseQA.window.selenium.action.CheckForUpdateAction;
import com.github.becauseQA.window.selenium.action.CloseAction;
import com.github.becauseQA.window.selenium.action.CloseAllAction;
import com.github.becauseQA.window.selenium.action.CopyAction;
import com.github.becauseQA.window.selenium.action.CutAction;
import com.github.becauseQA.window.selenium.action.DailyTipsAction;
import com.github.becauseQA.window.selenium.action.ExitAction;
import com.github.becauseQA.window.selenium.action.HelpAction;
import com.github.becauseQA.window.selenium.action.LookAndFeelAction;
import com.github.becauseQA.window.selenium.action.NewAction;
import com.github.becauseQA.window.selenium.action.OnlineResourcesAction;
import com.github.becauseQA.window.selenium.action.OpenAction;
import com.github.becauseQA.window.selenium.action.PasteAction;
import com.github.becauseQA.window.selenium.action.PreferencesAction;
import com.github.becauseQA.window.selenium.action.ProjectWebsiteAction;
import com.github.becauseQA.window.selenium.action.QuickStartPaneAction;
import com.github.becauseQA.window.selenium.action.RedoAction;
import com.github.becauseQA.window.selenium.action.ReportDefectWebsiteAction;
import com.github.becauseQA.window.selenium.action.SaveAction;
import com.github.becauseQA.window.selenium.action.SaveAllAction;
import com.github.becauseQA.window.selenium.action.SaveAsAction;
import com.github.becauseQA.window.selenium.action.ShowHideStatusBarAction;
import com.github.becauseQA.window.selenium.action.ShowHideToolBarAction;
import com.github.becauseQA.window.selenium.action.UndoAction;
import com.github.becauseQA.window.selenium.preference.ApplicationSettings;
import com.github.becauseQA.window.ui.jframe.JStatusBar;
import com.github.becauseQA.window.ui.jframe.ToolbarStatusbarChangeHandler;
import com.github.becauseQA.window.ui.jmenu.JMenuItemStatusBarChangeHandler;
import com.github.becauseQA.window.ui.jtextpane.Log4jMessageAppender;
import com.github.becauseQA.window.ui.lookandfeels.LookAndFeelUtils;
import com.github.becauseQA.window.ui.systemtray.SystemTrayUtils;
import com.github.becauseQA.window.utils.PlatformUIUtils;
import com.github.becauseQA.window.utils.SplashScreenUtils;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.sun.jna.Platform;

public class SeleniumHelper implements JStatusBar {

	public JFrame frame = new JFrame();
	public static ApplicationSettings applicationSettings = ApplicationSettings.getInstance();
	public static ResourceBundle res = ResourceBundle.getBundle("Messages", Locale.getDefault());

	// public QuickStartPane quickStartPane;
	// Menu Bar
	public JMenuBar menuBar = new JMenuBar();

	public JMenu mnFile;
	public JMenuItem mntmNew;

	public JMenuItem mntmOpen;

	public JMenuItem mntmClose;

	public JMenuItem mntmCloseAll;

	public JMenuItem mntmSave;

	public JMenuItem mntmSaveAs;

	public JMenuItem mntmSaveAll;

	public JMenuItem mntmExit;

	public JMenu mnEdit;

	public JMenuItem mntmUndo;

	public JMenuItem mntmRedo;

	public JMenuItem mntmCut;

	public JMenuItem mntmCopy;

	public JMenuItem mntmPaste;

	public JCheckBoxMenuItem chckbxmntmStatusbar;

	public JCheckBoxMenuItem chckbxmntmToolbar;

	public JMenu mnTools;

	public JMenuItem mntmPreferences;

	public JMenu mnHelp;

	public JMenuItem mntmHelp;

	public JMenuItem mntmTipOfTheDay;

	public JMenuItem mntmQuickStart;

	private JMenu mnOnlineResources;
	private JMenuItem mntmProjectWebsite;
	private JMenuItem mntmReportBugOr;

	public JMenuItem mntmCheckUpdate;

	public JMenuItem mntmAbout;

	// Tool Bar
	public JToolBar toolBar = new JToolBar();
	public JButton jbNew;
	public JButton jbOpen;
	public JButton jbSave;
	public JButton jbSaveAll;
	public JButton jbUndo;
	public JButton jbRedo;
	public JButton jbCut;
	public JButton jbCopy;
	public JButton jbPaste;
	public JButton jbHelp;

	// Status Bar
	public JPanel statusBar = new JPanel();
	public JLabel statusBarContent = new JLabel();
	public JProgressBar statusBarProgressBar = new JProgressBar();

	// Main tabbed panel
	// public DraggedTabbedPane tabbedPane;

	// all actions
	public NewAction newAction = new NewAction(this);
	public OpenAction openAction = new OpenAction(this);

	public CloseAction closeAction = new CloseAction(this);
	public CloseAllAction closeAllAction = new CloseAllAction(this);

	public SaveAction saveAction = new SaveAction(this);
	public SaveAsAction saveAsAction = new SaveAsAction(this);
	public SaveAllAction saveAllAction = new SaveAllAction(this);

	public ExitAction exitAction = new ExitAction(this);

	public UndoAction undoAction = new UndoAction(this);
	public RedoAction redoAction = new RedoAction(this);
	public CutAction cutAction = new CutAction(this);
	public CopyAction copyAction = new CopyAction(this);
	public PasteAction pasteAction = new PasteAction(this);

	public ShowHideToolBarAction showHideToolBarAction = new ShowHideToolBarAction(this);
	public ShowHideStatusBarAction showHideStatusBarAction = new ShowHideStatusBarAction(this);

	public PreferencesAction preferencesAction = new PreferencesAction(this);

	public HelpAction helpAction = new HelpAction(this);
	public DailyTipsAction dailyTipsAction = new DailyTipsAction(this);
	public QuickStartPaneAction quickStartPaneAction = new QuickStartPaneAction(this);
	public ProjectWebsiteAction websiteAction = new ProjectWebsiteAction(this);
	public OnlineResourcesAction OnlineResourcesAction = new OnlineResourcesAction(this);
	public ProjectWebsiteAction ProjectWebsiteAction = new ProjectWebsiteAction(this);
	public ReportDefectWebsiteAction ReportDefectWebsiteAction = new ReportDefectWebsiteAction(this);
	public CheckForUpdateAction checkForUpdateAction = new CheckForUpdateAction(this);
	public AboutAction aboutAction = new AboutAction(this);
	private JMenu mntmLookandFeels;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SeleniumHelper window = new SeleniumHelper();
					window.display();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SeleniumHelper() {
		initialize();
	}

	public void display() {
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		// PlatformUIUtils.loadURL(null);
		if (applicationSettings.isShowTipsOnStartUp())
			dailyTipsAction.showTipOfTheDay();
		if (applicationSettings.isAutoUpdateCheckEnabled())
			checkForUpdateAction.checkAvailableUpdate();
		// Log4jMessageAppender.tieSystemOutAndErrToLog();
		Log4jMessageAppender.tieSystemOutAndErrToLog();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// TODO Auto-generated constructor stub

		createDefaultMenu();
		createDefaultToolBar();
		createMainPane();
		createDefaultStatusBar();
		initFrameSettings();

		updateControls();
	}

	/**
	 * init the application's icons use any icon it suits
	 */
	public void initFrameSettings() {
		// frame.pack();

		LookAndFeelUtils.initLookAndFeel(frame);

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// frame.setAlwaysOnTop(true);
		frame.setAutoRequestFocus(true);
		frame.setSize(1200, 900);

		frame.setTitle(res.getString("Project.name") + " " + res.getString("Project.version"));
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("Project.icon.image"))));

		frame.setIconImages(icons);

		PlatformUIUtils.centerWindow(frame);

		// support system tray

		frame.setExtendedState(JFrame.NORMAL); // 2016.09.05 to make the frame
												// show correct
		SystemTrayUtils.createSystemTray(frame, null);
		// the default state action,
		SystemTrayUtils.addSystemTray();
		frame.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				// TODO Auto-generated method stub
				if (e.getNewState() == JFrame.ICONIFIED) {
					SystemTrayUtils.addSystemTray();
				}
				if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
					SystemTrayUtils.removeSystemTray();
				}
				if (e.getNewState() == JFrame.NORMAL) {
					SystemTrayUtils.removeSystemTray();
				}

			}
		});
		// the default close action
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				exitAction.exitApplication(false);
			}
		});

		SplashScreen splashScreen = SplashScreenUtils.show();
		SplashScreenUtils.updateSplashMessage(splashScreen, res.getString("Project.loadmessage1"));

	}

	/**
	 * Create the main panel
	 */
	public void createMainPane() {
		PaneUtils.addQuickStartPane(this);

	}

	/**
	 * create a default menu bar
	 * 
	 */
	public void createDefaultMenu() {
		mnFile = new JMenu(res.getString("Menu.file.name"));
		PlatformUIUtils.setMnemonic(mnFile, KeyEvent.VK_F);
		menuBar.add(mnFile);

		mntmNew = new JMenuItem(newAction);
		new JMenuItemStatusBarChangeHandler(mntmNew, (String) newAction.getValue(Action.LONG_DESCRIPTION), this);
		mnFile.add(mntmNew);

		mnFile.addSeparator();

		mntmOpen = new JMenuItem(openAction);
		new JMenuItemStatusBarChangeHandler(mntmOpen, (String) openAction.getValue(Action.LONG_DESCRIPTION), this);
		mnFile.add(mntmOpen);

		mnFile.addSeparator();

		// Close menu
		mntmClose = new JMenuItem(closeAction);
		new JMenuItemStatusBarChangeHandler(mntmClose, (String) closeAction.getValue(Action.LONG_DESCRIPTION), this);
		mnFile.add(mntmClose);

		// Close all menu
		mntmCloseAll = new JMenuItem(closeAllAction);
		new JMenuItemStatusBarChangeHandler(mntmCloseAll, (String) closeAllAction.getValue(Action.LONG_DESCRIPTION),
				this);
		mnFile.add(mntmCloseAll);

		mnFile.addSeparator();

		mntmSave = new JMenuItem(saveAction);
		new JMenuItemStatusBarChangeHandler(mntmSave, (String) saveAction.getValue(Action.LONG_DESCRIPTION), this);
		mnFile.add(mntmSave);

		mntmSaveAs = new JMenuItem(saveAsAction);
		new JMenuItemStatusBarChangeHandler(mntmSaveAs, (String) saveAsAction.getValue(Action.LONG_DESCRIPTION), this);
		mnFile.add(mntmSaveAs);

		mntmSaveAll = new JMenuItem(saveAllAction);

		new JMenuItemStatusBarChangeHandler(mntmSaveAll, (String) saveAllAction.getValue(Action.LONG_DESCRIPTION),
				this);
		mnFile.add(mntmSaveAll);

		mnFile.addSeparator();

		mntmExit = new JMenuItem(exitAction);
		new JMenuItemStatusBarChangeHandler(mntmExit, (String) exitAction.getValue(Action.LONG_DESCRIPTION), this);
		mnFile.add(mntmExit);

		mnEdit = new JMenu(res.getString("Menu.edit.name"));
		PlatformUIUtils.setMnemonic(mnEdit, KeyEvent.VK_E);
		menuBar.add(mnEdit);

		mntmUndo = new JMenuItem(undoAction);
		new JMenuItemStatusBarChangeHandler(mntmUndo, (String) undoAction.getValue(Action.LONG_DESCRIPTION), this);
		mnEdit.add(mntmUndo);

		mntmRedo = new JMenuItem(redoAction);
		new JMenuItemStatusBarChangeHandler(mntmRedo, (String) redoAction.getValue(Action.LONG_DESCRIPTION), this);
		mnEdit.add(mntmRedo);

		mnEdit.addSeparator();

		mntmCut = new JMenuItem(cutAction);
		new JMenuItemStatusBarChangeHandler(mntmCut, (String) cutAction.getValue(Action.LONG_DESCRIPTION), this);
		mnEdit.add(mntmCut);

		mntmCopy = new JMenuItem(copyAction);
		new JMenuItemStatusBarChangeHandler(mntmCopy, (String) copyAction.getValue(Action.LONG_DESCRIPTION), this);
		mnEdit.add(mntmCopy);

		mntmPaste = new JMenuItem(pasteAction);
		new JMenuItemStatusBarChangeHandler(mntmPaste, (String) pasteAction.getValue(Action.LONG_DESCRIPTION), this);
		mnEdit.add(mntmPaste);

		JMenu mnView = new JMenu(res.getString("Menu.view.name"));
		PlatformUIUtils.setMnemonic(mnView, KeyEvent.VK_V);
		menuBar.add(mnView);

		chckbxmntmStatusbar = new JCheckBoxMenuItem(showHideStatusBarAction);
		boolean showStatusBar = applicationSettings.isShowStatusBar();
		chckbxmntmStatusbar.setState(showStatusBar);
		new JMenuItemStatusBarChangeHandler(chckbxmntmStatusbar,
				(String) showHideStatusBarAction.getValue(Action.LONG_DESCRIPTION), this);
		mnView.add(chckbxmntmStatusbar);

		chckbxmntmToolbar = new JCheckBoxMenuItem(showHideToolBarAction);
		boolean showToolBar = applicationSettings.isShowToolBar();
		chckbxmntmToolbar.setState(showToolBar);
		new JMenuItemStatusBarChangeHandler(chckbxmntmToolbar,
				(String) showHideToolBarAction.getValue(Action.LONG_DESCRIPTION), this);
		mnView.add(chckbxmntmToolbar);

		mnTools = new JMenu(res.getString("Menu.tools.name"));
		PlatformUIUtils.setMnemonic(mnTools, KeyEvent.VK_T);
		menuBar.add(mnTools);

		mntmLookandFeels = new JMenu(res.getString("Menu.tools.lookandfeels"));
		mntmLookandFeels.setIcon(new ImageIcon(
				Toolkit.getDefaultToolkit().createImage(getClass().getResource(res.getString("LookAndFeels.image")))));
		ButtonGroup lookandfeelsBtns = new ButtonGroup();
		LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();

		for (int i = 0; i < infos.length; i++) {
			LookAndFeelAction lookAndFeelAction = new LookAndFeelAction(this, infos[i]);
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(lookAndFeelAction);
			lookandfeelsBtns.add(item);
			mntmLookandFeels.add(item);
		}
		mnTools.add(mntmLookandFeels);
		mnTools.addSeparator();
		mntmPreferences = new JMenuItem(preferencesAction);
		new JMenuItemStatusBarChangeHandler(mntmPreferences,
				(String) preferencesAction.getValue(Action.LONG_DESCRIPTION), this);
		mnTools.add(mntmPreferences);

		mnHelp = new JMenu(res.getString("Menu.help.name"));
		PlatformUIUtils.setMnemonic(mnHelp, KeyEvent.VK_H);
		menuBar.add(mnHelp);

		mntmHelp = new JMenuItem(helpAction);
		new JMenuItemStatusBarChangeHandler(mntmHelp, (String) helpAction.getValue(Action.LONG_DESCRIPTION), this);
		mnHelp.add(mntmHelp);

		mntmTipOfTheDay = new JMenuItem(dailyTipsAction);
		mntmTipOfTheDay.setToolTipText(null);
		new JMenuItemStatusBarChangeHandler(mntmTipOfTheDay, (String) dailyTipsAction.getValue(Action.LONG_DESCRIPTION),
				this);
		mnHelp.add(mntmTipOfTheDay);

		mntmQuickStart = new JMenuItem(quickStartPaneAction);
		mntmQuickStart.setToolTipText(null);
		new JMenuItemStatusBarChangeHandler(mntmQuickStart,
				(String) quickStartPaneAction.getValue(Action.LONG_DESCRIPTION), this);
		mnHelp.add(mntmQuickStart);

		mnHelp.addSeparator();

		mnOnlineResources = new JMenu(); // $NON-NLS-1$
		mnOnlineResources.setAction(OnlineResourcesAction);

		mntmProjectWebsite = new JMenuItem(ProjectWebsiteAction); // $NON-NLS-1$
		mnOnlineResources.add(mntmProjectWebsite);

		mntmReportBugOr = new JMenuItem(ReportDefectWebsiteAction); // $NON-NLS-1$
		mnOnlineResources.add(mntmReportBugOr);
		mnHelp.add(mnOnlineResources);

		mnHelp.addSeparator();

		mntmCheckUpdate = new JMenuItem(checkForUpdateAction);

		new JMenuItemStatusBarChangeHandler(mntmCheckUpdate,
				(String) checkForUpdateAction.getValue(Action.LONG_DESCRIPTION), this);
		mnHelp.add(mntmCheckUpdate);

		mnHelp.addSeparator();

		mntmAbout = new JMenuItem(aboutAction);
		new JMenuItemStatusBarChangeHandler(mntmAbout, (String) aboutAction.getValue(Action.LONG_DESCRIPTION), this);
		mnHelp.add(mntmAbout);

		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		frame.setJMenuBar(menuBar);

	}

	/**
	 * Create the default toolbar
	 */
	public void createDefaultToolBar() {

		jbNew = new JButton();
		jbNew.setAction(newAction);
		jbNew.setText(null);
		jbNew.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbNew, this, newAction);

		jbOpen = new JButton();
		jbOpen.setAction(openAction);
		jbOpen.setText(null);
		jbOpen.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbOpen, this, openAction);

		jbSave = new JButton();
		jbSave.setAction(saveAction);
		jbSave.setText(null);
		jbSave.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbSave, this, saveAction);

		jbSaveAll = new JButton();
		jbSaveAll.setAction(saveAllAction);
		jbSaveAll.setText(null);
		jbSaveAll.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbSaveAll, this, saveAllAction);

		jbUndo = new JButton();
		jbUndo.setAction(undoAction);
		jbUndo.setText(null);
		jbUndo.setHideActionText(true); // Ensure text is not displayed when
		// changed dynamically
		// later on action
		jbUndo.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbUndo, this, undoAction);

		jbRedo = new JButton();
		jbRedo.setAction(redoAction);
		jbRedo.setText(null);
		jbRedo.setHideActionText(true); // Ensure text is not displayed when
		// changed dynamically
		// later on action
		jbRedo.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbRedo, this, redoAction);

		jbCut = new JButton();
		jbCut.setAction(cutAction);
		jbCut.setText(null);
		jbCut.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbCut, this, cutAction);

		jbCopy = new JButton();
		jbCopy.setAction(copyAction);
		jbCopy.setText(null);
		jbCopy.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbCopy, this, copyAction);

		jbPaste = new JButton();
		jbPaste.setAction(pasteAction);
		jbPaste.setText(null);
		jbPaste.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbPaste, this, pasteAction);

		jbHelp = new JButton();
		jbHelp.setAction(helpAction);
		jbHelp.setText(null);
		jbHelp.setFocusable(false);
		new ToolbarStatusbarChangeHandler(jbHelp, this, helpAction);

		// If using Windows need a bottom line on the toolbar to seperate it
		// from the main view
		if (LookAndFeelUtils.usingWindowsLnf() || LookAndFeelUtils.usingWindowsClassicLnf()) {
			toolBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		}

		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setName("");

		toolBar.add(jbNew);
		toolBar.add(jbOpen);
		toolBar.add(jbSave);
		toolBar.add(jbSaveAll);

		toolBar.addSeparator();

		toolBar.add(jbUndo);
		toolBar.add(jbRedo);

		toolBar.addSeparator();

		toolBar.add(jbCut);
		toolBar.add(jbCopy);
		toolBar.add(jbPaste);

		toolBar.addSeparator();

		toolBar.add(jbHelp);

		toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		boolean showToolBar = applicationSettings.isShowToolBar();
		if (showToolBar) {
			frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		}
	}

	/**
	 * Create default statusbar
	 */
	public void createDefaultStatusBar() {
		int rightPadding = 3;
		if (Platform.isMac()) {
			rightPadding = 15; // Allow extra padding in the grow box status bar
			// if using Mac OS
		}
		statusBarContent.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
				new EmptyBorder(0, 0, 0, rightPadding)));
		setDefaultStatusBarText();

		statusBarProgressBar.setPreferredSize(new Dimension(150, 18));
		statusBarProgressBar.setStringPainted(true);

		statusBar.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
				new EmptyBorder(0, 0, 0, rightPadding)));
		statusBar.setLayout(new BorderLayout(0, 0));
		statusBar.add(statusBarContent, BorderLayout.CENTER);
		if (statusBarProgressBar.getValue() != 0)
			statusBar.add(statusBarProgressBar, BorderLayout.EAST);

		boolean showStatusBar = applicationSettings.isShowStatusBar();
		if (showStatusBar) {
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}

	}

	@Override
	public void setStatusBarText(String status) {
		// TODO Auto-generated method stub
		statusBarContent.setText(status);
	}

	@Override
	public void setDefaultStatusBarText() {
		// TODO Auto-generated method stub
		statusBarContent.setText(res.getString("Statusbar.defaultText"));

	}

	public void updateControls() {
		// disabled some controlls
		boolean findTabbedPane = PaneUtils.findTabbedPane(frame);
		if (!findTabbedPane) {
			newAction.setEnabled(true);
			openAction.setEnabled(true);

			closeAction.setEnabled(false);
			closeAllAction.setEnabled(false);

			saveAction.setEnabled(false);
			saveAsAction.setEnabled(false);
			saveAllAction.setEnabled(false);

			undoAction.setEnabled(false);
			redoAction.setEnabled(false);

			cutAction.setEnabled(false);
			copyAction.setEnabled(false);
			pasteAction.setEnabled(false);

			quickStartPaneAction.setEnabled(true);
		} else {
			newAction.setEnabled(true);
			openAction.setEnabled(true);

			closeAction.setEnabled(true);
			closeAllAction.setEnabled(true);

			saveAction.setEnabled(true);
			saveAsAction.setEnabled(true);
			saveAllAction.setEnabled(true);

			undoAction.setEnabled(false);
			redoAction.setEnabled(false);

			cutAction.setEnabled(true);
			copyAction.setEnabled(true);
			pasteAction.setEnabled(true);

			quickStartPaneAction.setEnabled(true);
		}

	}
}
