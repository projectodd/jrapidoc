package org.projectodd.jrapidoc.model.generator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.APIModel;

import java.io.*;

/**
 * Created by Tomas "sarzwest" Jiricek on 3.4.15.
 */
public class ModelGenerator {

    public static void generateModel(APIModel model, File output) throws JrapidocExecutionException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(output);
            generateModel(model, outputStream);
        }catch (FileNotFoundException e){
            Logger.error(e, "File {0} was not found on filesystem", output.getAbsolutePath());
            throw new JrapidocExecutionException(e.getMessage(), e);
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Logger.warn(e, "File output stream {0} could not be closed", output.getAbsolutePath());
                }
            }
        }
    }

    public static void generateModel(APIModel model, OutputStream output) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(output, model);
        } catch (IOException e) {
            Logger.error(e, "Could not write model to output stream");
        }
    }
}
