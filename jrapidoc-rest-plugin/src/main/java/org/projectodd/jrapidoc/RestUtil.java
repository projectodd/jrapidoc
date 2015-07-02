package org.projectodd.jrapidoc;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.metadata.MethodParameter;
import org.jboss.resteasy.spi.metadata.Parameter;
import org.projectodd.jrapidoc.model.param.Param;

import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 29.3.15.
 */
public class RestUtil {

    public static String trimSlash(String urlPath){
        if(StringUtils.isEmpty(urlPath)){
            return urlPath;
        }
        if(urlPath.charAt(0) == '/'){
            urlPath = urlPath.substring(1);
        }
        if(StringUtils.isEmpty(urlPath)){
            return urlPath;
        }
        if(urlPath.charAt(urlPath.length() - 1) == '/'){
            urlPath = urlPath.substring(0, urlPath.length() - 1);
        }
        return urlPath;
    }

    public static String getPathInModelFormat(String path){
        path = trimSlash(path);
        if(path.equals("")){
            path = "/";
        }
        return path;
    }

    public static boolean isHttpParam(Parameter parameter){
        try{
            Param.Type.valueOf(parameter.getParamType().name());
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
