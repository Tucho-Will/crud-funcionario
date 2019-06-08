package crud.core.spark.filter;

import spark.Spark;

/**
 * Responsavel pela configuração do crossOrigem do webservice.
 * 
 * @author Fagner W. Mateus
 * @since 07/06/2019
 */
public class CorsFilter {

	public static final void crossOrigin() {

		Spark.before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Request-Method", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
			response.header("Access-Control-Allow-Headers",
					"Access-Control-*, Origin, X-Requested-With, Content-Type, Accept");
			response.type("application/json");
		});

		Spark.options("/*", (request, response) -> {

			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});

	}

}
