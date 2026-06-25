/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.views.booking;

import java.util.List;

import com.floreantpos.Messages;
import com.floreantpos.model.BookingCustomerInfo;
import com.floreantpos.swing.PaginatedTableModel;

public class BookingListTableModel extends PaginatedTableModel {

	private final static String[] columns = {
        Messages.getString("BookingListTableModel.0"), Messages.getString("BookingListTableModel.1"), Messages.getString("BookingListTableModel.2"),			Messages.getString("BookingListTableModel.3"), Messages.getString("BookingListTableModel.4"), Messages.getString("BookingListTableModel.5"), Messages.getString("BookingListTableModel.6"), Messages.getString("BookingListTableModel.7"), Messages.getString("BookingListTableModel.8"), Messages.getString("BookingListTableModel.97") }; 
	
	public BookingListTableModel() {
		super(columns);
	}

	public BookingListTableModel(List<BookingCustomerInfo> bookings) {
		super(columns, bookings);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		BookingCustomerInfo booking = (BookingCustomerInfo) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return booking.getBookingId();
            case 1:
                {
                    String s = booking.getFromDate ().toString();
                    s.replace ("T"," ");
                    return s;
                }
			case 2:
				return  booking.getStatus();
			case 3:
				return booking.getToDate();
			case 4:
				return booking.getGuestCount();
			case 5:
				return "5";  //??? booking.getCustomerId();
			case 6:
				return booking.getName();
			case 7:
				return  booking.getHomePhone();
			case 8:
				return booking.getCellPhone();
			case 9:
				return booking.getCity();

		}
		return null;
	}
}
