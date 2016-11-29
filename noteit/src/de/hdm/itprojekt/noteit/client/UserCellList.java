package de.hdm.itprojekt.noteit.client;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import de.hdm.itprojekt.noteit.shared.bo.*;

public class UserCellList extends Widget {

	private User SelectedUser;

	CellList<User> userCellList = null;

	public CellList<User> createUserCellList() {

		// Create a KeyProvider.
		ProvidesKey<User> noteKeyProvider = new ProvidesKey<User>() {
			public Object getKey(User item) {
				return (item == null) ? null : item.getFirstName() + item.getLastName() + item.getMail();
			}
		};

		// Create a cell to render each value.
		UserCell noteCell = new UserCell();

		// Use the cell in a CellList.
		userCellList = new CellList<User>(userCell, userKeyProvider);

		// Set the width of the CellList.
		// noteCellList.setWidth("230px");

		// Stylen der CellList
		userCellList.setStylePrimaryName("CellList");

		// Set a key provider that provides a unique key for each contact. If
		// key is
		// used to identify contacts when fields (such as the name and address)
		// change.
		userCellList.setPageSize(30);
		userCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		userCellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

		// Add a selection model so we can select cells.
		final SingleSelectionModel<User> userSelectionModel = new SingleSelectionModel<User>(
				noteKeyProvider);
		userCellList.setSelectionModel(userSelectionModel);
		userSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				// contactForm.setContact(selectionModel.getSelectedObject());
				Window.alert("Du hast gewählt: " + userSelectionModel.getSelectedObject().getFirstName() + userSelectionModel.getSelectedObject().getLastName() + userSelectionModel.getSelectedObject().getMail());
				SelectedUser = userSelectionModel.getSelectedObject();
			}
		});

		return userCellList;

	}

	public User getSelectedUser() {

		return SelectedUser;

	}

	
}