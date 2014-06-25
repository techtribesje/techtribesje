package je.techtribes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.model.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class AbstractUtility {

    protected static final String FILENAME = "techtribes.json";

    protected static void writeModelToFile(Model model) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String modelAsJson = objectMapper.writeValueAsString(model);

        FileWriter fileWriter = new FileWriter(new File(FILENAME));
        fileWriter.write(modelAsJson);
        fileWriter.flush();
        fileWriter.close();
    }

    protected static Model readModelFromFile() throws Exception {
        StringBuffer buf = new StringBuffer();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(new File(FILENAME)));
        line = reader.readLine();
        while (line != null) {
            buf.append(line);
            line = reader.readLine();
        }
        reader.close();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        Model model = objectMapper.readValue(buf.toString(), Model.class);
        model.hydrate();

        return model;
    }

}
