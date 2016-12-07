package de.hdm.itprojekt.noteit.client;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.itprojekt.noteit.shared.NotesAdministration;
import de.hdm.itprojekt.noteit.shared.NotesAdministrationAsync;
import de.hdm.itprojekt.noteit.shared.bo.Note;
import de.hdm.itprojekt.noteit.shared.bo.Notebook;
import de.hdm.itprojekt.noteit.shared.bo.User;

/**
 * The model that defines the nodes in the tree.
 */
public class NoteitCellBrowser implements TreeViewModel {
	private NotesAdministrationAsync notesAdmin = GWT.create(NotesAdministration.class);

	private ListDataProvider<Notebook> notebooksListDataProvider;
	private ListDataProvider<Note> notesListDataProvider;
	
	private User currentUser = Noteit.currentUser;
	/**
	 * This selection model is shared across all leaf nodes. A selection model
	 * can also be shared across all nodes in the tree, or each set of child
	 * nodes can have its own instance. This gives you flexibility to determine
	 * how nodes are selected.
	 */
	private final SingleSelectionModel<Notebook> selectionModelNotebook = new SingleSelectionModel<Notebook>();
	private final SingleSelectionModel<Note> selectionModelNote = new SingleSelectionModel<Note>();

	
	
	/**
	 * Get the {@link NodeInfo} that provides the children of the specified
	 * value.
	 */
	
	public NoteitCellBrowser() {
	}
	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if (value == null) {

			notebooksListDataProvider = new ListDataProvider<Notebook>();
			notesAdmin.getAllNotebooksByUserID(currentUser.getId(), new AsyncCallback<ArrayList<Notebook>>() {

				@Override
				public void onSuccess(ArrayList<Notebook> result) {
					for (Notebook notebook : result) {
						notebooksListDataProvider.getList().add(notebook);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});
			// LEVEL 0.
			// We passed null as the root value. Return the composers.

			// Create a data provider that contains the list of composers.
			//ListDataProvider<Notebook> dataProvider = new ListDataProvider<Notebook>(notebooks);
			
			
			// Return a node info that pairs the data provider and the cell.
			return new DefaultNodeInfo<Notebook>(notebooksListDataProvider, new NotebookCell(), selectionModelNotebook, null);
			
		} else if (value instanceof Notebook) {
			
			// LEVEL 1.
			// We want the children of the notebook. Return the notes.
			
			notesListDataProvider = new ListDataProvider<Note>();
			notesAdmin.getAllNotesByNotebookID(((Notebook) value).getId(), currentUser.getId(), new AsyncCallback<ArrayList<Note>>() {
				
				@Override
				public void onSuccess(ArrayList<Note> result) {
					for (Note note : result) {
						notesListDataProvider.getList().add(note);
					}
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			return new DefaultNodeInfo<Note>(notesListDataProvider, new NoteCell(), selectionModelNote, null);
		} 

		return null;
	}

	/**
	 * Check if the specified value represents a leaf node. Leaf nodes cannot be
	 * opened.
	 */
	public boolean isLeaf(Object value) {
		// The leaf nodes are the songs, which are Strings.
		if (value instanceof String) {
			return true;
		}
		return false;
	}

}
