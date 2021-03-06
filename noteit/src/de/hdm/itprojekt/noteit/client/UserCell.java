package de.hdm.itprojekt.noteit.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.itprojekt.noteit.shared.bo.*;

public class UserCell extends AbstractCell<User> {
	String permission;

	public void render(Context context, User value, SafeHtmlBuilder sb) {

		if (value == null) {
			return;
		}
		if (value.getPermissionID() == 1) {
			permission = "lesen";
		} else if (value.getPermissionID() == 2) {
			permission = "bearbeiten";
		} else {
			permission = "bearbeiten & löschen";
		}

		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getMail() + ", " + permission);
		sb.appendHtmlConstant("</div>");
		sb.appendHtmlConstant("</div>");

	}

}
