package com.github.becauseQA.window.selenium.ui;

/*-
 * false
 * commons-window
 * sectionDelimiter
 * Copyright (C) 2016 Alter Hu
 * sectionDelimiter
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/*-
 * #%L
 * commons-window
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 Alter Hu
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class DraggedTabbedPane extends JTabbedPane implements DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DraggedTabbedPane.class);
	private JFrame frame;

	public DraggedTabbedPane(JFrame frame) {
		// Make this pane a drop target and its own listener
		this.frame = frame;
		new DropTarget(this, this);
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetDropEvent event) {
		// TODO Auto-generated method stub
		event.acceptDrop(DnDConstants.ACTION_MOVE);
		Transferable trans = event.getTransferable();

		if (trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			try {

				@SuppressWarnings({ "unchecked" })
				final List<File> droppedFiles = (List<File>) trans.getTransferData(DataFlavor.javaFileListFlavor);

				// open files in new thread, so we can return quickly
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// do some thing
						openFiles(frame, droppedFiles);
					}
				});

			} catch (IOException e) {
				log.error(e);
			} catch (UnsupportedFlavorException e) {
				log.error(e);
			}
		}

	}

	public static void openFiles(JFrame kseFrame, List<File> droppedFiles) {
		for (File droppedFile : droppedFiles) {
			log.info("Open file: " + droppedFile.getAbsolutePath());
		}
	}

}
