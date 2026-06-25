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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.QuickCustomerForm;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.TicketAlreadyExistsException;
import com.floreantpos.customer.CustomerTable;
import com.floreantpos.customer.CustomerListTableModel;
import com.floreantpos.ui.views.BookingView;
import com.floreantpos.ui.views.booking.BookingForm;


public class BookingCustomerListView 
extends JPanel 
{
    private static BookingView bookingView;
    private BookingForm bookingForm;
    private JPanel pnlMain;
	private PosButton btnCreateNewCustomer;
	private CustomerTable customerTable;
	private POSTextField tfMobile;
	private POSTextField tfHomeNo;
	private POSTextField tfName;
	private PosButton btnInfo;
	protected Customer selectedCustomer;
	private PosButton btnRemoveCustomer;

	private Ticket ticket;
	private PosButton btnCancel;
	private QwertyKeyPad qwertyKeyPad;
	private PosButton btnNext;
	private PosButton btnPrevious;
	private CustomerListTableModel customerListTableModel;
	private JLabel lblNumberOfItem;

	public BookingCustomerListView (BookingView bookingview)
	{	
        this.bookingView = bookingView;
        initUI();
       
	}

    public void loadBookingCustomers ()
    {
        doSearchCustomer ();
    }
	
	/***********************8
	public void BookingCustomerListView (Ticket ticket) {
		this.ticket = ticket;
		initUI();
//////		loadCustomerFromTicket();
	}
*************************/
	public void initUI() 
	{

        setLayout (new BorderLayout ());

        pnlMain = new JPanel (new BorderLayout ());

        // border north panel
		JPanel searchPanel = new JPanel(new MigLayout());

		
		JLabel lblByName = new JLabel(Messages.getString("BookingCustomerListView.1")); 
		JLabel lblByHome = new JLabel(Messages.getString("BookingCustomerListView.16"));
		JLabel lblByPhone = new JLabel(Messages.getString("BookingCustomerListView.19"));   
		
		tfName = new POSTextField(16);
		tfHomeNo = new POSTextField(16);
		tfMobile = new POSTextField(16);

		tfName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomerByIndex();
			}
		});
		tfHomeNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomerByIndex();
			}
		});
		tfMobile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomerByIndex();
			}
		});

		PosButton btnSearch = new PosButton(Messages.getString("BookingCustomerListView.15")); //$NON-NLS-1$
		btnSearch.setFocusable(false);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearchCustomerByIndex();
			}
		});

		PosButton btnKeyboard = new PosButton(IconFactory.getIcon("/images/", "keyboard.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnKeyboard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				qwertyKeyPad.setCollapsed(!qwertyKeyPad.isCollapsed());
                if (qwertyKeyPad.isVisible ())
                    tfName.requestFocus ();
			}
		});

		searchPanel.add(lblByName, "growy"); //$NON-NLS-1$
		searchPanel.add(tfName, "growy"); //$NON-NLS-1$		
		searchPanel.add(lblByHome, "growy"); //$NON-NLS-1$
		searchPanel.add(tfHomeNo, "growy"); //$NON-NLS-1$		
		
		searchPanel.add(lblByPhone, "growy"); //$NON-NLS-1$
		searchPanel.add(tfMobile, "growy"); //$NON-NLS-1$


		searchPanel.add(btnKeyboard, "growy,w " + PosUIManager.getSize(80) + "!,h " + PosUIManager.getSize(35) + "!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		searchPanel.add(btnSearch, ",growy,h " + PosUIManager.getSize(35) + "!"); //$NON-NLS-1$ //$NON-NLS-2$

		pnlMain.add (searchPanel, BorderLayout.NORTH);    //, "cell 0 1"); //$NON-NLS-1$

		// center panel
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder (new TitledBorder(null, POSConstants.SELECT_CUSTOMER.toUpperCase(), TitledBorder.CENTER, TitledBorder.TOP, null, null)); //$NON-NLS-1$

		JPanel customerListPanel = new JPanel(new BorderLayout(0, 0));
		customerListPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

		customerTable = new CustomerTable();
		customerListTableModel = new CustomerListTableModel();
		customerListTableModel.setPageSize(20);
		customerTable.setModel(customerListTableModel);
		customerTable.setFocusable(false);
		customerTable.setRowHeight(30);
		customerTable.getTableHeader().setPreferredSize(new Dimension(100, 35));
		customerTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
        /*****************
        customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedCustomer = customerTable.getSelectedCustomer();
				if (selectedCustomer != null) {
					//btnInfo.setEnabled(true);
				}
				else {
					btnInfo.setEnabled(false);
				}
			}
		});
		***********************/
		
		PosScrollPane scrollPane = new PosScrollPane();
		scrollPane.setFocusable(false);
		scrollPane.setViewportView(customerTable);

		customerListPanel.add(scrollPane, BorderLayout.CENTER);

		// border layout south
        JPanel panel = new JPanel(new  MigLayout ("center"));  
				
		btnCreateNewCustomer = new PosButton(Messages.getString("BookingCustomerListView.25")); //$NON-NLS-1$
		btnCreateNewCustomer.setFocusable(false);
		panel.add(btnCreateNewCustomer, ""); //$NON-NLS-1$
		btnCreateNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCreateNewCustomer();
			}
		});

		PosButton btnEdit = new PosButton(Messages.getString("BookingCustomerListView.22"));
		// add action listener ??????
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doEditCustomer();
			}
		});
		panel.add (btnEdit, "");
		
		PosButton btnSelect = new PosButton(Messages.getString("BookingCustomerListView.28")); //$NON-NLS-1$
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
                doSelectCustomer ();
			}
		});
		panel.add(btnSelect, ""); //$NON-NLS-1$

		btnCancel = new PosButton(Messages.getString("BookingCustomerListView.29")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
                bookingView = BookingView.getInstance (null);
                bookingView.cancelBookingCustomerListView ();
			}
		});
		panel.add(btnCancel, ""); //$NON-NLS-1$

		customerListPanel.add(panel, BorderLayout.SOUTH);
		
		centerPanel.add(customerListPanel, BorderLayout.CENTER); //$NON-NLS-1$

		pnlMain.add (centerPanel, BorderLayout.CENTER);  //, "cell 0 2,grow"); //$NON-NLS-1$

		// border south panel
		qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		qwertyKeyPad.setCollapsed(false);
		pnlMain.add (qwertyKeyPad, BorderLayout.SOUTH);  //, "cell 0 3,grow"); //$NON-NLS-1$

		add (pnlMain, BorderLayout.CENTER);
	}

	private void doSelectCustomer ()
	{
        int index = customerTable.getSelectedRow();
		if (index < 0)
		{
            POSMessageDialog.showMessage(Messages.getString ("BookingListView.4"));
            return;
        }
    
        bookingView = BookingView.getInstance (null);
        bookingView.selectBookingCustomerListView (customerTable);
	}

	protected void doSearchCustomer() 
	{
		try 
		{
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			qwertyKeyPad.setCollapsed(true);
			String mobile = tfMobile.getText();
			String name = tfName.getText();
			String homePh = tfHomeNo.getText();

			if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(homePh) && StringUtils.isEmpty(name)) 
			{
                CustomerDAO.getInstance().getNumberOfCustomers();
				CustomerDAO.getInstance().loadCustomers(customerListTableModel);
			}
			else 
			{
         ///////       customerListTableModel.setNumRows(CustomerDAO.getInstance().getNumberOfCustomers(mobile, homePh, name));
				CustomerDAO.getInstance().findBookingCustomer (name, homePh, mobile, customerListTableModel);
			}
        
            customerListTableModel.fireTableDataChanged();
			customerTable.repaint();
        } catch (Exception e) {
			PosLog.error(BookingCustomerListView.class, e);
			e.printStackTrace();
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

    private void doSearchCustomerByIndex ()
    {
        doSearchCustomer ();
    }

	protected void doCreateNewCustomer() {
		boolean setKeyPad = true;

		QuickCustomerForm form = new QuickCustomerForm(setKeyPad);

		//TODO: handle exception

		form.enableCustomerFields(true);
		BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), form);
		dialog.setResizable(false);
		
		dialog.open();

		if (dialog.isCanceled()) 
            return;
            
        selectedCustomer = (Customer) form.getBean();

		//	CustomerListTableModel model = (CustomerListTableModel) customerTable.getModel();
		//	model.addItem(selectedCustomer);
		doSearchCustomer ();
	}


	protected void doEditCustomer() {
        selectedCustomer = customerTable.getSelectedCustomer();
				
        if (selectedCustomer == null) 
        {
            POSMessageDialog.showMessage ("Please select a customer to edit");
            return;
        }
            
		boolean setKeyPad = true;

		QuickCustomerForm form = new QuickCustomerForm(setKeyPad);

		//TODO: handle exception

		form.enableCustomerFields(true);
		BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), form);
		dialog.setResizable(false);
		dialog.setBean (selectedCustomer);
		
		dialog.open();

		if (dialog.isCanceled()) 
            return;

		//	CustomerListTableModel model = (CustomerListTableModel) customerTable.getModel();
		//	model.addItem(selectedCustomer);
		doSearchCustomer ();
	}

	
	@Override
	public String getName() {
		return "C"; //$NON-NLS-1$
	}

}
