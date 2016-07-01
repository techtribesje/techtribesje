package je.techtribes;

import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
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
        createSystemContextView();
        createContainerView();
        createComponentViewsForContentUpdater();
        createComponentViewsForWebApplication();
        addStyles();

        writeToFile();
    }

    private void createSystemContextView() {
        SoftwareSystem techTribes = getTechTribesSoftwareSystem();
        SystemContextView contextView = workspace.getViews().createSystemContextView(techTribes, "Context", null);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
    }

    private void createContainerView() {
        SoftwareSystem techTribes = getTechTribesSoftwareSystem();
        ContainerView containerView = workspace.getViews().createContainerView(techTribes, "Containers", null);
        containerView.addAllSoftwareSystems();
        containerView.addAllPeople();
        containerView.addAllContainers();
    }

    private void createComponentViewsForContentUpdater() {
        Container contentUpdater = getContentUpdater();
        Container webApplication = getWebApplication();

        ComponentView view = workspace.getViews().createComponentView(contentUpdater, "UpdateInformation", "Updating information from external systems");
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

        view = workspace.getViews().createComponentView(contentUpdater, "RecentActivity", "Updating recent activity");
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

        view = workspace.getViews().createComponentView(contentUpdater, "AwardingBadges", "Awarding badges");
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

    private void createComponentViewsForWebApplication() {
        Container contentUpdater = getContentUpdater();
        Container webApplication = getWebApplication();

        // create one component view per Spring controller
        Set<Component> controllers = webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER)).collect(Collectors.toSet());
        for (Component controller : controllers) {
            ComponentView view = workspace.getViews().createComponentView(webApplication, controller.getName(), "This diagram shows a slice through the web application, starting at " + controller.getName() + ".");
            view.addAllSoftwareSystems();
            view.addAllContainers();
            view.remove(contentUpdater);
            view.addAllComponents();
            view.remove(webApplication.getComponentWithName("LoggingComponent"));
            view.removeElementsThatCantBeReachedFrom(controller);
            view.addAllPeople();
            view.removeElementsWithNoRelationships();
        }

        ComponentView view = workspace.getViews().createComponentView(webApplication, "AllComponents", "All components");
        view.addAllComponents();
        view.addAllPeople();
        view.addAllContainers();
        view.remove(contentUpdater);
    }

    private void addStyles() {
        SoftwareSystem techTribes = getTechTribesSoftwareSystem();
        techTribes.addTags(TECHTRIBES_JE);

        Styles styles = workspace.getViews().getConfiguration().getStyles();
        styles.add(new ElementStyle(Tags.ELEMENT, null, null, null, null, null));
        styles.add(new ElementStyle(TECHTRIBES_JE, null, null, "#041F37", "#ffffff", null));
        styles.add(new ElementStyle(Tags.SOFTWARE_SYSTEM, null, null, "#A4B7C9", "#000000", null));
        styles.add(new ElementStyle(Tags.PERSON, null, null, "#728da5", "#ffffff", null, Shape.Person));
        styles.add(new ElementStyle(Tags.CONTAINER, null, null, "#2A4E6E", "#ffffff", null));
        styles.add(new ElementStyle(DATABASE_TAG, null, null, null, null, null, Shape.Cylinder));
        styles.add(new ElementStyle(FILE_SYSTEM_TAG, null, null, null, null, null, Shape.Folder));
        styles.add(new ElementStyle(Tags.COMPONENT, null, null, "#728DA5", "#ffffff", null));
        styles.addRelationshipStyle(Tags.RELATIONSHIP).width(500);
    }

}