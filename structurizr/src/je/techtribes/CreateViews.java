package je.techtribes;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateViews extends AbstractUtility {

    public static void main(String[] args) throws Exception {
        Workspace workspace = readFromFile();

        createSystemContextView(workspace);
        createContainerView(workspace);
        createComponentViewsForContentUpdater(workspace);
        createComponentViewsForWebApplication(workspace);

        showViews(workspace);

        writeToFile(workspace);
    }

    private static void createSystemContextView(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        SystemContextView contextView = viewSet.createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
    }

    private static void createContainerView(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        ContainerView containerView = viewSet.createContainerView(techTribes);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();
    }

    private static void createComponentViewsForContentUpdater(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName("Content Updater");
        Container webApplication = techTribes.getContainerWithName("Web Application");

        ComponentView view = viewSet.createComponentView(techTribes, contentUpdater);
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

        view = viewSet.createComponentView(techTribes, contentUpdater);
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

        view = viewSet.createComponentView(techTribes, contentUpdater);
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

    private static void createComponentViewsForWebApplication(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName("Content Updater");
        Container webApplication = techTribes.getContainerWithName("Web Application");

        // create one component view per Spring controller
        Set<Component> controllers = webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals("Spring Controller")).collect(Collectors.toSet());
        for (Component controller : controllers) {
            ComponentView view = viewSet.createComponentView(techTribes, webApplication);
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

    private static void showViews(Workspace workspace) {
        List<View> views = new ArrayList<>();
        views.addAll(workspace.getViews().getSystemContextViews());
        views.addAll(workspace.getViews().getContainerViews());
        views.addAll(workspace.getViews().getComponentViews());

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
