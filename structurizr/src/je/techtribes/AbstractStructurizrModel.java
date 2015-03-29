package je.techtribes;

import com.structurizr.Workspace;
import com.structurizr.io.json.JsonReader;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.SoftwareSystem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

abstract class AbstractStructurizrModel {

    protected static final String FILENAME = "techtribesje.json";

    protected static final String TECHTRIBES_JE = "techtribes.je";
    protected static final String WEB_APPLICATION = "Web Application";
    protected static final String CONTENT_UPDATER = "Content Updater";

    protected Workspace workspace;

    protected AbstractStructurizrModel() throws Exception {
        readFromFile();
    }

    protected SoftwareSystem getTechTribesSoftwareSystem() {
        return workspace.getModel().getSoftwareSystemWithName(TECHTRIBES_JE);
    }

    protected void writeToFile() throws Exception {
        FileWriter fileWriter = new FileWriter(new File(FILENAME));
        JsonWriter jsonWriter = new JsonWriter(true);
        jsonWriter.write(workspace, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    protected void readFromFile() throws Exception {
        File jsonFile = new File(FILENAME);
        if (jsonFile.exists()) {
            FileReader fileReader = new FileReader(new File(FILENAME));
            JsonReader jsonReader = new JsonReader();
            workspace = jsonReader.read(fileReader);
            fileReader.close();
        }
    }

}