package je.techtribes;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateViews extends AbstractUtility {

    public static void main(String[] args) throws Exception {
        Model model = readModelFromFile();

        createSystemContextView(model);
        createContainerView(model);
        createComponentViewsForContentUpdater(model);
        createComponentViewsForWebApplication(model);

        showViews(model);

        writeModelToFile(model);
    }

    private static void createSystemContextView(Model model) {
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        SystemContextView contextView = model.createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
    }

    private static void createContainerView(Model model) {
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        ContainerView containerView = model.createContainerView(techTribes);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();
    }

    private static void createComponentViewsForContentUpdater(Model model) {
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName("Content Updater");
        Container webApplication = techTribes.getContainerWithName("Web Application");

        ComponentView view = model.createComponentView(techTribes, contentUpdater);
        view.setDescription("Updating information from external systems");
        view.addAllSoftwareSystems();
        view.addAllContainers();
        view.remove(webApplication);
        view.addAllComponents();
        view.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        view.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        view.remove(contentUpdater.getComponentWithName("ActivityUpdater"));
        view.remove(contentUpdater.getComponentWithName("BadgeAwarder"));
        view.removeElementsWithNoRelationships();
        view.removeElementsThatCantBeReachedFrom(contentUpdater.getComponentWithName("ScheduledContentUpdater"));

        view = model.createComponentView(techTribes, contentUpdater);
        view.setDescription("Updating recent activity");
        view.addAllSoftwareSystems();
        view.addAllContainers();
        view.remove(webApplication);
        view.addAllComponents();
        view.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        view.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        view.remove(contentUpdater.getComponentWithName("TwitterConnector"));
        view.remove(contentUpdater.getComponentWithName("GitHubConnector"));
        view.remove(contentUpdater.getComponentWithName("NewsFeedConnector"));
        view.remove(contentUpdater.getComponentWithName("BadgeAwarder"));
        view.removeElementsWithNoRelationships();
        view.removeElementsThatCantBeReachedFrom(contentUpdater.getComponentWithName("ActivityUpdater"));

        view = model.createComponentView(techTribes, contentUpdater);
        view.setDescription("Awarding badges");
        view.addAllSoftwareSystems();
        view.addAllContainers();
        view.remove(webApplication);
        view.addAllComponents();
        view.remove(contentUpdater.getComponentWithName("LoggingComponent"));
        view.remove(contentUpdater.getComponentWithName("ContentSourceComponent"));
        view.remove(contentUpdater.getComponentWithName("TwitterConnector"));
        view.remove(contentUpdater.getComponentWithName("GitHubConnector"));
        view.remove(contentUpdater.getComponentWithName("NewsFeedConnector"));
        view.remove(contentUpdater.getComponentWithName("ActivityUpdater"));
        view.removeElementsWithNoRelationships();
        view.removeElementsThatCantBeReachedFrom(contentUpdater.getComponentWithName("BadgeAwarder"));
    }

    private static void createComponentViewsForWebApplication(Model model) {
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName("Content Updater");
        Container webApplication = techTribes.getContainerWithName("Web Application");

        // create one component view per Spring controller
        Set<Component> controllers = webApplication.getComponents().stream().filter(c -> c.getTechnology().equals("Spring Controller")).collect(Collectors.toSet());
        for (Component controller : controllers) {
            ComponentView view = model.createComponentView(techTribes, webApplication);
            view.setDescription(controller.getName());
            view.addAllSoftwareSystems();
            view.addAllContainers();
            view.remove(contentUpdater);
            view.addAllComponents();
            view.remove(webApplication.getComponentWithName("LoggingComponent"));
            view.removeElementsThatCantBeReachedFrom(controller);
            view.addAllPeople();
            view.removeElementsWithNoRelationships();
        }
    }

    private static void showViews(Model model) {
        List<View> views = new ArrayList<>();
        views.addAll(model.getSystemContextViews());
        views.addAll(model.getContainerViews());
        views.addAll(model.getComponentViews());

        for (View view : views) {
            System.out.println("Name: " + view.getName());
            System.out.println("Description: " + view.getDescription());
            System.out.println("====================");
            view.getElements().forEach(System.out::println);
            System.out.println("--------------------");
            view.getRelationships().forEach(System.out::println);
            System.out.println("====================");
            System.out.println();
        }
    }


}
