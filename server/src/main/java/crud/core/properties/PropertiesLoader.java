package crud.core.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import crud.util.i18n.Messages;

/*
 * Claase utilizada para fazer leitura do arquivos Connection
 */
public class PropertiesLoader {
    private Properties props;
    private String nomeDoProperties = "/connection.properties";

    protected PropertiesLoader() {
        props = new Properties();
        try {
            // carrega do arquivo externo(na pasta onde está executando), se não
            // achar,
            // carrega do jar
            InputStream resource;
            File file = new File("connection.properties");
            if (file.exists()) {
                resource = new FileInputStream(file);
            } else {
                resource = this.getClass().getResourceAsStream(nomeDoProperties);
            }
            if (resource != null) {
                props.load(resource);
                resource.close();
            } else {
                throw new IOException(Messages.getString("PropertiesLoader.exception.message"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String getValor(String chave, String defaultValue) {
        return props.getProperty(chave, defaultValue);
    }

    protected String getValor(String chave) {
        return props.getProperty(chave);
    }

    protected int getValorInt(String chave, int defaultValue) {
        String value = props.getProperty(chave, "");
        if (value.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    protected int getValorInt(String chave) {
        String value = props.getProperty(chave, "");
        if (value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    protected Properties getProperties() {
        return props;
    }
}
