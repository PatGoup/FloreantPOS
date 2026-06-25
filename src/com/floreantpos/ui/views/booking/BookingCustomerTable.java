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

//??? import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.BookingCustomerInfo;



public class BookingCustomerTable extends JXTable {

	public BookingCustomerTable() {
	}

	public BookingCustomerTable (TableModel dm) {
		super(dm);
	}

	public BookingCustomerTable (TableModel dm, TableColumnModel cm) {
		super(dm, cm);
	}

	public BookingCustomerTable (int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	public BookingCustomerTable (Vector<?> rowData, Vector<?> columnNames) {
		super(rowData, columnNames);
	}

	public BookingCustomerTable (Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
	}

	public BookingCustomerTable (TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
	}
	
	//???  public TableBookingInfo  
	// create the BookingCustomerInfo class
	public BookingCustomerInfo 
	getSelectedBooking () 
	{
		TableModel model = getModel();
		if(model instanceof BookingListTableModel) {
			if(getSelectedRow()== -1) {
				return null;
			}
			//??? return (TableBookingInfo)
			// update BookingListTableModel
			return (BookingCustomerInfo) 
			((BookingListTableModel) model).getRowData(getSelectedRow());
		}
		
		return null;
	}

}
