package de.hdm.itprojekt.noteit.client;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.NodeInfo;

import de.hdm.itprojekt.noteit.shared.NotesAdministration;
import de.hdm.itprojekt.noteit.shared.NotesAdministrationAsync;
import de.hdm.itprojekt.noteit.shared.bo.Note;
import de.hdm.itprojekt.noteit.shared.bo.Notebook;
import de.hdm.itprojekt.noteit.shared.bo.User;

public class Homepage extends VerticalPanel {

	private final static NotesAdministrationAsync notesAdmin = GWT.create(NotesAdministration.class);
	
	private static Logger rootLogger = Logger.getLogger("");

	// --------- Horizontal Panel -----------//
	HorizontalPanel headlinePanel = new HorizontalPanel();
	HorizontalPanel navPanel = new HorizontalPanel();
	HorizontalPanel navLeftPanel = new HorizontalPanel();
	HorizontalPanel navRightPanel = new HorizontalPanel();
	static HorizontalPanel contentPanel = new HorizontalPanel();
	final static VerticalPanel showNote = new ShowNote();
	final static VerticalPanel editNotebook = new EditNotebook();
	
	
	
	final HorizontalPanel contentNotebookPanel = new HorizontalPanel();
	final static HorizontalPanel contentNotesPanel = new HorizontalPanel();

	// --------- Label -----------//
	Label lbheadlineNotebookLabel = new Label("Notizbücher");
	Label lbheadlineNotesLabel = new Label("Notizen");
	Label lbheadlineNoteit = new Label("Noteit");

	// --------- Button -----------//
//	Button btnAddNewNotebookOrNoteButton = new Button("<img src='Images/plus.png'/ width=\"15\" height=\"15\">");

	// --------- Text Box -----------//
	final TextBox tbSearchNotebook = new TextBox();

	// --------- Noteit Class -----------//
	static User currentUser = new User();
	final Notebooks notebooks = new Notebooks();
	final NotebookCellList notebookCellList = new NotebookCellList();
	static Notebook selectedNotebook = new Notebook();
	static Note selectedNote = new Note();
	
	
	
    

	// --------- Cell List -----------//
	final static CellList<Notebook> clNotebook = new NotebookCellList().createNotebookCellList();
	final static CellList<Note> clNote = new NoteCellList().createNoteCellList();
	
	

	public void onLoad() {
		getCurrentUser();
		//CellBrowser
				TreeViewModel model = new NoteitCellBrowser();
				CellBrowser cellBrowser = new CellBrowser(model, null);
				
				cellBrowser.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			    cellBrowser.setHeight("500px");
			    cellBrowser.setWidth("430px");
			
//			    dockPanel.add(headerPanel, DockPanel.NORTH);
//			    dockPanel.add(new HTML("This is the first south component."), DockPanel.SOUTH);
//				dockPanel.add(showNote, DockPanel.EAST);
//				dockPanel.add(cellBrowser, DockPanel.WEST);
				
				contentPanel.add(cellBrowser);
				contentPanel.add(showNote);
				
				final ListBox listBox1 = new ListBox();
				listBox1.addItem("Notizbuch");
				listBox1.addItem("Notiz");
			      
				Command settingDialog = new Command() {
					public void execute() {
						Settings settings = new Settings(currentUser);
						settings.show();
						settings.center();
						settings.setGlassEnabled(true);
					}
				};
				Command logout = new Command() {
					public void execute() {
						Noteit.loginInfo.getLogoutUrl();
						Window.open(Noteit.loginInfo.getLogoutUrl(), "_self", "");
						Noteit.loadLogin();
					}
				};
				
				MenuBar settings = new MenuBar(true);
			    settings.addItem("Profil", settingDialog);
			    settings.addItem("Abmelden", logout);
				MenuBar menu = new MenuBar();
			    menu.addItem(currentUser.getFirstName(), settings);
			    
			   
				
		

		lbheadlineNotebookLabel.setStylePrimaryName("headlineNotebookLabel");
		lbheadlineNotesLabel.setStylePrimaryName("headlineNotesLabel");
		lbheadlineNoteit.setStyleName("lbheadlineNoteit");
		// Style Names
		headlinePanel.setStyleName("headlinePanel");
		navLeftPanel.setStylePrimaryName("navLeftPanel");
		contentNotebookPanel.setStylePrimaryName("contentNotebookPanel");
		contentNotesPanel.setStylePrimaryName("contentNotesPanel");
		navPanel.setStylePrimaryName("navPanel");
		navRightPanel.setStyleName("menu");
		contentPanel.setStylePrimaryName("contentPanel");
//		btnAddNewNotebookOrNoteButton.setStylePrimaryName("btnAddNewNotebookButton");

		// Alignment
		contentNotebookPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		contentNotesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		navPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		navPanel.setWidth("100%");
		contentPanel.setWidth("100%");
		headlinePanel.setWidth("100%");
		headlinePanel.add(lbheadlineNoteit);
		navLeftPanel.add(listBox1);
//		navLeftPanel.add(btnAddNewNotebookOrNoteButton);
		navLeftPanel.add(tbSearchNotebook);
		
		navRightPanel.add(menu);
		
		navPanel.add(navLeftPanel);
		navPanel.add(navRightPanel);

		/**
		 * create the TextBox for Notebook Search, and include it to the Panel
		 */

		tbSearchNotebook.getElement().setPropertyString("placeholder", "Suchen...");
		tbSearchNotebook.setStylePrimaryName("tbSearchNotebook");
		



		/**
		 * add to the Panels
		 */
		
		//navPanel.add(navNotebookPanel);
//		navPanel.add(navNotesPanel);
	//	contentPanel.add(contentNotesPanel);
		
		

		/**
		 * Add all notebooks at start to the panel
		 */
		notesAdmin.getAllNotebooksByUserID(currentUser.getId(), new AsyncCallback<ArrayList<Notebook>>() {

			@Override
			public void onSuccess(ArrayList<Notebook> result) {
				System.out.println("result" + result);
				clNotebook.setRowData(result);
				contentNotebookPanel.add(clNotebook);
				notesAdmin.getAllNotesByNotebookID(result.get(0).getId(),currentUser.getId() , new AsyncCallback<ArrayList<Note>>() {

					@Override
					public void onSuccess(ArrayList<Note> result) {

						clNote.setRowData(result);
						contentNotesPanel.add(clNote);
						
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Error" + caught);
			}
		});
		

//		btnAddNewNotebookOrNoteButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if (listBox1.getSelectedItemText() == "Notiz") {
//					if (selectedNotebook.getId() == 0) {
//						Window.alert("in diesem Notizbuch können sie keine Notizen erstellen");
//					} else
//						NoteitCellBrowser.addNote();
//				}else{
//					NoteitCellBrowser.addNotebook();
//				}
//			}
//		});
		


		
		listBox1.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(listBox1.getSelectedItemText() == "Notiz"){
					/**
					 * Create the ChangeHandler for TextBox for Search Note Function.
					 */
					tbSearchNotebook.addValueChangeHandler(new ValueChangeHandler<String>() {

						@Override
						public void onValueChange(ValueChangeEvent<String> event) {
							NoteitCellBrowser.searchNoteByKeyword(currentUser.getId(), event.getValue());
							
						}
					});
					
					
			      }else{
			    	  /**
			  		 * Create the ChangeHandler for TextBox for Search Notebook Function.
			  		 */
			  		tbSearchNotebook.addValueChangeHandler(new ValueChangeHandler<String>() {

			  			@Override
			  			public void onValueChange(ValueChangeEvent<String> event) {
			  				NoteitCellBrowser.searchNotebookByKeyword(currentUser.getId(), event.getValue());
			  				
			  			}
			  		});
			  		
			  		
			      }
				
			}
		});

		this.add(headlinePanel);
		this.add(navPanel);
		this.add(contentPanel);

	}
	
	public Homepage(){
		
	}
	
	public Homepage(User currentUser){
		this.currentUser = currentUser;
	}
	

	public static void showCurrentNote(VerticalPanel vpShowNote){
		
		contentPanel.remove(1);
		vpShowNote.setHeight("300px");
		vpShowNote.setWidth("500px");
		contentPanel.add(vpShowNote);
		
		
	}
	
	public static void showNotes(){
		contentPanel.remove(1);
		contentPanel.add(contentNotesPanel);
		
	}

	/**
	 * set all notes from selected notebook
	 * 
	 * @param notebook
	 */
	public static void setNotesWhenNotebookSelected(Notebook notebook) {
		selectedNotebook = notebook;
		rootLogger.log(Level.SEVERE, "ID" + notebook.getId() + "NotebookID" + notebook.getId());

		notesAdmin.getAllNotesByNotebookID(notebook.getId(),getCurrentUser().getId(), new AsyncCallback<ArrayList<Note>>() {

			@Override
			public void onSuccess(ArrayList<Note> result) {
				System.out.println("result" + result);

				clNote.setRowData(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Error" + caught);
			}
		});
	};
	
	public static void setSelectedNote (Note note){
		selectedNote = note;
		
	}

	public void searchNotebookByKeyword(int userID, String keyword) {
		notesAdmin.findNotebooksByKeyword(userID, keyword, new AsyncCallback<ArrayList<Notebook>>() {

			@Override
			public void onSuccess(ArrayList<Notebook> result) {
				clNotebook.setRowData(result);

			}

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Error find notebook " + caught);

			}
		});
	}

	public void searchNoteByKeyword(int userID, String keyword, int notebookID) {
		rootLogger.log(Level.SEVERE, "userid: " + userID + "searchtext: " + keyword + "notebookID: " + notebookID);
		notesAdmin.findNoteByKeyword(userID, keyword, notebookID, new AsyncCallback<ArrayList<Note>>() {

			@Override
			public void onSuccess(ArrayList<Note> result) {
				clNote.setRowData(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	public static void updateNotebookCellList(int userID) {
		notesAdmin.getAllNotebooksByUserID(userID, new AsyncCallback<ArrayList<Notebook>>() {

			@Override
			public void onSuccess(ArrayList<Notebook> result) {
				clNotebook.setRowData(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}
	public static void updateNotesCellList (int notebookId){
		
		notesAdmin.getAllNotesByNotebookID(notebookId, getCurrentUser().getId(), new AsyncCallback<ArrayList<Note>>() {

			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(ArrayList<Note> result) {
				clNote.setRowCount(0);
				clNote.setRowData(result);
				
			}
		});
	}

	public Notebook getSelectedNotebook() {
		return selectedNotebook;
	}
	public static void setSelectedNotebook(Notebook selectedNotebook2){
		selectedNotebook = selectedNotebook2;
	}
	
	public static User getCurrentUser(){
		currentUser.setId(1);
		currentUser.setFirstName("Max");
		currentUser.setLastName("Mustermann");
		currentUser.setMail("max@mustermann.de");
		return currentUser;
	}
	
	public static void editNotebookView(){
		contentPanel.remove(1);
		EditNotebook editNotebookView = new EditNotebook();
		contentPanel.add(editNotebookView);
	}
	public static void showNoteView(){
		contentPanel.remove(1);
		contentPanel.add(showNote);
		
	}


}
