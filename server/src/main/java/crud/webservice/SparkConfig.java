package crud.webservice;

import crud.core.hibernate.HibernateDAO;
import crud.core.properties.PropertiesLoaderImpl;
import crud.core.spark.filter.CorsFilter;
import crud.domain.cadastro.funcionario.resource.FuncionarioResource;
import crud.util.enums.ECommonsKeys;
import spark.Spark;

/**
 * @author Fagner W. Mateus
 * @since 07/06/2019
 */
public class SparkConfig {

	public static void start() throws Exception {
		// Configurações do spark/inicia rotas
		sparkConfig();
		HibernateDAO.getSession();
	}

	public static void restartSpark() {
		sparkConfig();
	}

	private static void initResources() {
		new FuncionarioResource();
	}

	private static void sparkConfig() {
		Spark.staticFiles.location("/public");
		{
			Spark.port(PropertiesLoaderImpl.getValorInt(ECommonsKeys.APPLICATION_PORT.toString(), 8090));
		}
		Spark.threadPool(200, 10, 6000);
		CorsFilter.crossOrigin();
		initResources();
	}
}
