package com.grsu.reader.dao;

import com.grsu.reader.models.SkipInfo;
import com.grsu.reader.entities.Student;
import com.grsu.reader.utils.db.DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.List;

public class StudentDAO {

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

	public List<SkipInfo> getSkipInfo(int streamId, int lessonId) {
		Session session = DBSessionFactory.getSession();
		try {
			Query query = session.createNamedQuery("SkipInfoQuery", SkipInfo.class);
			query.setParameter("streamId", streamId);
			query.setParameter("lessonId", lessonId);
			return query.getResultList();
		} catch (PersistenceException e) {
			System.err.println(e.getLocalizedMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public List<SkipInfo> getStudentSkipInfo(List<Integer> studentId, int streamId, int lessonId) {
		Session session = DBSessionFactory.getSession();
		try {
			Query query = session.createNamedQuery("StudentSkipInfoQuery", SkipInfo.class);
			query.setParameterList("studentId", studentId);
			query.setParameter("streamId", streamId);
			query.setParameter("lessonId", lessonId);
			return query.getResultList();
		} catch (PersistenceException e) {
			System.err.println(e.getLocalizedMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

}
