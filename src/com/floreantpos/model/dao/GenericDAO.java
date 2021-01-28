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
package com.floreantpos.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Database;
import com.floreantpos.config.AppConfig;

public class GenericDAO extends _RootDAO {

	public GenericDAO() {
		super();
	}

	@Override
	protected Class getReferenceClass() {
		return null;
	}
	
	@Override
	public Serializable save(Object obj) {
		return super.save(obj);
	}
	
	@Override
	public void saveOrUpdate(Object obj) {
		super.saveOrUpdate(obj);
	}
	
	@Override
	public Serializable save(Object obj, Session s) {
		return super.save(obj, s);
	}
	
	@Override
	public void saveOrUpdate(Object obj, Session s) {
		super.saveOrUpdate(obj, s);
	}
	
	@Override
	public Session getSession(String configFile, boolean createNew) {
		return super.getSession(configFile, createNew);
	}
	
	public List findAll(Class clazz, Session session) {
		Criteria crit = session.createCriteria(clazz);
		return crit.list();
	}
	
	public void saveAll(List list, Session session) {
		Transaction tx = session.beginTransaction();
		for (Object object : list) {
			session.saveOrUpdate(object);
		}
		tx.commit();
	}
	
	@Override
	public void closeSession(Session session) {
		try {
			super.closeSession(session);
		}catch(Exception x) {}
	}

	public static Configuration getNewConfiguration(String configFileName) {
		Configuration configuration = new Configuration();	
		Database defaultDatabase = AppConfig.getDefaultDatabase();

		configuration.setProperty("hibernate.dialect", defaultDatabase.getHibernateDialect()); //$NON-NLS-1$
		configuration.setProperty("hibernate.connection.driver_class", defaultDatabase.getHibernateConnectionDriverClass()); //$NON-NLS-1$

		configuration.setProperty("hibernate.connection.url", AppConfig.getConnectString()); //$NON-NLS-1$
		configuration.setProperty("hibernate.connection.username", AppConfig.getDatabaseUser()); //$NON-NLS-1$
		configuration.setProperty("hibernate.connection.password", AppConfig.getDatabasePassword()); //$NON-NLS-1$
		configuration.setProperty("hibernate.hbm2ddl.auto", "update"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.connection.autocommit", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.max_fetch_depth", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.show_sql", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		configuration.setProperty("hibernate.connection.isolation", String.valueOf(Connection.TRANSACTION_READ_COMMITTED)); //$NON-NLS-1$

		return configuration;	
	}
	
}
