package de.hdm.itprojekt.noteit.client;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import de.hdm.itprojekt.noteit.shared.NotesAdministrationAsync;
import de.hdm.itprojekt.noteit.shared.bo.Note;
import de.hdm.itprojekt.noteit.shared.bo.Notebook;
import de.hdm.itprojekt.noteit.shared.bo.User;

/**
 * diese Klasse stellt eine durchsuchbare Ansicht von Notizbüchern und deren Notizen dar, in dem nur ein einzelner Knoten
 * pro Ebene gleichzeitig geöffnet sein kann
 * 
 * @author Tobias Dahms
 * @author Maik Zimmermann
 */
public class NoteitCellBrowser implements TreeViewModel {
	private static NotesAdministrationAsync notesAdmin = ClientsideSettings.getAdministrationService();

	private static ListDataProvider<Notebook> notebooksListDataProvider = new ListDataProvider<Notebook>();
	public static ListDataProvider<Note> notesListDataProvider = new ListDataProvider<Note>();

	private static User currentUser = Noteit.getCurrentUser();
	private static Notebook selectedNotebook = new Notebook();
	private static Note selectedNote = new Note();

	private static Notebook addNotebook = new Notebook();
	private static Note addNote = new Note();

	private static final NoSelectionModel<Notebook> selectionModelNotebook = new NoSelectionModel<Notebook>();
	private static final NoSelectionModel<Note> selectionModelNote = new NoSelectionModel<Note>();


	/**
	 * Gibt das Objekt NodeInfo {@link NodeInfo} zurück, welches die Inhalte für einen Knoten darstellt
	 */

	public <T> NodeInfo<?> getNodeInfo(T value) {
		
		if (value == null) {

			// LEVEL 0.
			// We passed null as the root value. Return the notebooks.

			addCreateNewNotebookButton();
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

			// Return a node info that pairs the data provider and the cell.
			return new DefaultNodeInfo<Notebook>(notebooksListDataProvider, new NotebookCell(), selectionModelNotebook,
					null);

		} else if (value instanceof Notebook) {
			
			Homepage.editNotebookView();

			selectedNotebook = selectionModelNotebook.getLastSelectedObject();
			EditNotebook.setNotebook(selectedNotebook);

			EditNotebook.getAllPermittedUsersbyNotebookID(selectedNotebook.getId());
			Homepage.setSelectedNotebook(selectedNotebook);
			
			notesListDataProvider.getList().clear();
			// LEVEL 1.
			// We want the children of the notebook. Return the notes.
			if (((Notebook) value).getId() != 0 && ((Notebook) value).getId() != -1
					&& ((Notebook) value).getPermissionID() != 1) {

				addNote.setId(0);
				addNote.setTitle("");
				addNote.setSubTitle("");
				addNote.setText("");

				notesListDataProvider.getList().add(addNote);

			}

			notesAdmin.getAllNotesByNotebookID(((Notebook) value).getId(), currentUser.getId(),
					new AsyncCallback<ArrayList<Note>>() {

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
		} else if (value instanceof Note) {

			selectedNote = selectionModelNote.getLastSelectedObject();

			if (((Note) value).getId() != 0) {
				ShowNote.setCurrentNotebook(selectedNotebook);
				ShowNote.getAllPermittedUsersbyNoteID(selectionModelNote.getLastSelectedObject().getId());
				ShowNote.showNote(selectionModelNote.getLastSelectedObject());

			} else {
				// ShowNote.setCurrentNotebook(selectedNotebook);
				 ShowNote.showNote(selectionModelNote.getLastSelectedObject());

				ShowNote.tbNoteTitel.setText("");
				ShowNote.tbNoteSubTitel.setText("");
				ShowNote.content.setText("");
				ShowNote.dateBox.setValue(null);
				ShowNote.setPermissionPanelInvisible();
				ShowNote.lblHeaderTitel.setText("Neue Notiz");
			}
			Homepage.showNoteView();
		}

		return null;
	}

	/**
	 * Prüft ob die Selektion ein Blattknoten darstellt
	 */
	public boolean isLeaf(Object value) {
		// The leaf nodes are the songs, which are Strings.
		if (value instanceof String) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sucht ein Notizbuch anhand eines Suchbegriffs
	 * @param userID
	 * @param keyword
	 */

	public static void searchNotebookByKeyword(int userID, String keyword) {

		ArrayList<Notebook> searchedNotebooks = new ArrayList<Notebook>();

		if (keyword != "") {
			// Notizbuch in aktueller NotebookList durchsuchen
			for (Notebook foundedNotebook : notebooksListDataProvider.getList()) {
				String title = foundedNotebook.getTitle().toLowerCase();
				if (title.contains(keyword)) {
					searchedNotebooks.add(foundedNotebook);
				}
			}
			notebooksListDataProvider.setList(searchedNotebooks);
		} else {
			updateNotebooks();
		}

	}
/**
 * Sucht eine Notiz anhand eines Suchbegriffs
 * @param userID
 * @param keyword
 */
	public static void searchNoteByKeyword(int userID, String keyword) {
		ArrayList<Note> searchedNote = new ArrayList<Note>();

		if (keyword != "") {
			// Notizbuch in aktueller NotebookList durchsuchen
			for (Note foundedNote : notesListDataProvider.getList()) {
				String title = foundedNote.getTitle().toLowerCase();
				if (title.contains(keyword)) {
					searchedNote.add(foundedNote);
				}
			}
			notesListDataProvider.setList(searchedNote);
		} else {
			updateNotes();
		}

	}
/**
 * Diese Methode löscht ein Notizbuch aus dem CellBrowser
 */
	public static void deleteNotebook() {
		int newID = selectedNotebook.getId();
		newID--;

		notebooksListDataProvider.getList().remove(selectedNotebook);
		selectedNotebook.setId(newID);
		selectionModelNotebook.setSelected(selectedNotebook, true);
	}
/**
 * Diese Methode löscht eine Notiz aus dem CellBrowser
 */
	public static void deleteNote() {
		int newId = selectedNote.getId();
		newId--;

		notesListDataProvider.getList().remove(selectedNote);
		selectedNote.setId(newId);
		selectionModelNote.setSelected(selectedNote, true);

	}
/**
 * Diese Methode fügt ein Notizbuch dem Listdataprovider hinzu
 * @param notebook
 */
	public static void getNotebookList(Notebook notebook) {
		notebooksListDataProvider.getList().add(notebook);
		// notebooksListDataProvider.refresh();
	}
/**
 * Diese Methode fügt eine Notiz dem Listdataprovider hinzu
 * @param note
 */
	public static void getNoteList(Note note) {
		notesListDataProvider.getList().add(note);

	}


/**
 * Diese Methode gibt das aktuell ausgewählte Objekt zurück
 * @return
 */
	public static Note getSelectedNote() {
		return selectedNote;
	}
/**
 * Diese Methode aktualisiert die Dataprovider Liste für Notizen
 */
	public static void updateNotes() {
		notesListDataProvider.getList().clear();
		notesAdmin.getAllNotesByNotebookID(selectedNotebook.getId(), currentUser.getId(),
				new AsyncCallback<ArrayList<Note>>() {

					@Override
					public void onSuccess(ArrayList<Note> result) {
						
						if(selectedNotebook.getId() != -1){
							if(selectedNotebook.getPermissionID() != 1){
						result.add(0, addNote);
							}
						}
						notesListDataProvider.setList(result);

					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}
/**
 * Diese Methode aktualisiert die Dataprovider Liste für Notizbücher
 */
	public static void updateNotebooks() {
		notebooksListDataProvider.getList().clear();
		notesAdmin.getAllNotebooksByUserID(currentUser.getId(), new AsyncCallback<ArrayList<Notebook>>() {

			@Override
			public void onSuccess(ArrayList<Notebook> result) {
				result.add(0, addNotebook);
				notebooksListDataProvider.setList(result);

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}
/**
 * Diese Methode erzeugt den Erstellungsbutton für Notizbücher
 */
	public static void addCreateNewNotebookButton() {

		addNotebook.setId(0);
		addNotebook.setTitle("");
		selectionModelNotebook.isSelected(addNotebook);

		notebooksListDataProvider.getList().add(addNotebook);
	}

	/**
	 * Absteigend sortieren nach Fälligkeitsdatum
	 * 
	 * @param notebookID
	 */
	public static void sortNotesMaturityDesc(int notebookID) {
		ArrayList<Note> notesList = new ArrayList<>();
		for (Note note : notesListDataProvider.getList()) {
			notesList.add(note);
		}
		java.util.Collections.sort(notesList, new Comparator<Note>() {

			@Override
			public int compare(Note o1, Note o2) {
				// TODO Auto-generated method stub
				if (o1.getMaturityDate() == null || o2.getMaturityDate() == null)
					return 0;
				return o2.getMaturityDate().compareTo(o1.getMaturityDate());

			}
		});
		notesListDataProvider.setList(notesList);

	}

	/**
	 * Aufsteigend sortieren nach Fälligkeitsdatum
	 * 
	 * @param notebookID
	 */
	public static void sortNotesByMaturityAsc(int notebookID) {
		ArrayList<Note> notesList = new ArrayList<>();
		for (Note note : notesListDataProvider.getList()) {
			notesList.add(note);
		}
		java.util.Collections.sort(notesList, new Comparator<Note>() {

			@Override
			public int compare(Note o1, Note o2) {
				// TODO Auto-generated method stub
				if (o1.getMaturityDate() == null || o2.getMaturityDate() == null)
					return 0;
				return o1.getMaturityDate().compareTo(o2.getMaturityDate());

			}
		});

		notesListDataProvider.setList(notesList);

	}

	/**
	 * Absteigend sortieren nach Erstelldatum
	 * 
	 * @param notebookID
	 */
	public static void sortNotesCreationDateDesc(int notebookID) {
		ArrayList<Note> notesList = new ArrayList<>();
		for (Note note : notesListDataProvider.getList()) {
			notesList.add(note);
		}
		java.util.Collections.sort(notesList, new Comparator<Note>() {

			@Override
			public int compare(Note o1, Note o2) {
				// TODO Auto-generated method stub
				if (o1.getCreationDate() == null || o2.getCreationDate() == null)
					return 0;
				return o2.getCreationDate().compareTo(o1.getCreationDate());

			}
		});

		notesListDataProvider.setList(notesList);

	}

	/**
	 * Aufsteigend sortieren nach Erstelldatum
	 * 
	 * @param notebookID
	 */
	public static void sortNotesCreationDateAsc(int notebookID) {
		ArrayList<Note> notesList = new ArrayList<>();
		for (Note note : notesListDataProvider.getList()) {
			notesList.add(note);
		}
		java.util.Collections.sort(notesList, new Comparator<Note>() {

			@Override
			public int compare(Note o1, Note o2) {
				// TODO Auto-generated method stub
				if (o1.getCreationDate() == null || o2.getCreationDate() == null)
					return 0;
				return o1.getCreationDate().compareTo(o2.getCreationDate());

			}
		});

		notesListDataProvider.setList(notesList);

	}

	/**
	 * Absteigend sortieren nach Änderungsdatum
	 * 
	 * @param notebookID
	 */
	public static void sortNotesModificationDateDesc(int notebookID) {
		ArrayList<Note> notesList = new ArrayList<>();
		for (Note note : notesListDataProvider.getList()) {
			notesList.add(note);
		}
		java.util.Collections.sort(notesList, new Comparator<Note>() {

			@Override
			public int compare(Note o1, Note o2) {
				// TODO Auto-generated method stub
				if (o1.getModificationDate() == null || o2.getModificationDate() == null)
					return 0;
				return o2.getModificationDate().compareTo(o1.getModificationDate());

			}
		});

		notesListDataProvider.setList(notesList);

	}

	/**
	 * Aufsteigend sortieren nach Änderungsdatum
	 * 
	 * @param notebookID
	 */
	public static void sortNotesModificationDateAsc(int notebookID) {
		ArrayList<Note> notesList = new ArrayList<>();
		for (Note note : notesListDataProvider.getList()) {
			notesList.add(note);
		}
		java.util.Collections.sort(notesList, new Comparator<Note>() {

			@Override
			public int compare(Note o1, Note o2) {
				// TODO Auto-generated method stub
				if (o1.getModificationDate() == null || o2.getModificationDate() == null)
					return 0;
				return o1.getModificationDate().compareTo(o2.getModificationDate());

			}
		});

		notesListDataProvider.setList(notesList);

	}

}
