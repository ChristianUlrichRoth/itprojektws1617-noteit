package de.hdm.itprojekt.noteit.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import de.hdm.itprojekt.noteit.shared.bo.*;

public class UserCell extends AbstractCell<User> {

	public void render(Context context, User value, SafeHtmlBuilder sb) {

		if (value == null) {
			return;
		}

		sb.appendHtmlConstant("<div>");
		sb.appendEscaped(value.getFirstName() + value.getLastName()
				+ value.getMail() + " ");
		sb.appendHtmlConstant("</div>");
		sb.appendHtmlConstant(
				"<div style=\"border-bottom: 4px solid #dddddd;\">");
		// sb.appendHtmlConstant("<email style='font-size:80%; padding-left:
		// 10px;'>");
		// sb.appendEscaped(value.getEmail());
		// sb.appendHtmlConstant("</email>");
		sb.appendHtmlConstant("</div>");

	}

}
