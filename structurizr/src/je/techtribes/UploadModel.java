package je.techtribes;

import com.structurizr.api.StructurizrClient;

public class UploadModel extends AbstractStructurizrModel {

    public static void main(String[] args) throws Exception {
        new UploadModel().run();
    }

    public UploadModel() throws Exception {
    }

    void run() throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient();
        structurizrClient.mergeWorkspace(21, workspace);
    }


}
