package crud.core.gson;

import spark.ResponseTransformer;

/**
 * @author Fagner W. Mateus
 * @since 07/06/2019
 */
public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        return GsonHelper.gson.toJson(model);
    }

}