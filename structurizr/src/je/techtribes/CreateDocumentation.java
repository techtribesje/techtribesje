package je.techtribes;

import com.structurizr.documentation.Documentation;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Type;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;

import java.io.File;

public class CreateDocumentation extends AbstractStructurizrModel {

    public static void main(String[] args) throws Exception {
        new CreateDocumentation().run();
    }

    public CreateDocumentation() throws Exception {
    }

    void run() throws Exception {
        SoftwareSystem techtribesje = getTechTribesSoftwareSystem();
        Container contentUpdater = getContentUpdater();
        Container webApplication = getWebApplication();

        File documentationRoot = new File("./docs");
        Documentation documentation = workspace.getDocumentation();

        documentation.add(techtribesje, Type.Context, Format.Markdown, new File(documentationRoot, "context.md"));
        documentation.add(techtribesje, Type.QualityAttributes, Format.Markdown, new File(documentationRoot, "quality-attributes.md"));
        documentation.add(techtribesje, Type.Constraints, Format.Markdown, new File(documentationRoot, "constraints.md"));
        documentation.add(techtribesje, Type.Principles, Format.Markdown, new File(documentationRoot, "principles.md"));

        documentation.add(techtribesje, Type.Containers, Format.Markdown, new File(documentationRoot, "containers.md"));
        documentation.add(webApplication, Format.Markdown, new File(documentationRoot, "components-web-application.md"));
        documentation.add(contentUpdater, Format.Markdown, new File(documentationRoot, "components-content-updater.md"));

        Component tweetComponent = webApplication.getComponentWithName("TweetComponent");
        documentation.add(tweetComponent, Format.Markdown, new File(documentationRoot, "code-tweet-component.md"));

        documentation.add(techtribesje, Type.InfrastructureArchitecture, Format.Markdown, new File(documentationRoot, "infrastructure-architecture.md"));
        documentation.add(techtribesje, Type.Deployment, Format.Markdown, new File(documentationRoot, "deployment.md"));
        documentation.add(techtribesje, Type.DevelopmentEnvironment, Format.Markdown, new File(documentationRoot, "development-environment.md"));
        documentation.add(techtribesje, Type.OperationAndSupport, Format.Markdown, new File(documentationRoot, "operation-and-support.md"));
        documentation.add(techtribesje, Type.DecisionLog, Format.Markdown, new File(documentationRoot, "decision-log.md"));

        documentation.addImages(documentationRoot);

        writeToFile();
    }

}