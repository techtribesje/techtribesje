package je.techtribes;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class AbstractUtility {

    protected static final String FILENAME = "techtribes.json";

    protected static void writeToFile(Workspace workspace) throws Exception {
        FileWriter fileWriter = new FileWriter(new File(FILENAME));
        JsonWriter jsonWriter = new JsonWriter(true);
        jsonWriter.write(workspace, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    protected static Workspace readFromFile() throws Exception {
        FileReader fileReader = new FileReader(new File(FILENAME));
        JsonReader jsonReader = new JsonReader();
        Workspace workspace = jsonReader.read(fileReader);
        fileReader.close();

        return workspace;
    }

}