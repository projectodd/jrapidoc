package org.projectodd.jrapidoc.model.handler;

import org.projectodd.jrapidoc.model.APIModel;

public interface ModelHandler {

    void handleModel(APIModel model) throws HandlerException;
    
}
