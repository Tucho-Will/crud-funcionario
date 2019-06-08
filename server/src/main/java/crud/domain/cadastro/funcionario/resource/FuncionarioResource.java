package crud.domain.cadastro.funcionario.resource;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import crud.core.gson.GsonHelper;
import crud.core.gson.JsonTransformer;
import crud.core.hibernate.HibernateDAO;
import crud.core.hibernate.ResultList;
import crud.domain.cadastro.funcionario.dao.FuncionarioDAO;
import crud.domain.cadastro.funcionario.model.Funcionario;
import crud.util.enums.ECommonsKeys;
import spark.Spark;

/**
 * @author Fagner W. Mateus
 * @since 07/06/2019
 */
public class FuncionarioResource {

	public FuncionarioResource() {

		path("/api", () -> {

			get("/employees", ECommonsKeys.APPLICATION_JSON.toString(), (request, response) -> {
				ResultList result = FuncionarioDAO.getAll(request.queryParams("term"), //
						Long.valueOf(request.queryParams("page")), //
						Long.valueOf(request.queryParams("rowsPerPage")));
				
				JsonObject retorno = new JsonObject();
				retorno.addProperty("total", result.getCount());
				retorno.add("employees", GsonHelper.gson.toJsonTree(result.getRetorno()));
				return retorno;
			}, new JsonTransformer());

			delete("/employee/:id", (request, response) -> {
				try {
					String params = request.params(":id");
					FuncionarioDAO.removerFuncionario(Long.valueOf(params));
				} catch (Exception e) {
					Spark.halt(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Não foi possivel executar ação solicitada.");
				}
				return HttpServletResponse.SC_OK;
			});

			get("/employee/:id", ECommonsKeys.APPLICATION_JSON.toString(), (request, response) -> {
				try {
					String params = request.params(":id");
					Funcionario ob = FuncionarioDAO.getFuncionario(Long.valueOf(params));
					return ob;
				} catch (Exception e) {
					Spark.halt(HttpServletResponse.SC_NOT_FOUND, "Não foi possivel executar ação solicitada.");
				}
				return response;
			}, new JsonTransformer());

			put("/employee/:id", ECommonsKeys.APPLICATION_JSON.toString(), (request, response) -> {
				String params = request.params(":id");

				if (request.body() == null || request.body().isEmpty() || params == null || params.isEmpty()) {
					Spark.halt(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Não foi possivel atualizar o funcionario");
				}

				try {
					Funcionario ob = GsonHelper.gson.fromJson(request.body(), Funcionario.class);
					ob.setIdFuncionario(Long.valueOf(params));
					HibernateDAO.merge(ob);
					return "OK";
				} catch (Exception exception) {
					Spark.halt(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Não foi possivel atualizar o funcionario");
				}

				return null;
			}, new JsonTransformer());

			post("/employee", ECommonsKeys.APPLICATION_JSON.toString(), (request, response) -> {

				if (request.body() == null || request.body().isEmpty()) {
					// UTF-8 encoding not supported
					Spark.halt(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Não foi possivel gravar o funcionario");
				}

				try {
					Funcionario ob = GsonHelper.gson.fromJson(request.body(), Funcionario.class);
					HibernateDAO.save(ob);
					return "OK";
				} catch (Exception e) {
					Spark.halt(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possivel gravar a rota");
				}
				return null;
			}, new JsonTransformer());

		});

	}

}
