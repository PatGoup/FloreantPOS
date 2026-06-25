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

import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.model.TableBookingInfo;

public class BookingTable extends JXTable {

	public BookingTable() {
	}

	public BookingTable(TableModel dm) {
		super(dm);
	}

	public BookingTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
	}

	public BookingTable(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	public BookingTable(Vector<?> rowData, Vector<?> columnNames) {
		super(rowData, columnNames);
	}

	public BookingTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
	}

	public BookingTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
	}
	
	public TableBookingInfo  getSelectedBooking () 
	{
		TableModel model = getModel();
		if(model instanceof BookingListTableModel) {
			if(getSelectedRow()== -1) {
				return null;
			}
			return (TableBookingInfo) ((BookingListTableModel) model).getRowData(getSelectedRow());
		}
		
		return null;
	}

}
