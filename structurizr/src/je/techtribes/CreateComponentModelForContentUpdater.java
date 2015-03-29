package je.techtribes;

import com.structurizr.componentfinder.ComponentFinder;
import com.structurizr.componentfinder.JavadocComponentFinderStrategy;
import com.structurizr.componentfinder.SpringComponentFinderStrategy;
import com.structurizr.componentfinder.StructurizrAnnotationsComponentFinderStrategy;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;

import java.io.File;

public class CreateComponentModelForContentUpdater extends AbstractStructurizrModel {

    private String sourceCodePath;

    public static void main(String[] args) throws Exception {
        new CreateComponentModelForContentUpdater(args[0]).run();
    }

    public CreateComponentModelForContentUpdater(String sourceCodePath) throws Exception {
        this.sourceCodePath = sourceCodePath;
    }

    public void run() throws Exception {
        Container container = getContentUpdater();

        ComponentFinder componentFinder = new ComponentFinder(
                container,
                "je.techtribes",
                new SpringComponentFinderStrategy(),
                new StructurizrAnnotationsComponentFinderStrategy(),
                new JavadocComponentFinderStrategy(new File("techtribes-core/src")),
                new JavadocComponentFinderStrategy(new File("techtribes-updater/src")));

        componentFinder.findComponents();

        String currentDirectory = new File(".").getCanonicalPath();
        for (Component component : container.getComponents()) {
            if (component.getSourcePath() != null) {
                component.setSourcePath(component.getSourcePath().replace(currentDirectory, sourceCodePath));
            }
        }

        writeToFile();
    }

}