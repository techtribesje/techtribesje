package je.techtribes;

import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.componentfinder.StructurizrComponentFinderStrategy;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;

public class CreateComponentModelForContainer extends AbstractUtility {

    public static void main(String[] args) throws Exception {
        Model model = readModelFromFile();

        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container container = techTribes.getContainerWithName(args[0]);

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "je.techtribes",
                new SpringComponentFinderStrategy(),
                new StructurizrComponentFinderStrategy());

        componentFinder.findComponents();

        writeModelToFile(model);
    }

}
