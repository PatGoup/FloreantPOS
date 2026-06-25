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
/*
 * SwitchboardView.java
 *
 * Created on August 14, 2006, 11:45 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

//import com.floreantpos.customer.CustomerSelector;
import com.floreantpos.customer.DefaultCustomerListView;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.model.OrderType;
import com.floreantpos.ui.views.order.ViewPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;

import com.floreantpos.ui.views.booking.BookingSelector;
import com.floreantpos.ui.views.booking.BookingListView;
import com.floreantpos.ui.views.booking.BookingForm;
import com.floreantpos.ui.views.booking.BookingCustomerListView;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.customer.CustomerTable;
import com.floreantpos.customer.CustomerListTableModel;

/**
 * 
 * @author MShahriar
 */
public class BookingView extends ViewPanel {

    public final static String VIEW_NAME = "RESERVATION_ACTIVITY"; //$NON-NLS-1$

	private static BookingView bookingView = null;
    private static BookingListView bookingListView = null;
	private static BookingForm bookingForm = null;
	private static BookingCustomerListView  bookingCustomerListView = null;
///	private static BookingCustomerForm bookingCustomerForm = null;
	
	private BookingView(OrderType orderType) {
		setLayout(new BorderLayout());

        bookingListView = new BookingListView (this);

		bookingListView.updateView (false);
		add (bookingListView, BorderLayout.CENTER);

        applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
	}

	public void updateView() {
	//???	bookingSelector.renderBookings();
	}

	public static BookingView getInstance(OrderType orderType) {
		if (bookingView == null) {
			bookingView = new BookingView (orderType);
		}

		return bookingView;
	}

	// bookingListView = new button
	public void newBookingListView ()
	{
        if (bookingForm == null)
        {
            bookingForm = new BookingForm (this);
        }

        this.remove (0);
        
        bookingForm.initNew ();
        
        this.add (bookingForm, BorderLayout.CENTER);
        bookingForm.revalidate ();
        bookingForm.repaint ();        
	}
	
	// bookingListView = edit button
	public void editBookingListView (JTable bookingTable)
	{
        if (bookingForm == null)
        {
            bookingForm = new BookingForm (this);
        }
        
        bookingForm.initEdit (bookingTable);
        
        this.remove (0);
        
        this.add (bookingForm, BorderLayout.CENTER);
        bookingForm.revalidate ();
        bookingForm.repaint ();        
	}
	

	public void selectCustomerBookingForm ()
	{
        if (bookingCustomerListView == null)
        {
            bookingCustomerListView = new BookingCustomerListView (this);
        }

        bookingCustomerListView.loadBookingCustomers ();

        bookingView.remove (0);
        bookingView.add (bookingCustomerListView, BorderLayout.CENTER);
        bookingCustomerListView.revalidate ();
        bookingCustomerListView.repaint ();
 
 
    }

	public void addUpdateBookingForm (String mode)
	{
        remove (0);
        add (bookingListView, BorderLayout.CENTER);
        bookingListView.loadBookings ();
        bookingListView.revalidate ();
        bookingListView.repaint ();        
	}
    
    
	public void cancelBookingForm ()
	{
        remove (0);
        add (bookingListView, BorderLayout.CENTER);
        bookingListView.revalidate ();
        bookingListView.repaint ();
    }
	
	public void selectBookingCustomerListView (CustomerTable customerTable)
	{
        remove (0);
        add (bookingForm, BorderLayout.CENTER);
        bookingForm.setCustomerFields (customerTable);
        bookingForm.revalidate ();
        bookingForm.repaint ();	
	}
		
	public void cancelBookingCustomerListView ()
	{
        remove (0);
        add (bookingForm, BorderLayout.CENTER);
        bookingForm.revalidate ();
        bookingForm.repaint ();	
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}
