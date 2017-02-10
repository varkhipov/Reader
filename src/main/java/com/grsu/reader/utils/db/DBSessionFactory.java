package com.grsu.reader.utils.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by pavel on 2/9/17.
 */
public class DBSessionFactory {
//	private static final String DATABASE_URL = "jdbc:" + getProperty("db.protocol") + ":" + FileUtils.DATABASE_PATH;
	private static final String DATABASE_URL = "jdbc:sqlite:/home/zaychick-pavel/env/apache-tomcat-8.5.5/app_files/database/db.s3db";
	private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			configuration.setProperty(HIBERNATE_CONNECTION_URL, DATABASE_URL);

			sessionFactory = configuration.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	public static void closeSessionFactory() {
		sessionFactory.close();
	}

}
