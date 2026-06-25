package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the TABLE_BOOKING_INFO table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="TABLE_BOOKING_INFO"
 */

public abstract class BaseBookingCustomerInfo  implements Comparable, Serializable {

	public static String REF = "BookingCustomerInfo"; //$NON-NLS-1$
	public static String PROP_BOOKING_ID = "bookingId"; //$NON-NLS-1$
	public static String PROP_FROM_DATE = "fromDate"; //$NON-NLS-1$
	public static String PROP_STATUS = "status"; //$NON-NLS-1$
	public static String PROP_TO_DATE = "toDate"; //$NON-NLS-1$
	public static String PROP_GUEST_COUNT = "guestCount"; //$NON-NLS-1$
	public static String PROP_CUSTOMER_ID = "customerId"; //$NON-NLS-1$
	public static String PROP_NAME = "name"; //$NON-NLS-1$
	public static String PROP_HOME_PHONE = "homePhone"; //$NON-NLS-1$
	public static String PROP_CELL_PHONE = "cellPhone"; //$NON-NLS-1$
	public static String PROP_CITY = "city"; 


	// constructors
	public BaseBookingCustomerInfo () {
		initialize();
	}

	public BaseBookingCustomerInfo (java.lang.Integer id) {
		this.setBookingId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
        protected java.lang.Integer bookingId;
		protected java.util.Date fromDate;
		protected java.lang.String status;		
		protected java.util.Date toDate;
		protected java.lang.Integer guestCount;
		protected java.lang.Integer customerId;
		protected java.lang.String name;
		protected java.lang.String homePhone;
		protected java.lang.String cellPhone;
		protected java.lang.String city;
		

	// many to one
//	private com.floreantpos.model.User user;
//	private com.floreantpos.model.Customer customer;

	// collections
//	private java.util.List<com.floreantpos.model.ShopTable> tables;


	public java.lang.Integer getBookingId ()
	{
		return bookingId;
	}

	public void setBookingId (java.lang.Integer id) 
	{
		this.bookingId = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public java.util.Date getFromDate () 
	{
        return fromDate;
    }

	public void setFromDate (java.util.Date fromDate) 
	{
		this.fromDate = fromDate;
	}

	public java.util.Date getToDate () 
	{
        return toDate;
    }

	public void setToDate (java.util.Date toDate) 
	{
		this.toDate = toDate;
	}

	public java.lang.Integer getGuestCount () 
	{
        return guestCount == null ? Integer.valueOf(0) : guestCount;
    }

	public void setGuestCount (java.lang.Integer guestCount) 
	{
		this.guestCount = guestCount;
	}

	public java.lang.String getStatus () 
	{
        return status;
    }

	public void setStatus (java.lang.String status) 
	{
		this.status = status;
	}

	public java.lang.String getName () 
	{
        return name;
    }
    
	public void setName (java.lang.String name) 
	{
		this.name = name;
	}

	public java.lang.String getHomePhone ()
	{
        return homePhone;
    }

	public void setHomePone (java.lang.String homePhone) 
	{
		this.homePhone = homePhone;
	}

	public java.lang.String getCellPhone ()
	{
        return cellPhone;
    }

	public void setCellPhone (java.lang.String cellPhone) 
	{
		this.cellPhone = cellPhone;
	}

	public java.lang.String getCity () 
	{
        return city;
    }

	public void setCity (java.lang.String city) 
	{
		this.city = city;
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getBookingId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getBookingId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}


}
