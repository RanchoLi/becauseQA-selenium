package com.github.becauseQA.window.selenium.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.github.becauseQA.apache.commons.StringUtils;
import com.github.becauseQA.command.CommandUtils;
import com.github.becauseQA.cucumber.selenium.BaseSteps;
import com.github.becauseQA.cucumber.selenium.PageRecorder;
import com.github.becauseQA.cucumber.selenium.SeleniumCore;
import com.github.becauseQA.window.selenium.code.JavaPageObjectCode;
import com.github.becauseQA.window.selenium.code.PageObjectCode;
import com.github.becauseQA.window.selenium.code.PythonPageObjectCode;
import com.github.becauseQA.window.selenium.code.RubyPageObjectCode;
import com.github.becauseQA.window.selenium.preference.CurrentDirectoryUtils;
import com.github.becauseQA.window.swingworker.AbstractSwingTasker;
import com.github.becauseQA.window.ui.RSyntaxTextArea.RSyntaxTextAreaUtils;
import com.github.becauseQA.window.ui.jdialog.JMessageDialogUtils;
import com.github.becauseQA.window.ui.jdialog.jfilechooser.FileChooserFactory;
import com.github.becauseQA.window.ui.jtree.CustomMutableTreeNode;
import com.github.becauseQA.window.ui.jtree.JTreeUtils;
import com.github.becauseQA.window.ui.jtree.JTreeUtils.EditorTreeCellEditor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class SeleniumRecorderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1357455456922079199L;
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SeleniumRecorderPanel.class);
	private String startUrl = "https://www.attheregister.com/moneypak";
	private JTextField txtHttplocalhostwdhub;
	private JTextField textFieldID;
	private JTextField textFieldXPath;
	private JTextField textFieldCSS;
	private JTextField textFieldName;
	public BaseSteps baseSteps;
	private JButton highlightBtn3;
	private JButton highlightBtn1;
	private JButton highlightBtn2;
	private JButton startBtn;
	private JTree pageobjectsTree;
	private JTabbedPane tabbedPane;
	private EditorTreeCellEditor editorTreeCellEditor;
	private static List<Map<String, String>> childNodes = new ArrayList<>();
	private String currentSelectionName;
	private ButtonGroup buttonGroup;
	// private RSyntaxTextArea scriptAreaContent;
	private JComboBox scriptComboBox;
	String rootDir = System.getProperty("user.dir");
	// private static String seleniumServerStandalone;
	// private List<File> jarList = new ArrayList<File>();
	protected PageObjectCode pageObjectCode;
	private static JFileChooser chooser;
	private static String SaveFileName = "SamplePageStep";
	private JTextField textFieldContent;
	private ImageIcon rootIcon;
	private ImageIcon pageIcon;
	private ImageIcon webElementIcon;

	enum ElementTypes {
		ID, CSS, XPATH, TEXT
	}

	enum ScriptLanguages {
		JAVA(SyntaxConstants.SYNTAX_STYLE_JAVA), RUBY(SyntaxConstants.SYNTAX_STYLE_RUBY), PYTHON(
				SyntaxConstants.SYNTAX_STYLE_PYTHON);

		private String langaugeName;

		private ScriptLanguages(String languageName) {
			// TODO Auto-generated constructor stub
			this.setLangaugeName(languageName);
		}

		public String getLangaugeName() {
			return langaugeName;
		}

		public void setLangaugeName(String langaugeName) {
			this.langaugeName = langaugeName;
		}

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setContentPane(new SeleniumRecorderPanel());
		frame.setSize(1200, 800);
		frame.setVisible(true);
	}

	/**
	 * Create the panel.
	 */
	public SeleniumRecorderPanel() {
		setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("30px"), ColumnSpec.decode("975px:grow"), },
				new RowSpec[] { RowSpec.decode("104px"), FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("615px:grow"), }));

		JPanel topPanel = new JPanel();
		topPanel.setBorder(new TitledBorder(null, "Browser Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(topPanel, "2, 1, fill, top");
		topPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, ColumnSpec.decode("10dlu"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, ColumnSpec.decode("35px"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.LINE_GAP_ROWSPEC, RowSpec.decode("23px:grow"), FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		buttonGroup = new ButtonGroup();
		JCheckBox chckbxChrome = new JCheckBox("Chrome");
		chckbxChrome.setActionCommand("chrome");
		chckbxChrome.setSelected(true);
		chckbxChrome.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean selected = e.getStateChange() == ItemEvent.SELECTED;
				if (selected) {
					DefaultTreeModel treeModel = (DefaultTreeModel) pageobjectsTree.getModel();
					CustomMutableTreeNode rootNode = (CustomMutableTreeNode) treeModel.getRoot();
					rootIcon = new ImageIcon(getClass().getResource("/images/chrome.png"));
					rootNode.setIcon(rootIcon);
					JCheckBox checkBox = (JCheckBox) e.getItem();
					rootNode.setUserObject(checkBox.getText());
					pageobjectsTree.updateUI();
				}

			}
		});

		buttonGroup.add(chckbxChrome);

		topPanel.add(chckbxChrome, "2, 2");
		JCheckBox chckbxFirefox = new JCheckBox("Firefox");
		chckbxFirefox.setActionCommand("firefox");
		chckbxFirefox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean selected = e.getStateChange() == ItemEvent.SELECTED;
				if (selected) {
					DefaultTreeModel treeModel = (DefaultTreeModel) pageobjectsTree.getModel();
					CustomMutableTreeNode rootNode = (CustomMutableTreeNode) treeModel.getRoot();
					rootIcon = new ImageIcon(getClass().getResource("/images/firefox.png"));
					rootNode.setIcon(rootIcon);
					JCheckBox checkBox = (JCheckBox) e.getItem();
					rootNode.setUserObject(checkBox.getText());
					pageobjectsTree.updateUI();
				}

			}
		});
		buttonGroup.add(chckbxFirefox);
		topPanel.add(chckbxFirefox, "5, 2");
		JCheckBox chckbxSafari = new JCheckBox("Safari");
		chckbxSafari.setActionCommand("safari");
		chckbxSafari.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean selected = e.getStateChange() == ItemEvent.SELECTED;
				if (selected) {
					DefaultTreeModel treeModel = (DefaultTreeModel) pageobjectsTree.getModel();
					CustomMutableTreeNode rootNode = (CustomMutableTreeNode) treeModel.getRoot();
					rootIcon = new ImageIcon(getClass().getResource("/images/safari.png"));
					rootNode.setIcon(rootIcon);
					JCheckBox checkBox = (JCheckBox) e.getItem();
					rootNode.setUserObject(checkBox.getText());
					pageobjectsTree.updateUI();
				}

			}
		});
		buttonGroup.add(chckbxSafari);
		topPanel.add(chckbxSafari, "13, 2");
		JCheckBox chckbxIE = new JCheckBox("IE/Edge");
		chckbxIE.setActionCommand("ie");
		chckbxIE.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				boolean selected = e.getStateChange() == ItemEvent.SELECTED;
				if (selected) {
					DefaultTreeModel treeModel = (DefaultTreeModel) pageobjectsTree.getModel();
					CustomMutableTreeNode rootNode = (CustomMutableTreeNode) treeModel.getRoot();
					rootIcon = new ImageIcon(getClass().getResource("/images/ie.png"));
					rootNode.setIcon(rootIcon);
					JCheckBox checkBox = (JCheckBox) e.getItem();
					rootNode.setUserObject(checkBox.getText());
					pageobjectsTree.updateUI();
				}

			}
		});
		buttonGroup.add(chckbxIE);
		topPanel.add(chckbxIE, "17, 2");

		JLabel lblRemoteServerUrl = new JLabel("Start URL:");
		topPanel.add(lblRemoteServerUrl, "2, 4, center, default");

		txtHttplocalhostwdhub = new JTextField();
		txtHttplocalhostwdhub.setText(startUrl);
		// txtHttplocalhostwdhub.setEnabled(false);
		topPanel.add(txtHttplocalhostwdhub, "3, 4, 59, 1, fill, default");
		txtHttplocalhostwdhub.setColumns(3);

		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JButton btnStart = (JButton) event.getSource();
				boolean blStart = btnStart.getText().equals("Start");
				try {
					if (blStart) {
						btnStart.setEnabled(false);
						String actionCommand = buttonGroup.getSelection().getActionCommand();
						String browserName = actionCommand;
						StartSeleniumTask seleniumTask = new StartSeleniumTask(SeleniumRecorderPanel.this, browserName);
						seleniumTask.execute();
					} else {
						btnStart.setEnabled(false);

						String commandOutput = CommandUtils.runArrayCommand(null, "cmd.exe", "/C",
								"netstat.exe -aon |  findstr 0:4444");
						log.info(commandOutput);
						if (!commandOutput.equals("")) {
							if (commandOutput.contains(" ")) {
								String[] commandOutputArray = StringUtils.split(commandOutput, " ");
								String pid = commandOutputArray[commandOutputArray.length - 1].trim();
								String killOutput = CommandUtils.runArrayCommand(null, "cmd.exe", "/C",
										"taskkill.exe /F /PID " + pid);
								log.info(killOutput);
							}
						}
						btnStart.setText("Start");
						btnStart.setEnabled(true);
					}
				} catch (Exception e) {
					// TODO: handle exception
					log.error(e);
				}
			}
		});
		topPanel.add(startBtn, "63, 4");

		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(
				new TitledBorder(null, "Element Checker", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(centerPanel, "2, 3, fill, fill");
		SpringLayout sl_centerPanel = new SpringLayout();
		centerPanel.setLayout(sl_centerPanel);

		JLabel lblName = new JLabel("Name:");
		sl_centerPanel.putConstraint(SpringLayout.NORTH, lblName, 25, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, lblName, 12, SpringLayout.WEST, centerPanel);
		centerPanel.add(lblName);

		textFieldName = new JTextField();
		sl_centerPanel.putConstraint(SpringLayout.NORTH, textFieldName, 22, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, textFieldName, 49, SpringLayout.WEST, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.EAST, textFieldName, 936, SpringLayout.WEST, centerPanel);
		centerPanel.add(textFieldName);
		textFieldName.setColumns(10);

		JLabel lblElementCss = new JLabel("CSS:");
		sl_centerPanel.putConstraint(SpringLayout.NORTH, lblElementCss, 52, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, lblElementCss, 49, SpringLayout.WEST, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.EAST, lblElementCss, 122, SpringLayout.WEST, centerPanel);
		centerPanel.add(lblElementCss);

		highlightBtn3 = new JButton("Highlight");
		sl_centerPanel.putConstraint(SpringLayout.NORTH, highlightBtn3, 48, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, highlightBtn3, 936, SpringLayout.WEST, centerPanel);
		highlightBtn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton highlightbtn = (JButton) e.getSource();
				highlightbtn.setEnabled(false);
				highlightElement(ElementTypes.CSS, textFieldCSS.getText());
				highlightbtn.setEnabled(true);
			}
		});

		textFieldCSS = new JTextField();
		sl_centerPanel.putConstraint(SpringLayout.NORTH, textFieldCSS, 49, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, textFieldCSS, 122, SpringLayout.WEST, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.EAST, textFieldCSS, 936, SpringLayout.WEST, centerPanel);
		centerPanel.add(textFieldCSS);
		textFieldCSS.setColumns(10);
		centerPanel.add(highlightBtn3);

		JLabel lblElementXpath = new JLabel("XPath:");
		sl_centerPanel.putConstraint(SpringLayout.NORTH, lblElementXpath, 81, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, lblElementXpath, 49, SpringLayout.WEST, centerPanel);
		centerPanel.add(lblElementXpath);

		textFieldXPath = new JTextField();
		sl_centerPanel.putConstraint(SpringLayout.NORTH, textFieldXPath, 78, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, textFieldXPath, 122, SpringLayout.WEST, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.EAST, textFieldXPath, 936, SpringLayout.WEST, centerPanel);
		centerPanel.add(textFieldXPath);
		textFieldXPath.setColumns(10);

		highlightBtn1 = new JButton("Highlight");
		sl_centerPanel.putConstraint(SpringLayout.NORTH, highlightBtn1, 77, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, highlightBtn1, 936, SpringLayout.WEST, centerPanel);
		highlightBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton highlightbtn = (JButton) e.getSource();
				highlightbtn.setEnabled(false);
				highlightElement(ElementTypes.XPATH, textFieldXPath.getText());
				highlightbtn.setEnabled(true);
			}
		});
		centerPanel.add(highlightBtn1);

		JLabel lblElementId = new JLabel("ID:");
		sl_centerPanel.putConstraint(SpringLayout.NORTH, lblElementId, 110, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, lblElementId, 49, SpringLayout.WEST, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.EAST, lblElementId, 122, SpringLayout.WEST, centerPanel);
		centerPanel.add(lblElementId);

		highlightBtn2 = new JButton("Highlight");
		sl_centerPanel.putConstraint(SpringLayout.NORTH, highlightBtn2, 106, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, highlightBtn2, 936, SpringLayout.WEST, centerPanel);
		highlightBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton highlightbtn = (JButton) e.getSource();
				highlightbtn.setEnabled(false);
				highlightElement(ElementTypes.ID, textFieldID.getText());
				highlightbtn.setEnabled(true);
			}
		});

		textFieldID = new JTextField();
		sl_centerPanel.putConstraint(SpringLayout.NORTH, textFieldID, 107, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, textFieldID, 122, SpringLayout.WEST, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.EAST, textFieldID, 936, SpringLayout.WEST, centerPanel);
		centerPanel.add(textFieldID);
		textFieldID.setColumns(10);
		centerPanel.add(highlightBtn2);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		sl_centerPanel.putConstraint(SpringLayout.NORTH, tabbedPane, 220, SpringLayout.NORTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, lblName);
		sl_centerPanel.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, centerPanel);
		sl_centerPanel.putConstraint(SpringLayout.EAST, tabbedPane, -13, SpringLayout.EAST, centerPanel);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index == 1) {
					// script prereview page
					gotoPreviewPage();
				}
			}
		});

		JPanel runPanel = new JPanel();
		sl_centerPanel.putConstraint(SpringLayout.NORTH, runPanel, 35, SpringLayout.SOUTH, lblElementId);
		sl_centerPanel.putConstraint(SpringLayout.WEST, runPanel, 0, SpringLayout.WEST, lblName);
		sl_centerPanel.putConstraint(SpringLayout.SOUTH, runPanel, -6, SpringLayout.NORTH, tabbedPane);
		sl_centerPanel.putConstraint(SpringLayout.EAST, runPanel, 973, SpringLayout.WEST, centerPanel);
		runPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Actions",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		centerPanel.add(runPanel);

		JButton btnSaveScript = new JButton("Save Script");
		runPanel.add(btnSaveScript);
		sl_centerPanel.putConstraint(SpringLayout.NORTH, btnSaveScript, 6, SpringLayout.SOUTH, textFieldID);
		sl_centerPanel.putConstraint(SpringLayout.EAST, btnSaveScript, -10, SpringLayout.EAST, centerPanel);

		JButton btnNewButton = new JButton("Run Script");
		btnNewButton.setEnabled(false);
		runPanel.add(btnNewButton);
		sl_centerPanel.putConstraint(SpringLayout.NORTH, btnNewButton, 6, SpringLayout.SOUTH, textFieldID);
		sl_centerPanel.putConstraint(SpringLayout.WEST, btnNewButton, 214, SpringLayout.WEST, centerPanel);

		btnSaveScript.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				tabbedPane.setSelectedIndex(1);

				try {
					saveScript(SaveFileName);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		centerPanel.add(tabbedPane);

		JScrollPane PageObjectsPane = new JScrollPane();
		tabbedPane.addTab("Page Objects", null, PageObjectsPane, null);

		// create the tree
		JPopupMenu menu = new JPopupMenu();
		/*
		 * JMenuItem addMenu = new JMenuItem("Add");
		 * addMenu.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { // TODO
		 * Auto-generated method stub DefaultMutableTreeNode lastPathComponent =
		 * (DefaultMutableTreeNode) pageobjectsTree.getSelectionPath()
		 * .getLastPathComponent(); lastPathComponent.add(new
		 * DefaultMutableTreeNode("Sample Node")); expandTree(pageobjectsTree,
		 * true);
		 * 
		 * } }); menu.add(addMenu);
		 */
		JMenuItem expandAllMenu = new JMenuItem("Expand All");
		expandAllMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JTreeUtils.expandTree(pageobjectsTree, true);

			}
		});
		menu.add(expandAllMenu);
		JMenuItem collapseAllMenu = new JMenuItem("Collapse All");
		collapseAllMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JTreeUtils.expandTree(pageobjectsTree, false);
			}
		});
		menu.add(collapseAllMenu);
		menu.addSeparator();
		JMenuItem highlightMenu = new JMenuItem("Highlight");
		highlightMenu.setMnemonic(KeyEvent.VK_H);
		highlightMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		highlightMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				highlightMenuAction();

			}
		});
		menu.add(highlightMenu);
		menu.addSeparator();
		JMenuItem renameMenu = new JMenuItem("Rename");
		renameMenu.setMnemonic(KeyEvent.VK_R);
		renameMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		renameMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				renameNodeAction();

			}
		});
		menu.add(renameMenu);
		menu.add(new JSeparator());
		JMenuItem deleteMenu = new JMenuItem("Delete");
		deleteMenu.setMnemonic(KeyEvent.VK_D);
		deleteMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		deleteMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteTreeNode();
			}
		});
		menu.add(deleteMenu);

		rootIcon = new ImageIcon(getClass().getResource("/images/chrome.png"));
		pageIcon = new ImageIcon(getClass().getResource("/images/page.png"));
		webElementIcon = new ImageIcon(getClass().getResource("/images/element.png"));

		DefaultMutableTreeNode rootNode = new CustomMutableTreeNode(rootIcon,"Chrome");
		pageobjectsTree = new JTree(rootNode);
		pageobjectsTree.setShowsRootHandles(true);
		//pageobjectsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// pageobjectsTree.setEditable(true);
		//TreeCellRenderer cellRenderer = pageobjectsTree.getCellRenderer();
		pageobjectsTree.setCellRenderer(new JTreeUtils.CustomTreeCellRenderer());

		
		editorTreeCellEditor = new JTreeUtils.EditorTreeCellEditor(pageobjectsTree,
				(DefaultTreeCellRenderer) pageobjectsTree.getCellRenderer());
		pageobjectsTree.setCellEditor(editorTreeCellEditor);
		// suppose F2 works on your tree nodes because you called
		// JTree#setEditable(true).F2 binding is installed in
		// BasicTreeUI#installKeyboardActions. You can install your own binding
		// in the usual way:
		InputMap inputMap = pageobjectsTree.getInputMap();
		KeyStroke keyStrokeF2 = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
		inputMap.put(keyStrokeF2, "RenameShortcutAction");
		KeyStroke keyStrokeDelete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		inputMap.put(keyStrokeDelete, "DeleteShortcutAction");
		KeyStroke keyStrokeF3 = KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0);
		inputMap.put(keyStrokeF3, "HighlightShortcutAction");
		/*
		 * KeyStroke keyStrokeEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
		 * 0); inputMap.put(keyStrokeEnter, "EnterShortcutAction");
		 */
		// pageobjectsTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
		// 0), "startEditing");

		// add the right click event
		pageobjectsTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if (SwingUtilities.isRightMouseButton(e)) {
					// pageobjectsTree.setComponentPopupMenu(menu); the easiest
					// way but limit
					// pageobjectsTree.getPathForLocation(e.getX(), e.getY())
					TreePath path = pageobjectsTree.getSelectionPath();
					Rectangle pathBounds = pageobjectsTree.getUI().getPathBounds(pageobjectsTree, path);
					if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
						menu.show(pageobjectsTree, pathBounds.x + pathBounds.height + 3,
								pathBounds.y + pathBounds.height);
					}
				}
			}
		});
		// add the selection event
		pageobjectsTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				String elementName = node.getUserObject().toString();
				for (Map<String, String> nodeMap : childNodes) {
					boolean foundElementNode = nodeMap.get("Name").equals(elementName);
					if (foundElementNode) {
						textFieldName.setText(elementName);
						String elementid = nodeMap.get("Id");
						textFieldID.setText(elementid);
						if (StringUtils.isNotEmpty(elementid)) {
							highlightBtn2.setEnabled(true);
						} else {
							highlightBtn2.setEnabled(false);
						}
						textFieldCSS.setText(nodeMap.get("CSS"));
						textFieldXPath.setText(nodeMap.get("XPath"));
						textFieldContent.setText(nodeMap.get("Text"));
						break;
					}
				}
			}
		});
		editorTreeCellEditor.addCellEditorListener(new CellEditorListener() {

			@Override
			public void editingStopped(ChangeEvent e) {
				String newUpdateValue = editorTreeCellEditor.getCellEditorValue().toString();
				String currentSelectionName = getCurrentSelectionName();
				for (Map<String, String> nodeMap : childNodes) {
					boolean foundElementNode = nodeMap.get("Name").equals(currentSelectionName);
					if (foundElementNode) {
						nodeMap.put("Name", newUpdateValue);
						textFieldName.setText(newUpdateValue);
						break;
					}

				}

			}

			@Override
			public void editingCanceled(ChangeEvent e) {
			}

		});

		pageobjectsTree.getActionMap().put("RenameShortcutAction", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2953156463125250780L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				renameNodeAction();

			}
		});
		pageobjectsTree.getActionMap().put("DeleteShortcutAction", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2951173373978390680L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteTreeNode();

			}
		});
		pageobjectsTree.getActionMap().put("HighlightShortcutAction", new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3251182384673492153L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				highlightMenuAction();

			}
		});

		PageObjectsPane.setViewportView(pageobjectsTree);

		JScrollPane PreviewPane = new JScrollPane();
		tabbedPane.addTab("Preview", null, PreviewPane, null);

		scriptComboBox = new JComboBox();
		scriptComboBox.setModel(new DefaultComboBoxModel(ScriptLanguages.values()));
		scriptComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				// Object selectedItem = scriptComboBox.getSelectedItem();
				gotoPreviewPage();

			}
		});
		PreviewPane.setColumnHeaderView(scriptComboBox);

		JPanel textAreaPanel = RSyntaxTextAreaUtils.createRSyntaxTextArea(true);

		// rTextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// rTextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		PreviewPane.setViewportView(textAreaPanel);

		JLabel lblText = new JLabel("Text:");
		sl_centerPanel.putConstraint(SpringLayout.WEST, lblText, 0, SpringLayout.WEST, textFieldName);
		sl_centerPanel.putConstraint(SpringLayout.SOUTH, lblText, -6, SpringLayout.NORTH, runPanel);
		centerPanel.add(lblText);

		textFieldContent = new JTextField();
		sl_centerPanel.putConstraint(SpringLayout.NORTH, textFieldContent, 6, SpringLayout.SOUTH, textFieldID);
		sl_centerPanel.putConstraint(SpringLayout.WEST, textFieldContent, 0, SpringLayout.WEST, textFieldCSS);
		sl_centerPanel.putConstraint(SpringLayout.EAST, textFieldContent, -10, SpringLayout.EAST, centerPanel);
		centerPanel.add(textFieldContent);

		textFieldContent.setColumns(10);

	}

	private void gotoPreviewPage() {
		ScriptLanguages scriptItem = (ScriptLanguages) scriptComboBox.getSelectedItem();
		String langaugeName = scriptItem.getLangaugeName();

		switch (scriptItem) {
		case JAVA:
			// langugeFile = "java";
			chooser = FileChooserFactory.getJavaFileChooser();
			pageObjectCode = new JavaPageObjectCode();
			SaveFileName = "SamplePageStep";
			break;
		case PYTHON:
			// langugeFile = "py";
			chooser = FileChooserFactory.getPythonFileChooser();
			pageObjectCode = new PythonPageObjectCode();
			SaveFileName = "SamplePageClass";
			break;
		case RUBY:
			// langugeFile = "rb";
			chooser = FileChooserFactory.getRubyFileChooser();
			pageObjectCode = new RubyPageObjectCode();
			SaveFileName = "SamplePage";
			break;
		default:
			// langugeFile = "java";
			chooser = FileChooserFactory.getJavaFileChooser();
			pageObjectCode = new JavaPageObjectCode();
			SaveFileName = "SamplePageStep";
			break;
		}
		RSyntaxTextAreaUtils.setText(langaugeName, pageObjectCode.pageObjectFileTemplate(childNodes, SaveFileName));
		ArrayList<File> jarList = new ArrayList<File>();
		File libFolder = new File("lib");
		if (libFolder.exists() && libFolder.isDirectory()) {
			// get all the selenium libraries
			File[] seleniumJars = libFolder.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.startsWith("selenium");
				}
			});

			for (File jar : seleniumJars) {
				jarList.add(jar);
			}

		}
		RSyntaxTextAreaUtils.addAdditionLibrariesSupport(jarList);

	}

	private void saveScript(String fileName) throws IOException {
		File currentDir = CurrentDirectoryUtils.get();
		chooser.setCurrentDirectory(currentDir);
		chooser.setDialogTitle("Selenium Page Recorder");
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		File defaultFileName = new File(fileName);
		chooser.setSelectedFile(defaultFileName);

		int rtnValue = chooser.showSaveDialog(SeleniumRecorderPanel.this);
		if (rtnValue == JFileChooser.APPROVE_OPTION) {
			File selectFile = chooser.getSelectedFile();

			try (FileWriter fw = new FileWriter(selectFile)) {
				String savedScript = RSyntaxTextAreaUtils.textArea.getText();
				fw.write(savedScript);
				CurrentDirectoryUtils.update(selectFile);
			}
			// do some thing
			// quickStartFrame.setStatusBarText(openFile.getAbsolutePath());
		}

	}

	private void renameNodeAction() {
		pageobjectsTree.setEditable(true);
		TreePath treePath = pageobjectsTree.getSelectionPath();
		// pageobjectsTree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
		// 0), "start Editing");
		pageobjectsTree.startEditingAtPath(treePath);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) pageobjectsTree.getLastSelectedPathComponent();
		String elementName = node.getUserObject().toString();
		setCurrentSelectionName(elementName);
	}

	private void deleteTreeNode() {
		DefaultTreeModel treeModel = (DefaultTreeModel) pageobjectsTree.getModel();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) pageobjectsTree.getSelectionPath()
				.getLastPathComponent();
		if (node != null) {
			treeModel.removeNodeFromParent(node);
			String elementName = node.getUserObject().toString();
			for (Map<String, String> nodeMap : childNodes) {
				boolean foundElementNode = nodeMap.get("Name").equals(elementName);
				if (foundElementNode) {
					childNodes.remove(nodeMap);
					break;
				}

			}
		}
	}

	private void highlightMenuAction() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String elementId = textFieldID.getText().trim();
				String elementCss = textFieldCSS.getText().trim();
				String elementXpath = textFieldXPath.getText().trim();

				boolean highlightElement = highlightElement(ElementTypes.CSS, elementCss);
				if (!highlightElement) {
					highlightElement = highlightElement(ElementTypes.XPATH, elementXpath);
					if (!highlightElement) {
						highlightElement = highlightElement(ElementTypes.ID, elementId);
					}
				}
				if (!highlightElement) {
					DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) pageobjectsTree
							.getLastSelectedPathComponent();
					String elementNodeName = "";
					if (lastSelectedPathComponent != null) {
						elementNodeName = lastSelectedPathComponent.getUserObject().toString();
					}
					JMessageDialogUtils.warning(SeleniumRecorderPanel.this, "Page Recorder",
							"Cannot inspect this element '" + elementNodeName
									+ "' in current opened page.\nPlease check the elements's CSS,XPATH or ID attribute in current web page.");
				}
			}
		});

	}

	private boolean highlightElement(ElementTypes type, String value) {
		boolean findElement = false;
		if (!value.equals("")) {
			By by = null;
			switch (type) {
			case ID:
				by = By.id(value);
				break;
			case CSS:
				by = By.cssSelector(value);
				break;
			case XPATH:
				by = By.xpath(value);
				break;
			default:
				by = By.cssSelector(value);
				break;
			}
			try {
				WebElement element = BaseSteps.driver.findElement(by);
				baseSteps.elementHighLight(element, 200);
				findElement = true;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return findElement;
	}

	class StartSeleniumTask extends AbstractSwingTasker<Void, String> {

		private String currentElementName = "";
		private String browser = "";

		public StartSeleniumTask(Component component, String browser) {
			super(component);
			this.browser = browser;

			// TODO Auto-generated constructor stub
		}

		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			// the new root node in jtree
			/*
			 * DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)
			 * pageobjectsTree.getModel().getRoot(); rootNode.setUserObject(new
			 * String("Root-"+browser));
			 */
			try {
				log.info("Start selenium browser: " + browser);
				SeleniumCore.startBrowser("localhost", browser, true, false);
				log.info("start selenium browser success!");
				baseSteps = new BaseSteps();
				baseSteps.pageVisit(txtHttplocalhostwdhub.getText().trim());

			} catch (Exception e) {
				// TODO: handle exception
				log.error("Start selenium browser exception: " + e.getMessage(), e);
			}
			// get the elements

			DefaultTreeModel treeModel = (DefaultTreeModel) pageobjectsTree.getModel();
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();

			PageRecorder.inspector(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						PageRecorder.InjectPageRecorder();
						JsonObject seleniumCommand = PageRecorder.getSeleniumCommand();
						if (seleniumCommand != null) {
							String commandName = seleniumCommand.get("Command").getAsString();
							if (commandName.equals("rightClickHandler")) {
								// when you right click the element
							} else if (commandName.equals("AddElement")) {
								// add the command to any place you want
								JsonElement jsonaElementName = seleniumCommand.get("ElementName");
								if (jsonaElementName != null) {
									String thisElementName = jsonaElementName.getAsString();
									if (!currentElementName.equals(thisElementName)) {
										currentElementName = thisElementName;

										String elementId = seleniumCommand.get("ElementId").getAsString();
										String cssPath = seleniumCommand.get("CSS").getAsString();
										String XPath = seleniumCommand.get("XPath").getAsString();
										String content = seleniumCommand.get("Text").getAsString();

										String thisCurrentUrl = BaseSteps.driver.getCurrentUrl();
										int LastSeperator = thisCurrentUrl.lastIndexOf("/");
										String pageUrlName = thisCurrentUrl.substring(LastSeperator + 1);
										int childCount = rootNode.getChildCount();
										boolean FindChild = false;
										DefaultMutableTreeNode urlNode = null;
										for (int index = 0; index < childCount; index++) {
											DefaultMutableTreeNode foundUrlNode = (DefaultMutableTreeNode) rootNode
													.getChildAt(index);
											String elementNode = foundUrlNode.getUserObject().toString();
											if (pageUrlName.equals(elementNode)) {
												FindChild = true;
												urlNode = foundUrlNode;
												break;
											}
										}
										if (!FindChild) {

											urlNode = new CustomMutableTreeNode(pageIcon, pageUrlName);
											JTreeUtils.addNode(pageobjectsTree, rootNode, urlNode);

										}
										boolean findChildNode = false;
										int elementNodeCount = urlNode.getChildCount();
										DefaultMutableTreeNode elementNode;
										for (int index = 0; index < elementNodeCount; index++) {
											elementNode = (DefaultMutableTreeNode) urlNode.getChildAt(index);
											String currentNodeElementName = elementNode.getUserObject().toString();
											if (currentNodeElementName.equals(thisElementName)) {
												findChildNode = true;
												break;
											}
										}
										if (!findChildNode) {
											Map<String, String> childNode = new HashMap<>();
											childNode.put("Name", thisElementName);
											childNode.put("CSS", cssPath);
											childNode.put("XPath", XPath);
											childNode.put("Id", elementId);
											childNode.put("Text", content);
											DefaultMutableTreeNode defaultMutableTreeNode = new CustomMutableTreeNode(
													webElementIcon, thisElementName);
											// urlNode.add(defaultMutableTreeNode);
											JTreeUtils.addNode(pageobjectsTree, urlNode, defaultMutableTreeNode);
											// treeModel.reload(rootNode);
											childNodes.add(childNode);
											textFieldName.setText(thisElementName);
											textFieldID.setText(elementId);
											textFieldContent.setText(content);
											textFieldCSS.setText(cssPath);
											textFieldXPath.setText(XPath);
											highlightBtn1.setEnabled(true);
											if (StringUtils.isNotEmpty(elementId)) {
												highlightBtn2.setEnabled(true);
											} else {
												highlightBtn2.setEnabled(false);
											}
											highlightBtn3.setEnabled(true);
										}
									}

								}

							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						log.error("inject page recorder script met exception: " + e.getMessage(), e);
						cancel();
					}

				}
			}, 400);
			return super.doInBackground();
		}

		@Override
		protected void process(List<String> chunks) {
			// TODO Auto-generated method stub
			super.process(chunks);
		}

		@Override
		protected void done() {
			// TODO Auto-generated method stub
			startBtn.setEnabled(true);
			startBtn.setText("Stop");
			super.done();
		}
	}

	public String getCurrentSelectionName() {
		return currentSelectionName;
	}

	public void setCurrentSelectionName(String currentSelectionName) {
		this.currentSelectionName = currentSelectionName;
	}
}
