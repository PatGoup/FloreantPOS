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
import java.util.Date;


import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.text.SimpleDateFormat;

import java.text.ParseException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.BookingCustomerInfo;
import com.floreantpos.model.dao.TableBookingInfoDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.DateKeyPad;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.TicketAlreadyExistsException;
import com.floreantpos.ui.views.BookingView;
import com.floreantpos.IconFactory;
import com.floreantpos.ui.views.booking.BookingTable;
import com.floreantpos.ui.views.booking.BookingCustomerTable;
import com.floreantpos.ui.views.booking.BookingListTableModel;
import com.floreantpos.swing.PaginatedTableModel;
import com.floreantpos.swing.FixedLengthTextField;



public class BookingListView extends JPanel 
{

    private BookingView bookingView = null;
    private BookingForm bookingForm =null;
	private PosButton addButton;
	private BookingCustomerTable bookingTable;
	private DefaultTableModel tableModel;
	private JTable tableBookings;
    private ArrayList bookingArray;
    private POSTextField tfMobile;
	private POSTextField tfLoyaltyNo;
	private POSTextField tfName;
	private PosButton btnInfo;
    private String [] names;
    protected BookingCustomerInfo selectedBooking;
    private PosButton btnEditBooking;
	private com.floreantpos.swing.FixedLengthTextField tfStartDate;
	private Ticket ticket;
	private PosButton btnCancel;
	private DateKeyPad dateKeyPad;
	private PosButton btnNext;
	private PosButton btnPrevious;
	private JLabel lblNumberOfItem;
	private BookingListTableModel bookingListTableModel;

	public BookingListView (BookingView bookingView) 
	{
        this.bookingView = bookingView;
        
		initUI();
		
		loadBookings ();

	}


	public void initUI() 
	{
        setLayout (new BorderLayout());
        
        JPanel searchPanel =  buildSearchPanel ();
        add (searchPanel, BorderLayout.NORTH);
        
		names = new String []  {Messages.getString("BookingListTableModel.0"),        Messages.getString("BookingListTableModel.1"),        Messages.getString("BookingListTableModel.2"),        Messages.getString("BookingListTableModel.3"),        Messages.getString("BookingListTableModel.4"),        Messages.getString("BookingListTableModel.5"),        Messages.getString("BookingListTableModel.6"),        Messages.getString("BookingListTableModel.7"),        Messages.getString("BookingListTableModel.8"),        Messages.getString("BookingListTableModel.9")};

        tableModel = new DefaultTableModel(names, 0);
        tableBookings = new JTable(tableModel);        
        buildTablePanel ();
        
        PosScrollPane scrollPane = new PosScrollPane();
		scrollPane.setFocusable(false);


		scrollPane.setViewportView  (bookingTable);
        
        add (scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel =  buildButtonPanel ();
        add (buttonPanel, BorderLayout.SOUTH);
        
	}

    
    private JPanel buildSearchPanel ()
    {
    	JPanel searchPanel = new JPanel ();

        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder title = BorderFactory.createTitledBorder(loweredetched, Messages.getString ("BookingListView.0") ); 
        
        title.setTitleJustification(TitledBorder.LEFT);
        searchPanel.setBorder(title);

		JLabel lblStartDate = new JLabel(Messages.getString("BookingListView.1")); 
        searchPanel.add (lblStartDate);

        tfStartDate = new com.floreantpos.swing.FixedLengthTextField (10);;        
        String start = LocalDate.now  ().toString();
        start = start.split(" ")[0];
        tfStartDate.setText (start);
        searchPanel.add(tfStartDate);
        
		PosButton btnSearch = new PosButton(Messages.getString("BookingListView.2")); 
        searchPanel.add(btnSearch, "");
        btnSearch.addActionListener(new ActionListener() 
        {
            public void actionPerformed (ActionEvent e) 
            {
                searchFromDate();
            }
        });
   
        // add the action listener here

		PosButton btnKeyboard = new PosButton(IconFactory.getIcon("/images/", "keyboard.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnKeyboard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dateKeyPad.setCollapsed(!dateKeyPad.isCollapsed());
                if (dateKeyPad.isVisible ())
                    tfStartDate.requestFocus ();
			}
		});
        searchPanel.add (btnKeyboard);

		dateKeyPad = new com.floreantpos.swing.DateKeyPad();
		dateKeyPad.setCollapsed(true);
        searchPanel.add(dateKeyPad);           
   
		return searchPanel;
    }
    
    private JPanel buildTablePanel ()
    {
        JPanel centerPanel = new JPanel();
        
		setBorder(new TitledBorder(null, "Current Reservations", TitledBorder.CENTER, TitledBorder.TOP, null, null)); //$NON-NLS-1$

        bookingTable = new BookingCustomerTable();
        bookingListTableModel = new BookingListTableModel();
		bookingListTableModel.setPageSize(2000);
		bookingTable.setModel(bookingListTableModel);
		bookingTable.setFocusable(false);
		bookingTable.setRowHeight(30);
		bookingTable.getTableHeader().setPreferredSize(new Dimension(100, 35));
		bookingTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookingTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedBooking = bookingTable.getSelectedBooking();
			}
		});
		
        centerPanel.add (bookingTable);
	
        return centerPanel;
    }

    
    private JPanel buildButtonPanel ()
    {
        JPanel buttonPanel = new JPanel ();
        
		JPanel panel = new JPanel(new MigLayout("fill", "[][center, grow][]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		buttonPanel.add (panel);
		
		addButton = new PosButton(Messages.getString("CustomerSelectionDialog.25")); 
        addButton.setFocusable(false);
		panel.add(addButton, ""); 

		addButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
                doAddBooking ();		
            }
		});
		
		
		btnEditBooking = new PosButton(Messages.getString("BookingListView.3")); 
		panel.add(btnEditBooking, ""); 

		btnEditBooking.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
                doEditSelectedBooking ();
            }
		});		
		
        return buttonPanel;
    }

	private void doAddBooking ()
	{
        bookingView.newBookingListView ();
    }
	
	private void doEditSelectedBooking ()
	{
        int index = bookingTable.getSelectedRow();
		if (index < 0)
		{
            POSMessageDialog.showMessage(Messages.getString ("BookingListView.4"));
            return;
        }
    
        bookingView.editBookingListView (bookingTable);
	}
	
	private void searchFromDate ()
	{
        loadBookings ();
	}

	public void loadBookings ()
	{
	
        String startDate = tfStartDate.getText();
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        Date date = null;
        try 
        {
            date = formatter.parse (startDate);
        } catch (ParseException e) 
        { };        

        tableModel = new DefaultTableModel();  
        tableBookings = new JTable(tableModel);         
        
        getAllBookings (bookingTable, startDate);
        
        int width = bookingTable.getColumnModel().getColumn(0).getWidth ();
        
        int halfWidth = width / 2;
        bookingTable.getColumn(0).setPreferredWidth (halfWidth);
        bookingTable.getColumnModel().getColumn(5).setWidth (halfWidth);
        // .setPreferredWidth(100);
        
        bookingTable.revalidate ();
        bookingTable.repaint ();
	}	
    

	private void getAllBookings (JTable table, String  startDate) 	
    {
        Session session = null;
        GenericDAO dao = new GenericDAO();
        ResultSet rs = null;
        // Create the table model
        DefaultTableModel tableModel;

        try
        {
            session = dao.createNewSession();
            Connection connection =     session.connection();
		
            try 
            {
                String strSelect = "select ID, FROM_DATE, STATUS, TO_DATE, GUEST_COUNT, AUTO_ID, name, HOMEPHONE_NO,  MOBILE_NO, CITY from TABLE_BOOKING_INFO, CUSTOMER where customer_id = AUTO_ID and FROM_DATE >=  DATE ('" + startDate + "') order by FROM_DATE";
    
                Statement stmt = connection.createStatement ();
                Integer i = 0;
                rs = 
                stmt.executeQuery  (strSelect);
        
             
                if (rs != null)
                {
                    try 
                    {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                      
                        // Add rows to the table model
  
                        tableModel = new DefaultTableModel(names, 0);
                        table.setModel (tableModel);

                        while (rs.next()) 
                        {
                            Object[] row = new Object[columnCount];
                            
                            for (i = 1; i <= columnCount; i++)                                                                 
                            {
                                row[i - 1] = rs.getObject(i);
                            }
                            tableModel.addRow(row);
                        }
                    } catch (Exception e) 
                    {
                        e.printStackTrace();
                    }
                }
             } catch (Exception e) 
            {
                PosLog.error(getClass(), e);
            }
		}
        finally 
        {
            dao.closeSession(session);
        }
 
         int width = bookingTable.getColumnModel().getColumn(0).getWidth ();
        
        int halfWidth = width / 2;
        bookingTable.getColumn(0).setPreferredWidth (halfWidth);
        bookingTable.getColumn(1).setPreferredWidth (width + halfWidth);
        bookingTable.getColumn(3).setPreferredWidth (width + width);
        bookingTable.getColumn(4).setPreferredWidth (halfWidth);
        bookingTable.getColumn(5).setPreferredWidth (halfWidth); 
 
		return; 
    }

	@Override
	public String getName() {
		return "C"; //$NON-NLS-1$
	}

	public void updateView(boolean update) 
	{

	}

}
