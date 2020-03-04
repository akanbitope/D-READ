package com.temitope.dao;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.temitope.ifc.common.HibernateRegistry;

import java.util.List;

/**
 *
 */
public class BaseDAOHibernate {

	public SessionFactory getSessionFactory() {
		return HibernateRegistry.getSessionJavaConfigFactory();
	}

	public void deleteObject(Object o) {
		getSession().delete(o);
	}

	public <T> List<T> loadAll(Class c) {
		Criteria criteria = getSession().createCriteria(c);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	public Object getById(Integer id, Class c) {
		if (id != null && id > 0) {
			return getSession().createCriteria(c).add(Restrictions.idEq(id)).uniqueResult();
		}
		return null;
	}

	public void saveOrUpdateObject(Object o) {
		getSession().saveOrUpdate(o);
	}

	public Session getSession() {
		Session session = getSessionFactory().getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);
		return session;
	}

	protected SimpleExpression criteriaStringLikeWild(String property, String s) {
		return Restrictions.like(property, "%" + s + "%");
	}

	protected List basicList(Class clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	public void closeSession(Session session, boolean flag) {
		Transaction transaction = session.getTransaction();
		if (transaction != null) {
			if (flag) {
				transaction.commit();
			} else {
				transaction.rollback();
			}
			getSession().close();
		}
	}

}
