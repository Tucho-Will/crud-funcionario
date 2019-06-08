package crud.core.properties;

import java.util.Properties;

/**
 * Implementação do PropertiesLoad com acesso estatico
 */
public class PropertiesLoaderImpl {
    private static PropertiesLoader loader = new PropertiesLoader();

    /**
     * Recupera o valor da propriedade requisitada, se não encontrada retorna null
     * 
     * @param chave
     **/
    public static String getValor(String chave) {
        return loader.getValor(chave);
    }

    /**
     * Recupera o valor da propriedade requisitada, se não encontrada retorna valor
     * default
     * 
     * @param chave
     * @param defaultValue
     */
    public static String getValor(String chave, String defaultValue) {
        return loader.getValor(chave, defaultValue);
    }

    /**
     * Recupera o valor da propriedade requisitada, se não encontrada retorna null
     * 
     * @param chave
     **/
    public static int getValorInt(String chave) {
        return loader.getValorInt(chave);
    }

    /**
     * Recupera o valor da propriedade requisitada, se não encontrada retorna valor
     * default
     * 
     * @param chave
     * @param defaultValue
     */
    public static int getValorInt(String chave, int defaultValue) {
        return loader.getValorInt(chave, defaultValue);
    }

    /**
     * Recupera todas as properties do connection
     */
    public static Properties getProperties() {
        return loader.getProperties();
    }

}
