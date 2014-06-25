package je.techtribes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.structurizr.ComponentFinder;
import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ContextView;

public class CreateViews extends AbstractUtility {

    public static void main(String[] args) throws Exception {
        Model model = readModelFromFile();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName("Content Updater");

        System.out.println("Context view");
        System.out.println("============");
        ContextView contextView = model.createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.getElements().forEach(System.out::println);
        contextView.getRelationships().forEach(System.out::println);
        System.out.println("============");

        System.out.println();

        System.out.println("Container view");
        System.out.println("============");
        ContainerView containerView = model.createContainerView(techTribes);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();
        containerView.getElements().forEach(System.out::println);
        containerView.getRelationships().forEach(System.out::println);
        System.out.println("============");

        System.out.println();

        System.out.println("Component view - content updater");
        System.out.println("============");
        ComponentView componentView = model.createComponentView(techTribes, contentUpdater);
        componentView.addAllSoftwareSystems();
        componentView.addAllContainers();
        componentView.addAllComponents();
        componentView.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        componentView.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        componentView.remove(contentUpdater.getComponentWithName("ActivityComponent"));
        componentView.removeElementsWithNoRelationships();
        componentView.getElements().forEach(System.out::println);
        componentView.getRelationships().forEach(System.out::println);
        System.out.println("============");

        writeModelToFile(model);
    }

}
