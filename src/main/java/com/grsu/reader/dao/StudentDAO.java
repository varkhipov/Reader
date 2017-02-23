package com.grsu.reader.dao;

import com.grsu.reader.models.PassInfo;
import com.grsu.reader.entities.Student;
import com.grsu.reader.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAO extends EntityDAO {

	public Student getByCardUid(String cardUid) {
		Session session = DBSessionFactory.getSession();
		try {
			Query query = session.createQuery("from Student where cardUid = :cardUid");
			query.setParameter("cardUid", cardUid);
			query.setFirstResult(0);
			query.setMaxResults(1);
			return (Student) query.uniqueResult();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public List<PassInfo> getPassInfo() {
		Session session = DBSessionFactory.getSession();
		try {
			return session.createNamedQuery("PassInfoQuery").getResultList();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

}
