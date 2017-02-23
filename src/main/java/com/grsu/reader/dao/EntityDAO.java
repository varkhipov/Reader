package com.grsu.reader.dao;

import com.grsu.reader.entities.AssistantEntity;
import com.grsu.reader.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by zaychick-pavel on 2/10/17.
 */
public class EntityDAO {
	public void add(AssistantEntity entity) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();
			session.save(entity);
			transaction.commit();
			System.out.println("[ " + entity + " ] successfully added to database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void add(List<AssistantEntity> entities) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();

			int count=0;
			for (AssistantEntity entity : entities) {
				session.save(entity);
				if ( ++count % 20 == 0 ) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			System.out.println("[ " + entities + " ] successfully added to database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void delete(AssistantEntity entity) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();
			session.delete(entity);
			transaction.commit();
			System.out.println("[ " + entity + " ] successfully deleted from database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void delete(List<AssistantEntity> entities) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();

			int count=0;
			for (AssistantEntity entity : entities) {
				session.delete(entity);
				if ( ++count % 20 == 0 ) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			System.out.println("[ " + entities + " ] successfully deleted from database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void update(AssistantEntity entity) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();
			session.update(entity);
			transaction.commit();
			System.out.println("[ " + entity + " ] successfully updated in database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void update(List<AssistantEntity> entities) {
		Transaction transaction = null;
		Session session = DBSessionFactory.getSession();

		try {
			transaction = session.beginTransaction();

			int count=0;
			for (AssistantEntity entity : entities) {
				session.update(entity);
				if ( ++count % 20 == 0 ) {
					session.flush();
					session.clear();
				}
			}
			transaction.commit();
			System.out.println("[ " + entities + " ] successfully updated in database.");
		} catch (RuntimeException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void save(AssistantEntity entity) {
		if (entity.getId() == null) {
			new EntityDAO().add(entity);
		} else {
			new EntityDAO().update(entity);
		}
	}

	public <T extends AssistantEntity> T get(Class<T> entityType, int id) {
		Session session = DBSessionFactory.getSession();

		try {
			return session.get(entityType, id);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public <T extends AssistantEntity> List<T> getAll(Class<T> entityType) {
		Session session = DBSessionFactory.getSession();

		try {
			return session.createQuery("from " + entityType.getSimpleName()).list();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
}
