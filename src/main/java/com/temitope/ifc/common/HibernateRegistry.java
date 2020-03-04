package com.temitope.ifc.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.temitope.model.Category;
import com.temitope.model.Material;
import com.temitope.model.Synonym;
import com.temitope.owl.model.FloorMaterial;
import com.temitope.owl.model.Gypsum_Board;
import com.temitope.owl.model.Insulation;
import com.temitope.owl.model.Plywood_Sheathing;
import com.temitope.owl.model.Stud;
import com.temitope.owl.model.WallMaterial;

public class HibernateRegistry {

	private static SessionFactory sessionJavaConfigFactory;
	private static HibernateRegistry instance;
	private Session session;
	private Transaction transaction;

	private static SessionFactory buildSessionJavaConfigFactory() {
		try {
			java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
			Configuration configuration = new Configuration();
			File file = new File("resources/database.properties");
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			/*
			 * configuration.addAnnotatedClass(Category.class);
			 * configuration.addAnnotatedClass(Material.class);
			 * configuration.addAnnotatedClass(Synonym.class);
			 */
			configuration.addAnnotatedClass(Stud.class);
			configuration.addAnnotatedClass(Plywood_Sheathing.class);
			configuration.addAnnotatedClass(Insulation.class);
			configuration.addAnnotatedClass(Gypsum_Board.class);
			configuration.addAnnotatedClass(FloorMaterial.class);
			configuration.addAnnotatedClass(WallMaterial.class);
			configuration.addAnnotatedClass(Synonym.class);
			configuration.addAnnotatedClass(Category.class);
			configuration.addAnnotatedClass(Material.class);
			
			
			configuration.setProperties(properties);
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			// System.out.println("Hibernate Java Config serviceRegistry created");
			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			return sessionFactory;
		} catch (Throwable ex) {
			// System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionJavaConfigFactory() {
		if (sessionJavaConfigFactory == null)
			sessionJavaConfigFactory = buildSessionJavaConfigFactory();
		return sessionJavaConfigFactory;
	}

	public static void closeSessionFactory() {
		if (sessionJavaConfigFactory != null) {
			sessionJavaConfigFactory.close();
			sessionJavaConfigFactory = null;
		}
	}

	public Session getSession() {
		Session session = buildSessionJavaConfigFactory().getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);
		return session;
	}

	public void closeSession(Session session, boolean flag) {
		Transaction transaction = session.getTransaction();
		if (transaction != null) {
			if (flag) {
				transaction.commit();
			} else {
				transaction.rollback();
			}
			session.close();
		}
	}

	public static HibernateRegistry getInstance() {
		if (instance == null) {
			instance = new HibernateRegistry();
		}
		return instance;
	}
}