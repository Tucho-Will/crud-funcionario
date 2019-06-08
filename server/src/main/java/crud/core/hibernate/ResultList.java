package crud.core.hibernate;

import java.util.List;

import lombok.Data;

/**
 * Retorno para consultas paginadas
 * 
 */
@Data
public class ResultList {

    private List<?> retorno;
    private Long count;

    /**
     * Retorno com a lista de objetos e o count total de registros.
     * 
     * @param retorno
     * @param count
     */
    public ResultList(List<?> retorno, Long count) {
        this.retorno = retorno;
        this.count = count;
    }

}
