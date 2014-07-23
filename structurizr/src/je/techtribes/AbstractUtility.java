package je.techtribes;

import com.structurizr.model.Model;
import com.structurizr.util.JsonUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class AbstractUtility {

    protected static final String FILENAME = "techtribes.json";

    protected static void writeModelToFile(Model model) throws Exception {
        FileWriter fileWriter = new FileWriter(new File(FILENAME));
        fileWriter.write(JsonUtils.toJson(model, true));
        fileWriter.flush();
        fileWriter.close();
    }

    protected static Model readModelFromFile() throws Exception {
        StringBuilder buf = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(new File(FILENAME)));
        line = reader.readLine();
        while (line != null) {
            buf.append(line);
            line = reader.readLine();
        }
        reader.close();

        return JsonUtils.toModel(buf.toString());
    }

}
