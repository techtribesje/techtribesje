package je.techtribes;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateViews extends AbstractStructurizrModel {

    public static void main(String[] args) throws Exception {
        new CreateViews().run();
    }

    public CreateViews() throws Exception {
    }

    void run() throws Exception {
        createSystemContextView(workspace);
        createContainerView(workspace);
        createComponentViewsForContentUpdater(workspace);
        createComponentViewsForWebApplication(workspace);

        SoftwareSystem techTribes = getTechTribesSoftwareSystem();
        techTribes.addTags(TECHTRIBES_JE);

        ViewSet viewSet = workspace.getViews();

        viewSet.getStyles().add(new ElementStyle(Tags.ELEMENT, null, null, null, null, null));
        viewSet.getStyles().add(new ElementStyle(TECHTRIBES_JE, null, null, "#041F37", "#ffffff", null));
        viewSet.getStyles().add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#A4B7C9", "#000000", null));
        viewSet.getStyles().add(new ElementStyle(Tags.PERSON, null, null, "#728da5", "#ffffff", null));
        viewSet.getStyles().add(new ElementStyle(Tags.CONTAINER, null, null, "#2A4E6E", "#ffffff", null));
        viewSet.getStyles().add(new ElementStyle(Tags.COMPONENT, null, null, "#728DA5", "#ffffff", null));
        viewSet.getStyles().add(new RelationshipStyle(Tags.RELATIONSHIP, null, null, null, null, 500));

        writeToFile();
    }

    private void createSystemContextView(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        SystemContextView contextView = viewSet.createContextView(techTribes);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
    }

    private void createContainerView(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        ContainerView containerView = viewSet.createContainerView(techTribes);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();
    }

    private void createComponentViewsForContentUpdater(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName("Content Updater");
        Container webApplication = techTribes.getContainerWithName("Web Application");

        ComponentView view = viewSet.createComponentView(contentUpdater);
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
        view.addRelationships();

        view = viewSet.createComponentView(contentUpdater);
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
        view.addRelationships();

        view = viewSet.createComponentView(contentUpdater);
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
        view.addRelationships();
    }

    private void createComponentViewsForWebApplication(Workspace workspace) {
        Model model = workspace.getModel();
        ViewSet viewSet = workspace.getViews();
        SoftwareSystem techTribes = model.getSoftwareSystemWithName("techtribes.je");
        Container contentUpdater = techTribes.getContainerWithName("Content Updater");
        Container webApplication = techTribes.getContainerWithName("Web Application");

        // create one component view per Spring controller
        Set<Component> controllers = webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals("Spring Controller")).collect(Collectors.toSet());
        for (Component controller : controllers) {
            ComponentView view = viewSet.createComponentView(webApplication);
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

}
