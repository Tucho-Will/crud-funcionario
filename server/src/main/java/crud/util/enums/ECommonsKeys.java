package crud.util.enums;

/**
 * @author Fagner W. Mateus
 * @since 07/06/2019
 */
public enum ECommonsKeys {

	APPLICATION_JSON("application/json"), //
	APPLICATION_URLENCODED("application/x-www-form-urlencoded"), //
	APPLICATION_PORT("WEBSERVICE_PORT");

	private String valor;

	private ECommonsKeys(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return valor;
	}

}
