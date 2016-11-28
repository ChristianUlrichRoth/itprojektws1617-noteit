package de.hdm.itprojekt.noteit.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.itprojekt.noteit.shared.bo.User;

public class Homepage extends VerticalPanel {
	public void onLoad() {

		// local User
		final User user = new User();
		user.setId(1);

		HorizontalPanel navPanel = new HorizontalPanel();
		HorizontalPanel navNotebookPanel = new HorizontalPanel();
		HorizontalPanel navNotesPanel = new HorizontalPanel();
		HorizontalPanel contentPanel = new HorizontalPanel();
		final HorizontalPanel contentNotebookPanel = new HorizontalPanel();
		HorizontalPanel contentNotesPanel = new HorizontalPanel();

		final Notebooks notebooks = new Notebooks();
		notebooks.getAllNotebooks(1);
		contentNotebookPanel.add(notebooks.getAllNotebooks(1));

		Label lbheadlineNotebookLabel = new Label("Notizbücher");
		Label lbheadlineNotesLabel = new Label("Notizen");
		Label lblTitleAddNotebook = new Label("Name des Notizbuches");

		Button btnaddNoteButton = new Button("+");
		Button btnaddNotebookButton = new Button("+");
		Button btnsearchNoteButton = new Button("Notiz suchen");
		Button btnAddNewNotebook = new Button("Notizbuch erstellen");
		Button btnEditNotebook = new Button ("<img src='war/Images/Search-48.png'/>");

		lbheadlineNotebookLabel.setStylePrimaryName("headlineNotebookLabel");
		lbheadlineNotesLabel.setStylePrimaryName("headlineNotesLabel");

		navNotebookPanel.setStylePrimaryName("navNotebookPanel");
		navNotesPanel.setStylePrimaryName("navNotesPanel");
		contentNotebookPanel.setStylePrimaryName("contentNotebookPanel");
		contentNotesPanel.setStylePrimaryName("contentNotesPanel");

		navNotebookPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		navNotesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		contentNotebookPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		contentNotesPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		navPanel.setWidth("1000px");
		contentPanel.setWidth("1000px");
		navNotebookPanel.setWidth("500px");
		navNotesPanel.setWidth("500px");
		contentNotebookPanel.setWidth("500px");
		contentNotesPanel.setWidth("500px");

		contentNotebookPanel.setHeight("300px");
		contentNotesPanel.setHeight("300px");

		navNotebookPanel.add(lbheadlineNotebookLabel);
		navNotesPanel.add(lbheadlineNotesLabel);
		navNotesPanel.add(btnaddNoteButton);
		navNotebookPanel.add(btnaddNotebookButton);
		navNotebookPanel.add(btnEditNotebook);
		navNotesPanel.add(btnsearchNoteButton);

		/**
		 * create the TextBox, and included to the Panel
		 */
		final TextBox tbSearchNotebook = new TextBox();
		navNotebookPanel.add(tbSearchNotebook);

		final TextBox tbAddNewNotebook = new TextBox();

		/**
		 * add to the Panels
		 */
		navPanel.add(navNotebookPanel);
		navPanel.add(navNotesPanel);
		contentPanel.add(contentNotebookPanel);
		contentPanel.add(contentNotesPanel);
		contentNotebookPanel.add(notebooks);

		// /**
		// * Create the DialoBox and Panel, this is the Popup for the
		// addNotesButton
		// */
		// final DialogBox notizBuchDialogBox = new DialogBox();
		// notizBuchDialogBox.setGlassEnabled(true);
		// notizBuchDialogBox.setAnimationEnabled(true);
		// notizBuchDialogBox.setText("Notizbuch bearbeiten?");
		//
		// VerticalPanel editNotebook = new EditNotebook();
		// editNotebook.setSpacing(40);
		// notizBuchDialogBox.setWidget(editNotebook);

		/**
		 * create the DialogBox
		 */
		final DialogBox dbAddNotebook = new DialogBox();
		dbAddNotebook.setGlassEnabled(true);
		dbAddNotebook.setAnimationEnabled(true);
		dbAddNotebook.setText("Notizbuch hinzufügen");
		VerticalPanel vpAddNewNotebookPanel = new VerticalPanel();

		vpAddNewNotebookPanel.add(lblTitleAddNotebook);
		vpAddNewNotebookPanel.add(tbAddNewNotebook);
		vpAddNewNotebookPanel.add(btnAddNewNotebook);

		vpAddNewNotebookPanel.setSpacing(40);
		dbAddNotebook.setWidget(vpAddNewNotebookPanel);

		btnAddNewNotebook.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				/**
				 * create new Notebook
				 */
				notebooks.createNotebooks(tbAddNewNotebook.getText(), user);

				tbAddNewNotebook.setText("");

				/**
				 * close the Popup
				 */
				dbAddNotebook.hide();

			}
		});

		/**
		 * Create the Button and the ClickHandler
		 */
		btnaddNotebookButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// VerticalPanel editNotebook = new EditNotebook();

				dbAddNotebook.center();
				dbAddNotebook.show();

				// RootPanel.get("content").clear();
				// RootPanel.get("content").add(editNotebook);
			}
		});

		btnaddNoteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				VerticalPanel editNotes = new EditNotes();

				RootPanel.get("content").clear();
				RootPanel.get("content").add(editNotes);
			}
		});

		/**
		 * Create the TextBox with ChangeHandler for Search Notebook Function.
		 */
		tbSearchNotebook.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				contentNotebookPanel.add(notebooks.getAllNotebooksByKeyword(1, event.getValue()));
			}
		});

		btnsearchNoteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				VerticalPanel searchNotes = new SearchNotes();

				RootPanel.get("content").clear();
				RootPanel.get("content").add(searchNotes);
			}
		});

		this.add(navPanel);
		this.add(contentPanel);

	}
}
