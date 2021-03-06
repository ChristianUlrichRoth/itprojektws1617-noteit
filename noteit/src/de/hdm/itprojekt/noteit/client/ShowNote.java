package de.hdm.itprojekt.noteit.client;

import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

import de.hdm.itprojekt.noteit.shared.NotesAdministrationAsync;
import de.hdm.itprojekt.noteit.shared.bo.Note;
import de.hdm.itprojekt.noteit.shared.bo.Notebook;
import de.hdm.itprojekt.noteit.shared.bo.User;

public class ShowNote extends VerticalPanel {

	private final static NotesAdministrationAsync notesAdmin = ClientsideSettings.getAdministrationService();
	static ArrayList<User> userList = new ArrayList<User>();
	static CellList<User> clUser = new UserCellList().createUserCellList();

	static HorizontalPanel hpShowNote = new HorizontalPanel();
	static HorizontalPanel hpHeader = new HorizontalPanel();
	static HorizontalPanel hpAddPermission = new HorizontalPanel();
	static HorizontalPanel hpBtnPanel = new HorizontalPanel();

	static VerticalPanel vpLeft = new VerticalPanel();
	static VerticalPanel vpRight = new VerticalPanel();
	static VerticalPanel vpTitel = new VerticalPanel();
	static VerticalPanel hpNoteSubTitel = new VerticalPanel();
	static VerticalPanel hpNoteText = new VerticalPanel();
	static VerticalPanel vpNotePermission = new VerticalPanel();
	static VerticalPanel hpBackButton = new VerticalPanel();
	static VerticalPanel hpNoteMaturity = new VerticalPanel();

	static Label lblHeaderTitel = new Label();
	static Label lblNoteTitel = new Label("Titel");
	static Label lblNoteDate = new Label();
	static Label lblNoteSubTitel = new Label("Untertitel");
	static Label lblNoteText = new Label("Deine Notiz");
	static Label lblNoteMaturity = new Label("Fälligkeitsdatum");
	static Label lblPermissionInformationRead = new Label(
			"Deine Berechtigung für diese Notiz beschränkt sich auf das Lesen.");
	static Label lblPermissionInformationWrite = new Label(
			"Deine Berechtigung für diese Notiz beschränkt sich auf das Bearbeiten.");
	static Label lblPermissionInformationDelete = new Label("Du hast volle Berechtigung für diese Notiz");

	static Label lblNotePermission = new Label();
	static Label lblNoteShare = new Label("Notiz Teilen mit:");
	static Label lblNoteShareRB = new Label("Berechtigung festlegen:");

	static TextBox tbNoteSubTitel = new TextBox();
	static TextBox tbNoteTitel = new TextBox();
	static TextBox tbNoteShareMail = new TextBox();

	static Button btnSaveNote = new Button("Speichern");
	static Button btnUnsubcribe = new Button("Deabonnieren");
	static Button btnDeleteNote = new Button("Löschen");
	static Button btnAddNotePermission = new Button("<img src='Images/check.png'/ width=\"10\" height=\"10\">");
	static Button btnDeletePermission = new Button("<img src='Images/cancle.png'/ width=\"10\" height=\"10\">");
	static Button btnNo = new Button("Nein");
	static Button btnYes = new Button("Ja");

	static RichTextArea content = new RichTextArea();

	static Note currentNote = new Note();
	static Notebook currentNotebook = new Notebook();

	static RadioButton rbRead = new RadioButton("permission", "lesen");
	static RadioButton rbWrite = new RadioButton("permission", "bearbeiten");
	static RadioButton rbDelete = new RadioButton("permission", "bearbeiten & löschen");
	DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
	static DateBox dateBox = new DateBox();

	VerticalPanel vDialog = new VerticalPanel();
	HorizontalPanel hDialog = new HorizontalPanel();

	protected void run() {

		vpLeft.setWidth("300px");
		vpRight.setWidth("300px");
		hpAddPermission.setWidth("300px");
		vpNotePermission.setWidth("300px");

		tbNoteShareMail.getElement().setPropertyString("placeholder", "nutzer@noteit.de");
		tbNoteSubTitel.getElement().setPropertyString("placeholder", "Dein Untertitel");
		tbNoteTitel.getElement().setPropertyString("placeholder", "Dein Titel");
		content.getElement().setAttribute("placeholder", "Deine Notiz");

		hpHeader.setStyleName("headerDetailView");
		lblHeaderTitel.setStyleName("lblHeaderTitel");
		lblNotePermission.setStyleName("lblPermissionText");
		hpShowNote.setStyleName("showDetailContent");
		vpRight.setStyleName("vpRightDetailContent");
		hpAddPermission.setStyleName("vpAddPermissionNotebook");
		lblNoteDate.setStyleName("lblNoteDate");
		vpLeft.setStyleName("vpLeft");
		vpRight.setStyleName("vpRight");

		// tbNoteTitel.setStyleName("textbox");
		// tbNoteSubTitel.setStyleName("textbox");
		// dateBox.setStyleName("textbox");

		rbRead.setValue(true);
		hpAddPermission.add(tbNoteShareMail);
		hpAddPermission.add(btnAddNotePermission);
		hpAddPermission.add(btnDeletePermission);
		hpAddPermission.setSpacing(0);

		lblPermissionInformationWrite.setVisible(false);
		lblPermissionInformationDelete.setVisible(false);
		lblPermissionInformationRead.setVisible(false);

		hpHeader.add(lblHeaderTitel);
		hpHeader.add(lblNoteDate);
		hpShowNote.setWidth("600px");
		/**
		 * Create the Panel, Label and TextBox
		 */

		// vpTitel.add(lblNoteTitel);
		vpTitel.add(lblNoteTitel);
		vpTitel.add(tbNoteTitel);
		vpTitel.setWidth("300px");

		/**
		 * Create the Panel, Label and TextBox
		 */

		// hpNoteSubTitel.add(lblNoteSubTitel);
		hpNoteSubTitel.add(lblNoteSubTitel);
		hpNoteSubTitel.add(tbNoteSubTitel);
		hpNoteSubTitel.setWidth("300px");

		/**
		 * Create the Panel, Label and TextBox
		 */

		// hpNoteText.add(lblNoteText);
		hpNoteText.add(content);
		hpNoteText.setWidth("300px");
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		dateBox.getDatePicker().setYearArrowsVisible(true);
		dateBox.getElement().setPropertyString("placeholder", "Fälligkeitsdatum");
		if (currentNote.getMaturityDate() != null) {
			dateBox.setValue(currentNote.getMaturityDate());
		}

		// hpNoteMaturity.add(lblNoteMaturity);
		hpNoteMaturity.add(lblNoteMaturity);
		hpNoteMaturity.add(dateBox);
		hpNoteMaturity.setWidth("300px");

		vpNotePermission.add(lblNotePermission);
		vpNotePermission.add(clUser);

		hpBtnPanel.setWidth("300px");
		hpBtnPanel.add(btnSaveNote);
		hpBtnPanel.add(btnUnsubcribe);
		hpBtnPanel.add(btnDeleteNote);

		btnUnsubcribe.setVisible(false);
		btnDeleteNote.setVisible(false);

		vDialog.setSpacing(10);
		vDialog.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vDialog.add(hDialog);
		hDialog.add(btnYes);
		hDialog.add(btnNo);

		vpLeft.add(vpTitel);
		vpLeft.add(hpNoteSubTitel);
		vpLeft.add(hpNoteText);
		vpLeft.add(hpNoteMaturity);
		vpLeft.add(hpBtnPanel);
		vpLeft.add(lblPermissionInformationRead);
		vpLeft.add(lblPermissionInformationWrite);
		vpLeft.add(lblPermissionInformationDelete);

		vpRight.add(lblNoteShare);
		vpRight.add(hpAddPermission);
		vpRight.add(lblNoteShareRB);
		vpRight.add(rbRead);
		vpRight.add(rbWrite);
		vpRight.add(rbDelete);
		vpRight.add(vpNotePermission);

		hpShowNote.add(vpLeft);
		hpShowNote.add(vpRight);

		/**
		 * Erstellen oder bearbeiten von Freigaben RPC
		 */
		btnAddNotePermission.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				int permissionID;
				if (rbRead.isChecked()) {
					permissionID = 1;
				} else if (rbWrite.isChecked()) {
					permissionID = 2;
				} else {
					permissionID = 3;
				}
				if (tbNoteShareMail.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
					if (Homepage.getCurrentUser().getMail() != tbNoteShareMail.getText()) {
						notesAdmin.setUserNotePermission(tbNoteShareMail.getText(), permissionID, currentNote.getId(),
								new AsyncCallback<Boolean>() {

									@Override
									public void onSuccess(Boolean result) {
										if (result == true) {
											tbNoteShareMail.setText("");
											tbNoteShareMail.getElement().setPropertyString("placeholder",
													"nutzer@noteit.de");
											rbRead.setValue(true);
											getAllPermittedUsersbyNoteID(currentNote.getId());
										} else {
											Window.alert("Der Nutzer mit der E-Mail `" + tbNoteShareMail.getText()
													+ "` wurde nicht gefunden");
										}
									}

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}
								});
					} else {
						Window.alert("Sie können sich nicht selbst freigeben!");
					}
				} else {
					Window.alert("Bitte geben Sie eine E-Mail-Adresse an!");
					// clUser.setRowData(userList);
				}

			}
		});

		btnSaveNote.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				currentNote = NoteitCellBrowser.getSelectedNote();

				if (tbNoteTitel.getText().length() > 0) {
					Timestamp timestampe;
					if (currentNote.getId() == 0) {
						if (dateBox.getTextBox().getValue().length() > 0) {

							Date date = dateBox.getValue();
							long time = date.getTime();
							timestampe = new Timestamp(time);
						} else {
							timestampe = null;
						}
						notesAdmin.createNote(tbNoteTitel.getText(), tbNoteSubTitel.getText(), content.getText(),
								timestampe, Homepage.getCurrentUser(), Homepage.selectedNotebook.getId(),
								new AsyncCallback<Note>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(Note result) {
										tbNoteSubTitel.setText("");
										tbNoteTitel.setText("");
										content.setText("");
										dateBox.setValue(null);

										NoteitCellBrowser.getNoteList(result);

									}
								});
					} else {
						if (dateBox.getTextBox().getValue().length() > 0) {
							Date date = dateBox.getValue();
							long time = date.getTime();
							timestampe = new Timestamp(time);
						} else {
							timestampe = null;
						}
						
						notesAdmin.updateNote(tbNoteTitel.getText(), tbNoteSubTitel.getText(), content.getText(),
								timestampe, Homepage.getCurrentUser().getId(), currentNote.getNotebookId(),
								currentNote, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(Void result) {
										Timestamp ts = new Timestamp(System.currentTimeMillis());
										Date date = new Date(ts.getTime());
										DateTimeFormat sdfmt = DateTimeFormat.getFormat("dd.MM.yyyy");
										lblNoteDate.setText("Zuletzt bearbeitet am: " + sdfmt.format(date));
										NoteitCellBrowser.updateNotes();

									}
								});
					}
				} else {
					Window.alert("Bitte vergeben Sie einen Titel für die Notiz.");
				}

			}
		});

		btnDeletePermission.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (tbNoteShareMail.getText().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
				final DialogBox dlbQuestion = new DialogBox();

				dlbQuestion.setAnimationEnabled(true);
				dlbQuestion.setText("Sind sie sicher, dass Sie die Freigabe löschen möchten?");
				dlbQuestion.setWidth("300px");
				dlbQuestion.setWidget(vDialog);
				dlbQuestion.setModal(true);
				dlbQuestion.setGlassEnabled(true);
				dlbQuestion.center();

				int width = Window.getClientWidth() / 2;
				int height = Window.getClientHeight() / 2;
				dlbQuestion.setPopupPosition(width, height);
				dlbQuestion.show();

				btnYes.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						notesAdmin.deleteUserNotePermission(tbNoteShareMail.getText(), currentNote.getPermissionID(),
								currentNote.getId(), new AsyncCallback<Void>() {

									@Override
									public void onSuccess(Void result) {
										getAllPermittedUsersbyNoteID(currentNote.getId());

									}

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}
								});
						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

				btnNo.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

			}else{
				Window.alert("Bitte wählen Sie einen verfügbaren Nutzer aus, dessen Freigabe Sie löschen möchten!");
			}
		}
		});

		btnUnsubcribe.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final DialogBox dlbQuestion = new DialogBox();

				dlbQuestion.setAnimationEnabled(true);
				dlbQuestion.setText("Sind sie sicher, dass Sie die ausgewählte Notiz deabonnieren möchten?");
				dlbQuestion.setWidth("300px");
				dlbQuestion.setWidget(vDialog);
				dlbQuestion.setModal(true);
				dlbQuestion.setGlassEnabled(true);
				dlbQuestion.center();

				int width = Window.getClientWidth() / 2;
				int height = Window.getClientHeight() / 2;
				dlbQuestion.setPopupPosition(width, height);
				dlbQuestion.show();

				btnYes.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						// Methode zum löschen der Note aufrufen
						notesAdmin.deleteUserNotePermission(Homepage.getCurrentUser().getMail(),
								currentNote.getPermissionID(),
								currentNote.getId(), new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(Void result) {
										NoteitCellBrowser.updateNotes();
										Homepage.hideView();
									}
								});
						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

				btnNo.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

			}
		});

		btnDeleteNote.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				currentNote = NoteitCellBrowser.getSelectedNote();

				final DialogBox dlbQuestion = new DialogBox();

				dlbQuestion.setAnimationEnabled(true);
				dlbQuestion.setText("Sind sie sicher, dass Sie die ausgewählte Notiz löschen möchten?");
				dlbQuestion.setWidth("300px");
				dlbQuestion.setWidget(vDialog);
				dlbQuestion.setModal(true);
				dlbQuestion.setGlassEnabled(true);
				dlbQuestion.show();
				dlbQuestion.center();

				int width = Window.getClientWidth() / 2;
				int height = Window.getClientHeight() / 2;
				dlbQuestion.setPopupPosition(width, height);
				dlbQuestion.show();

				btnYes.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						// Methode zum löschen der Note aufrufen
						notesAdmin.deleteNote(currentNote.getId(), Homepage.getCurrentUser().getId(),
								new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(Void result) {

										// Noteliste aktualisieren
										NoteitCellBrowser.updateNotes();
										Homepage.hideView();

									}
								});
						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

				btnNo.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {

						// DialogBox ausblenden
						dlbQuestion.hide();

					}
				});

			}
		}

		);

		this.add(hpHeader);
		this.add(hpShowNote);

	}

	public static void showNote(Note note) {
		currentNote = note;
		if(currentNote.getPermissionID() == 0 && currentNotebook.getPermissionID() > 0){
			currentNote.setPermissionID(currentNotebook.getPermissionID());
		}
		
		if(currentNotebook.getId()==-1){
			btnUnsubcribe.setVisible(true);
		}else{
			btnUnsubcribe.setVisible(false);
		}
		if (currentNote.getId() == 0) {
			btnDeleteNote.setVisible(false);
//			btnUnsubcribe.setVisible(false);
			btnSaveNote.setVisible(true);
		} else {

			if (currentNote.getPermissionID() > 0) {
				btnSaveNote.setVisible(true);
//				btnUnsubcribe.setVisible(true);
				btnDeleteNote.setVisible(true);
			} else {
				btnSaveNote.setVisible(true);
//				btnUnsubcribe.setVisible(false);
				btnDeleteNote.setVisible(true);
			}

			if (currentNote.getId() == 0) {
				lblNoteShare.setVisible(false);
				hpAddPermission.setVisible(false);
				lblNoteShareRB.setVisible(false);
				lblNotePermission.setVisible(false);
				vpNotePermission.setVisible(false);
				rbRead.setVisible(false);
				rbWrite.setVisible(false);
				rbDelete.setVisible(false);
			} else {

				lblNoteShare.setVisible(true);
				hpAddPermission.setVisible(true);
				lblNoteShareRB.setVisible(true);
				lblNoteShareRB.setVisible(true);
				lblNotePermission.setVisible(true);
				vpNotePermission.setVisible(true);
				rbRead.setVisible(true);
				rbWrite.setVisible(true);
				rbDelete.setVisible(true);
			}

			if (note.getModificationDate() == null) {
				Timestamp ts = note.getCreationDate();
				Date date = new Date(ts.getTime());
				DateTimeFormat sdfmt = DateTimeFormat.getFormat("dd.MM.yyyy");
				lblNoteDate.setText("Erstellt am: " + sdfmt.format(date));
				lblHeaderTitel.setText(note.getTitle());

			} else {
				Timestamp ts = note.getModificationDate();
				Date date = new Date(ts.getTime());
				DateTimeFormat sdfmt = DateTimeFormat.getFormat("dd.MM.yyyy");
				lblNoteDate.setText("Zuletzt bearbeitet am: " + sdfmt.format(date));
				lblHeaderTitel.setText(note.getTitle());
			}

			if (note.getMaturityDate() == null) {
				dateBox.setValue(null);
			} else {
				dateBox.setValue(currentNote.getMaturityDate());
			}

			if (currentNote.getPermissionID() == 1 && currentNotebook.getPermissionID() == 1
					|| currentNote.getPermissionID() == 1 || currentNote.getPermissionID() == 0 && currentNotebook.getPermissionID() == 1) {
				btnSaveNote.setEnabled(false);
				btnDeleteNote.setEnabled(false);
				btnAddNotePermission.setEnabled(false);
				btnDeletePermission.setEnabled(false);
				lblPermissionInformationWrite.setVisible(false);
				lblPermissionInformationDelete.setVisible(false);
				lblPermissionInformationRead.setVisible(true);
//				btnUnsubcribe.setVisible(true);
				btnAddNotePermission.setHTML("<img src='Images/check_grey.png'/ width=\"10\" height=\"10\">");
				btnDeletePermission.setHTML("<img src='Images/cancle_grey.png'/ width=\"10\" height=\"10\">");
			} else if (currentNote.getPermissionID() == 2 && currentNotebook.getPermissionID() == 2
					|| currentNote.getPermissionID() == 2 || currentNote.getPermissionID() == 0 && currentNotebook.getPermissionID() == 2) {

				btnSaveNote.setEnabled(true);
				btnDeleteNote.setEnabled(false);
				btnAddNotePermission.setEnabled(true);
				btnDeletePermission.setEnabled(true);
				lblPermissionInformationWrite.setVisible(true);
				lblPermissionInformationDelete.setVisible(false);
				lblPermissionInformationRead.setVisible(false);
				btnAddNotePermission.setHTML("<img src='Images/check.png'/ width=\"10\" height=\"10\">");
				btnDeletePermission.setHTML("<img src='Images/cancle.png'/ width=\"10\" height=\"10\">");
			} else {
				btnSaveNote.setEnabled(true);
				btnDeleteNote.setEnabled(true);
				btnAddNotePermission.setEnabled(true);
				btnDeletePermission.setEnabled(true);
				btnAddNotePermission.setHTML("<img src='Images/check.png'/ width=\"10\" height=\"10\">");
				btnDeletePermission.setHTML("<img src='Images/cancle.png'/ width=\"10\" height=\"10\">");

				if (currentNote.getPermissionID() == 3 && currentNotebook.getPermissionID() == 3
						|| currentNote.getPermissionID() == 3 || currentNote.getPermissionID() == 0 && currentNotebook.getPermissionID() == 3) {

					lblPermissionInformationWrite.setVisible(false);
					lblPermissionInformationDelete.setVisible(true);
					lblPermissionInformationRead.setVisible(false);
				} else {

					lblPermissionInformationWrite.setVisible(false);
					lblPermissionInformationDelete.setVisible(false);
					lblPermissionInformationRead.setVisible(false);
				}
			}
			tbNoteTitel.setText(note.getTitle());
			tbNoteSubTitel.setText(note.getSubTitle());
			content.setText(note.getText());

		}
	}

	public static void getAllPermittedUsersbyNoteID(int noteID) {

		notesAdmin.getAllPermittedUsersByNoteID(noteID, new AsyncCallback<ArrayList<User>>() {

			@Override
			public void onSuccess(ArrayList<User> result) {
				// TODO Auto-generated method stub
				userList = result;
				clUser.setRowData(result);
				if (result.size() < 1) {
					lblNotePermission.setText("Diese Notiz wurde niemandem freigegeben");
				} else if (result.size() == 1) {
					lblNotePermission.setText("Freigegeben an folgenden Nutzer:");
				} else {
					lblNotePermission.setText("Freigegeben an folgende " + result.size() + " Nutzer:");
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void setSelectedUserPermissionInTextbox(User user) {
		tbNoteShareMail.setText(user.getMail());
		tbNoteShareMail.getElement().setPropertyString("placeholder", "");
		if (user.getPermissionID() == 1) {
			rbRead.setValue(true);
		} else if (user.getPermissionID() == 2) {
			rbWrite.setValue(true);
		} else {
			rbDelete.setValue(true);
		}

	};

	public static void setCurrentNotebook(Notebook notebook) {
		currentNotebook = notebook;
	}

	public static void setPermissionPanelInvisible() {
		lblNoteShare.setVisible(false);
		hpAddPermission.setVisible(false);
		lblNoteShareRB.setVisible(false);
		lblNoteShareRB.setVisible(false);
		lblNotePermission.setVisible(false);
		vpNotePermission.setVisible(false);
		rbRead.setVisible(false);
		rbWrite.setVisible(false);
		rbDelete.setVisible(false);
	}

}
