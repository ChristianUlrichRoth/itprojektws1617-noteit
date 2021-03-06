package de.hdm.itprojekt.noteit.shared;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.itprojekt.noteit.client.NotesGeneralInformationReport;
import de.hdm.itprojekt.noteit.client.NotesSharingInformationReport;
import de.hdm.itprojekt.noteit.shared.bo.User;
import de.hdm.itprojekt.noteit.shared.report.NotesGeneralInformation;
import de.hdm.itprojekt.noteit.shared.report.NotesSharingInformation;
import de.hdm.itprojekt.noteit.shared.report.ReportSimple;

public interface ReportServiceAsync {

	void init(AsyncCallback<Void> callback);

	void createReportNotesGeneralInformation(User u, String sKeywordNote, String sKeywordNotebook, Date fromMaturity, Date toMaturity,
			Date fromCreationDate, Date toCreationDate, Date fromModificationDate, Date toModificationdate, AsyncCallback<NotesGeneralInformation> callback);	
	
	void createReportNotesSharingInformation(User u, int permission, AsyncCallback<NotesSharingInformation> callback);
	
}
