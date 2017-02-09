import com.grsu.reader.utils.FileUtils;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;
import static com.grsu.reader.utils.PropertyUtils.*;

import javax.persistence.metamodel.EntityType;

import java.util.Map;

/**
 *  Created by zaychick-pavel on 2/8/17.
 */
public class Main {
	private static final SessionFactory ourSessionFactory;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:/storage/storage/env/apache-tomcat-8.5.5/app_files/database/db.s3db");

			ourSessionFactory = configuration.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws HibernateException {
		return ourSessionFactory.openSession();
	}


	public static void main(final String[] args) throws Exception {
		final Session session = getSession();
		try {
			System.out.println("querying all the managed entities...");
			final Metamodel metamodel = session.getSessionFactory().getMetamodel();
			for (EntityType<?> entityType : metamodel.getEntities()) {
				final String entityName = entityType.getName();
				final Query query = session.createQuery("from " + entityName);
				System.out.println("executing: " + query.getQueryString());
				for (Object o : query.list()) {
					System.out.println("  " + o);
				}
			}
		} finally {
			session.close();
		}

	}
}