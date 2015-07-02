package org.projectodd.jrapidoc.model.handler;

import org.projectodd.jrapidoc.model.APIModel;

/**
 * Created by Tomas "sarzwest" Jiricek on 12.4.15.
 */
public interface ModelHandler {

    void handleModel(APIModel model) throws HandlerException;
}
