package crud.core.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import crud.core.properties.PropertiesLoaderImpl;
import crud.domain.cadastro.funcionario.model.Funcionario;
import crud.util.i18n.Messages;

/**
 * Responsavel para controlar o Hibernate.
 * 
 * @author Fagner W. Mateus
 */
public class HibernateDAO {

	private static SessionFactory sessionFactory;
	private static final Logger logger = Logger.getLogger(HibernateDAO.class);
	private static StandardServiceRegistry registry;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.addProperties(PropertiesLoaderImpl.getProperties());

			ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties())
					.build();

			MetadataSources sources = new MetadataSources(registry)//
					.addAnnotatedClass(Funcionario.class);
			Metadata metadata = sources.getMetadataBuilder().build();
			sessionFactory = metadata.getSessionFactoryBuilder().build();
		} catch (Exception e) {
			if (registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
			e.printStackTrace();
		}
	}

	/**
	 * Recupera a currentSession caso nao exista cria e armazena na thread
	 */
	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}

	/**
	 * Salva o objeto controlando a session do mesmo
	 */
	public static synchronized void save(Object ob) throws Exception {

		Session session = getSession();
		try {
			session.getTransaction().begin();
			session.persist(ob);
			logger.info(Messages.getString("HibernateDAO.persist") + ob.toString()); //$NON-NLS-1$
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			session.getTransaction().rollback();
			session.close();
			logger.error(Messages.getString("HibernateDAO.persist.exception") + ob.toString()); //$NON-NLS-1$
			throw e;
		}

	}

	/**
	 * Deleta o objeto controlando a session do mesmo
	 */
	public static synchronized void delete(Object ob) throws Exception {

		Session session = getSession();

		try {
			session.getTransaction().begin();
			session.delete(ob);
			session.getTransaction().commit();
			logger.info(Messages.getString("HibernateDAO.delete") + ob.toString()); //$NON-NLS-1$
		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error(Messages.getString("HibernateDAO.delete.exception") + ob.toString()); //$NON-NLS-1$
			e.printStackTrace();
			throw e;
		} finally {
			session.evict(ob);
			session.close();
		}

	}

	/**
	 * Merge do objeto controlando a session do mesmo
	 */
	public static synchronized void merge(Object ob) throws Exception {

		Session session = getSession();

		try {
			session.getTransaction().begin();
			session.merge(ob);
			logger.info(Messages.getString("HibernateDAO.merge") + ob.toString()); //$NON-NLS-1$
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(Messages.getString("HibernateDAO.merge.exception") + ob.toString()); //$NON-NLS-1$
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}

	}

	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}

	public static synchronized void doVacuum() throws Exception {
		Connection connection = DriverManager.getConnection(PropertiesLoaderImpl.getValor(AvailableSettings.URL),
				PropertiesLoaderImpl.getValor(AvailableSettings.USER),
				PropertiesLoaderImpl.getValor(AvailableSettings.PASS));
		connection.prepareStatement("vacuum").execute(); //$NON-NLS-1$
		connection.close();
	}

}
