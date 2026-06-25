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
 * FoodGroupEditor.java
 *
 * Created on August 2, 2006, 8:55 PM
 */

package com.floreantpos.ui.views.booking;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.hibernate.Transaction;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;


import net.miginfocom.swing.MigLayout;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.TableBookingInfoDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.views.CustomerBookingView;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.PosLog;
import com.floreantpos.model.OrderType;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.forms.CustomerBookingForm;
import com.floreantpos.ui.views.booking.BookingListView;
import com.floreantpos.customer.DefaultCustomerListView;
import com.floreantpos.customer.CustomerSelectorFactory;
import com.floreantpos.model.Customer;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.customer.CustomerTable;
import com.floreantpos.customer.CustomerListTableModel;
import com.floreantpos.ui.views.CustomerView;
import com.floreantpos.ui.views.IView;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.SwitchboardOtherFunctionsView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.model.dao.TableBookingInfoDAO;
import com.floreantpos.ui.forms.QuickCustomerForm;

import com.floreantpos.ui.views.BookingView;
import com.floreantpos.ui.views.booking.BookingCustomerListView;
import com.floreantpos.ui.views.booking.BookingSelector;



/*
 *
 * @author  MShahriar
 */
public class BookingForm extends JPanel 
{
    private static BookingView bookingView;
    private BookingListView bookingListView;
    private BookingSelector bookingSelector;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnNewCategory;
	private javax.swing.JComboBox cbCategory;
	private PosButton btnOK;
	private JLabel lblBId;
	private JLabel tfBId;
	private JLabel lblFromDate;
	private com.floreantpos.swing.FixedLengthTextField tfFromDate;
    private JLabel lblGuests;
    private com.floreantpos.swing.FixedLengthTextField tfGuests;
    private JLabel lblStatus;
    private String [] status = {"0","1","2","3"};
    private com.floreantpos.swing.FixedLengthTextField tfStatus;
    private String currentStatus;
	private javax.swing.JCheckBox c0;
	private javax.swing.JCheckBox c1;
	private javax.swing.JCheckBox c2;
	private javax.swing.JCheckBox c3;
    private JPanel pnlBooking;
    private JLabel lblCId;
    private JLabel tfCId;
    private JLabel lblName;

	private JLabel tfName;
    private JLabel lblHomePhoneNo;
    private JLabel tfHomePhoneNo;
    private JLabel lblCellPhoneNo;
    private JLabel tfCellPhoneNo;    
    private JLabel lblCity;
    private JLabel tfCity;    
    private JTable bookingTable;
    private TableBookingInfoDAO bookingInfo;
    private String errorMessage;
	
	private JLabel lblTranslatedName;
	private FixedLengthTextField tfTranslatedName;
	private JLabel lblSortOrder;
	private JLabel lblButtonColor;
	private IntegerTextField tfSortOrder;
	private JButton btnButtonColor;
	private JLabel lblTextColor;
	private JButton btnTextColor;

	private String mode = null;
	private OrderType orderType;
	private GridBagConstraints c;
	public BookingListView parent = null;
	
	
	/** Creates new form FoodGroupEditor */
	public BookingForm (BookingView bookingView) 
	{
        this.bookingView = bookingView;
        initComponents();
	}

	public void initNew () 
	{
        mode = POSConstants.ADD_MODE;
             
        initBookingValues ();
        
        btnOK.setText (Messages.getString("BookingForm.24"));     
	}

	public void initEdit (JTable bookingTable) 
	{
        mode = POSConstants.UPDATE_MODE;
        this.bookingTable = bookingTable;
     
        setBookingValues ();
        
        btnOK.setText (Messages.getString("BookingForm.25"));  
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */

	private void initComponents() 
	{
       setLayout (new BorderLayout ());
        
        JPanel pnlMain = new JPanel (new GridBagLayout ());
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 0, 0, 0);

        pnlBooking = new JPanel ();
		pnlBooking.setLayout(new MigLayout("", "[70px][289px,grow][6px][49px]", "[19px][][25px][][][][15px]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		pnlBooking.setPreferredSize(new Dimension(600, 260));
        c.gridx = 0;
		c.gridy = 0;
		pnlMain.add (pnlBooking, c);
		
   
		lblBId = new javax.swing.JLabel();
        lblBId.setText (Messages.getString ("BookingForm.4"));
        pnlBooking.add(lblBId, "cell 0 0,alignx left, aligny center"); //$NON-NLS-1$
		tfBId = new JLabel ();
		pnlBooking.add(tfBId, "cell 1 0,alignx left, aligny center"); 
		
		lblFromDate = new javax.swing.JLabel();		
        lblFromDate.setText (Messages.getString ("BookingForm.5"));
		pnlBooking.add(lblFromDate, "cell 0 1,alignx left,aligny center"); //$NON-NLS-1$
		tfFromDate = new com.floreantpos.swing.FixedLengthTextField(20);
		tfFromDate.setText ("");
		pnlBooking.add(tfFromDate, "cell 1 1,alignx left, aligny center"); //$NON-NLS-1$
	
	
		lblStatus = new javax.swing.JLabel();
        lblStatus.setText (Messages.getString ("BookingForm.7"));
        pnlBooking.add(lblStatus, "cell 0 2,alignx left, aligny center"); //$NON-NLS-1$
        status [0] = Messages.getString ("BookingForm.19");
        status [1] = Messages.getString ("BookingForm.20");
        status [2] = Messages.getString ("BookingForm.21");
        status [3] = Messages.getString ("BookingForm.22");
        pnlBooking.add (createStatusBox (), "cell 1 2,alignx left, aligny center"); //$NON-NLS-1$

		lblGuests = new javax.swing.JLabel();
        lblGuests.setText (Messages.getString ("BookingForm.6"));
        pnlBooking.add(lblGuests, "cell 0 3,alignx left, aligny center"); //$NON-NLS-1$
		tfGuests = new com.floreantpos.swing.FixedLengthTextField(3);
		pnlBooking.add(tfGuests, "cell 1 3,alignx left, aligny center"); //$NON-NLS-1$

		lblCId = new javax.swing.JLabel();
        lblCId.setText (Messages.getString ("BookingForm.8"));
        pnlBooking.add(lblCId, "cell 0 4,alignx left, aligny center"); //$NON-NLS-1$
		tfCId = new JLabel ();
		pnlBooking.add(tfCId, "cell 1 4,alignx left, aligny center"); //$NON-NLS-1$
	
		lblName = new javax.swing.JLabel();
        lblName.setText (Messages.getString ("BookingForm.9"));
        pnlBooking.add(lblName, "cell 0 5,alignx left, aligny center"); //$NON-NLS-1$		
//		tfName = new com.floreantpos.swing.FixedLengthTextField(30);
        tfName = new JLabel ();
	//	tfName.setEnabled (false);
		tfName.setText ("");
		pnlBooking.add(tfName, "cell 1 5,alignx left, aligny center"); //$NON-NLS-1$

        JButton btnCustomer = new javax.swing.JButton();
		btnCustomer.setText (Messages.getString ("BookingForm.12"));
        btnCustomer.addActionListener(new java.awt.event.ActionListener() 
        {
			public void actionPerformed(java.awt.event.ActionEvent evt) 
			{
				doSelectCustomer ();
			}
		});
        pnlBooking.add(btnCustomer, "cell 2 5,alignx left, aligny center");	
		
		lblHomePhoneNo = new javax.swing.JLabel();
        lblHomePhoneNo.setText(Messages.getString ("BookingForm.10"));
		pnlBooking.add(lblHomePhoneNo, "cell 0 6,alignx left, aligny center"); //$NON-NLS-1$
		tfHomePhoneNo = new JLabel ();
		tfHomePhoneNo.setText ("");
		pnlBooking.add(tfHomePhoneNo, "cell 1 6,alignx left, aligny center"); //$NON-NLS-1$

		lblCellPhoneNo = new javax.swing.JLabel();
        lblCellPhoneNo.setText(Messages.getString ("BookingForm.23"));
		pnlBooking.add(lblCellPhoneNo, "cell 0 7,alignx left, aligny center"); //$NON-NLS-1$
		tfCellPhoneNo = new JLabel ();
		tfCellPhoneNo.setText ("");
		pnlBooking.add(tfCellPhoneNo, "cell 1 7,alignx left, aligny center"); //$NON-NLS-1$
		
		
		lblCity = new javax.swing.JLabel();
        lblCity.setText (Messages.getString ("BookingForm.11"));
		pnlBooking.add(lblCity, "cell 0 8,alignx left, aligny center"); //$NON-NLS-1$
		tfCity = new JLabel ();
		tfCity.setText ("");
		pnlBooking.add(tfCity, "cell 1 8,alignx left, aligny center"); //$NON-NLS-1$

		
		// btnAdd/Update  btnCancel
		if (mode == POSConstants.ADD_MODE)
		{
            btnOK = new PosButton(Messages.getString("BookingForm.24"));
        }
        else
        {
            btnOK = new PosButton(Messages.getString("BookingForm.25"));        
        }

		btnOK.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
               doAddUpdateBooking ();
            }
		});		
        
		pnlBooking.add(btnOK, "cell 0 9,alignx left, aligny center");
		
		PosButton btnCancel = new
        PosButton(Messages.getString("BookingForm.26")); 
		pnlBooking.add(btnCancel, "cell 1 9,alignx left, aligny center");

		btnCancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
                bookingView.cancelBookingForm (); 
            }
		});		
		add (pnlMain, BorderLayout.CENTER);
	}

	private JPanel createStatusBox ()
	{
        // create checkbox
        if (mode == POSConstants.ADD_MODE)
            { c0 = new JCheckBox (status [0], true);}
        else
            { c0 = new JCheckBox (status [0], false);}    
        c1 = new JCheckBox (status [1]);
        c2 = new JCheckBox (status [2]);        
        c3 = new JCheckBox (status [3]);        

        // create a new panel
        JPanel p = new JPanel();

        // add combobox to panel
        p.add(c0);
        p.add(c1);
        p.add(c2);
        p.add(c3);
        
        return p;
	}
	
	private void doSelectCustomer () 
	{
        bookingView.selectCustomerBookingForm ();
	}        
        
	
    private void initBookingValues ()
    {
		tfBId.setText ("");
        tfFromDate.setText ("");
        currentStatus = "";
        c0.setSelected(true);
        tfGuests.setText ("");
        tfCId.setText ("");
        tfName.setText ("");
        tfHomePhoneNo.setText ("");
        tfCellPhoneNo.setText ("");   
        tfCity.setText ("");
    }
		
    private void setBookingValues ()
    {
        Integer row = bookingTable.getSelectedRow();

		tfBId.setText ((String) bookingTable.getModel().getValueAt(row, 0).toString());

		// remove the T from date / time
		String fromDate = ((String) bookingTable.getModel().getValueAt(row, 1).toString());
		fromDate = fromDate.replace ("T", " ");
		tfFromDate.setText (fromDate);
        
        String s = (String) bookingTable.getModel().getValueAt(row, 2).toString();
        currentStatus = s;
        if (s.compareTo (status [0]) == 0)
            c0.setSelected(true);
        if (s.compareTo (status [1]) == 0)
            c1.setSelected(true);
        if (s.compareTo (status [2]) == 0)
            c2.setSelected(true);
        if (s.compareTo (status [3]) == 0)
            c3.setSelected(true);
        
        tfGuests.setText ((String) bookingTable.getModel().getValueAt(row, 4).toString());
        
        tfCId.setText ((String) bookingTable.getModel().getValueAt(row, 5).toString());
        
        tfName.setText ((String) bookingTable.getModel().getValueAt(row, 6).toString());

        
        tfHomePhoneNo.setText ((String) bookingTable.getModel().getValueAt(row, 7).toString());
        
        tfCellPhoneNo.setText ((String) bookingTable.getModel().getValueAt(row, 8).toString());   
        
        tfCity.setText ((String) bookingTable.getModel().getValueAt(row, 9).toString());
    		    
    }
	
	public void setCustomerFields (CustomerTable customerTable)
	{
        int row = customerTable.getSelectedRow ();
        
        tfCId.setText ((String) customerTable.getModel().getValueAt(row, 8).toString());
        
        tfName.setText ((String) customerTable.getModel().getValueAt(row, 0).toString() + " " + (String) customerTable.getModel().getValueAt(row, 1).toString());

        tfHomePhoneNo.setText ((String) customerTable.getModel().getValueAt(row, 2).toString());
        
        tfCellPhoneNo.setText ((String) customerTable.getModel().getValueAt(row, 3).toString());   
        
        tfCity.setText ((String) customerTable.getModel().getValueAt(row, 6).toString());
	
	}
	
	
	private void doAddUpdateBooking ()
	{
        if (validateBooking () == false)
            return;
            
        if (mode == POSConstants.ADD_MODE)
            doAddBooking ();
            
        if (mode == POSConstants.UPDATE_MODE)
            doUpdateBooking ();
           
        //???  refresh bookinglistview
        //???  add / show bookinglistview
        bookingView.addUpdateBookingForm (mode);
    }
	
	private boolean validateBooking ()
	{
        boolean result = true;
        Date date = null;
        errorMessage = "";
        
        // reservation date
        String testDate = tfFromDate.getText ();
        testDate = testDate.replace (" ", "T");
        try 
        {
            DateFormat parseFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm");
            parseFormat.setLenient(false);  
            date = parseFormat.parse  (testDate);
            Date today = new Date ();
            int i = date.compareTo (today);
            if (i < 0)
            {
                errorMessage +=  Messages.getString ("BookingForm.18");
                result = false;
            }
        } catch (ParseException e) 
        { 
            errorMessage +=  Messages.getString ("BookingForm.13");
            result = false;
        }
        
        // number of guests
        try 
        { 
            Integer.parseInt(tfGuests.getText()); 
        } catch(NumberFormatException e) 
        { 
            errorMessage +=  Messages.getString ("BookingForm.14");
            result = false;
        }
        
        // status  bookingform.15
        // some checkbox selected???
        
        // name
        String s = tfName.getText ();
        int i = s.length();
        if (i < 1)
        {
            
            errorMessage +=  Messages.getString ("BookingForm.16");
            result = false;
        }
        
        // phoneno
        s = tfHomePhoneNo.getText ();
        i = s.length();
        if (i < 1)
        {
            errorMessage +=  Messages.getString ("BookingForm.17");
            result = false;
        }        
        
        if (result == false)
            POSMessageDialog.showMessage (errorMessage);
            
        return result;
    }	

	
	public void doAddBooking () 
	{
        String [] fields = {"0", "1", "2", "3", "4", "5"};
        fields [0] = tfBId.getText ();
        fields [1] = tfFromDate.getText ();

        if (c0.isSelected ())
            fields [2] = "Booked";
        if (c1.isSelected ())
            fields [2] = "Confirmed";            
        if (c2.isSelected ())
            fields [2] = "Canceled";
        if (c3.isSelected ())
            fields [2] = "Arrived";          
        fields [3] = "now";  
        fields [4] = tfGuests.getText ();
        fields [5] = tfCId.getText (); 
        
        String strInsert = "insert into TABLE_BOOKING_INFO ( ";
        strInsert += "FROM_DATE, TO_DATE, STATUS, GUEST_COUNT, customer_id) values (";
        strInsert += "'" + fields [1] + "', ";
        strInsert += " now(), ";
        strInsert += "'" + fields [2] + "', "; 
        strInsert += fields [4] + ", ";   
        strInsert += fields [5] + ")";
        PosLog.error(getClass(), strInsert);
        

		Session session = null;
		Transaction tx = null;
		Connection connection  = null;
        GenericDAO dao = new GenericDAO();
		
		try 
		{
            session = dao.createNewSession(); // dao.

            connection =     session.connection ();
            tx = session.beginTransaction();

            try 
            {
                PreparedStatement stmt = connection.prepareStatement (strInsert);
        
                int rows = stmt.executeUpdate  ();
                
                stmt.close ();

                tx.commit ();
            }
            catch (Exception e)
            {
                tx.rollback ();
                PosLog.error(getClass(), e);
                POSMessageDialog.showMessage ("Insert failed, check log file");
            }
            finally 
            {
            
			}
        }
		catch (Exception e)
		{
            PosLog.error(getClass(), e);
            POSMessageDialog.showMessage ("Insert failed, see log file");		
		}
		finally 
		{
            session.close ();
		}
    
	}

	private void doUpdateBooking ()	
	{
         String [] fields = {"0", "1", "2", "3", "4", "5"};
        fields [0] = tfBId.getText ();
        fields [1] = tfFromDate.getText ();
        if (c0.isSelected ())
            fields [2] = "Booked";
        if (c1.isSelected ())
            fields [2] = "Canceled";            
        if (c2.isSelected ())
            fields [2] = "Confirmed";
        if (c3.isSelected ())
            fields [2] = "Arrived";
        if (currentStatus != fields [2])
            fields [3] = "now";
        else fields [3] = "";
        fields [4] = tfGuests.getText ();
        fields [5] = tfCId.getText ();  
        
        String strUpdate = "update TABLE_BOOKING_INFO set ";
        strUpdate += "FROM_DATE = '" + fields [1] + "', ";
        strUpdate += "STATUS = '" + fields [2] + "', "; 
        if (fields [3] != "")
            strUpdate += "TO_DATE = now(), ";
        strUpdate += "GUEST_COUNT = " + fields [4] + ", ";   
        strUpdate += "customer_id = " + fields [5] + " ";
        strUpdate += "where ID = " + fields [0];

		Session session = null;
        Transaction tx = null;
		Connection connection  = null;
        GenericDAO dao = new GenericDAO();
		
		try 
		{
            session = dao.createNewSession();
            connection =     session.connection ();
            tx = session.beginTransaction();
            
            try 
            {
                PreparedStatement stmt = connection.prepareStatement (strUpdate);
        
                int rows = stmt.executeUpdate  ();
                
                stmt.close ();

                tx.commit ();
            }
            catch (Exception e)
            {
                tx.rollback ();
                PosLog.error(getClass(), e);
                POSMessageDialog.showMessage ("Update failed, check log file");
            }
            finally 
            {
            
			}
        }
		catch (Exception e)
		{
            PosLog.error(getClass(), e);
            POSMessageDialog.showMessage ("Update failed, see log file");		
		}
		finally 
		{
            session.close ();
		}
			
	}

	
	public String getDisplayText() 
	{
		if (mode == POSConstants.ADD_MODE)
		{
			return Messages.getString("BookingForm.2"); // add reservation
		}
		return Messages.getString("BookingForm.3");  // edit reservation
	}
}
