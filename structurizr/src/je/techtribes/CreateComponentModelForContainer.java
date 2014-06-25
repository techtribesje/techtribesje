package je.techtribes;

import com.structurizr.ComponentFinder;
import com.structurizr.model.Container;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

public class CreateComponentModelForContainer extends AbstractUtility {

    public static void main(String[] args) throws Exception {
        Model model = readModelFromFile();

        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName(args[0]);

        ComponentFinder componentFinder = new ComponentFinder(contentUpdater, "je.techtribes");
        componentFinder.findComponents();
        componentFinder.findComponentDependencies();
        componentFinder.findSoftwareSystemDependencies();
        componentFinder.findContainerDependencies();

        writeModelToFile(model);
    }

}
