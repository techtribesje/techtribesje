package je.techtribes;

import com.structurizr.Workspace;
import com.structurizr.model.*;

public class CreateBaseModel extends AbstractStructurizrModel {

    public static void main(String[] args) throws Exception {
        new CreateBaseModel().run();
    }

    public CreateBaseModel() throws Exception {
        this.workspace = new Workspace("techtribes.je", "The software architecture model for the techtribes.je system");
    }

    void run() throws Exception {
        Model model = workspace.getModel();
        SoftwareSystem techTribes = model.addSoftwareSystem(TECHTRIBES_JE, "techtribes.je is the only way to keep up to date with the IT, tech and digital sector in Jersey and Guernsey, Channel Islands.");

        Person anonymousUser = model.addPerson("Anonymous User", "Anybody on the web.");
        Person authenticatedUser = model.addPerson("Aggregated User", "A user or business with content that is aggregated into the website, signed in using their Twitter ID.");
        Person adminUser = model.addPerson("Administration User", "A system administration user, signed in using a Twitter ID.");
        anonymousUser.uses(techTribes, "View people, tribes (businesses, communities and interest groups), content, events, jobs, etc from the local tech, digital and IT sector");
        authenticatedUser.uses(techTribes, "Manage user profile and tribe membership");
        adminUser.uses(techTribes, "Add people, add tribes and manage tribe membership");

        SoftwareSystem twitter = model.addSoftwareSystem("Twitter", "");
        techTribes.uses(twitter, "Gets profile information and tweets from");

        SoftwareSystem gitHub = model.addSoftwareSystem("GitHub", "");
        techTribes.uses(gitHub, "Gets information about public code repositories from");

        SoftwareSystem blogs = model.addSoftwareSystem("Blogs", "");
        techTribes.uses(blogs, "Gets content using RSS and Atom feeds from");

        Container webApplication = techTribes.addContainer("Web Application", "Allows users to view people, tribes, content, events, jobs, etc from the local tech, digital and IT sector.", "Apache Tomcat 7.x");
        Container contentUpdater = techTribes.addContainer("Content Updater", "Updates profiles, tweets, GitHub repos and content on a scheduled basis.", "Standalone Java 7 application");
        Container relationalDatabase = techTribes.addContainer("Relational Database", "Stores people, tribes, tribe membership, talks, events, jobs, badges, GitHub repos, etc.", "MySQL 5.5.x");
        relationalDatabase.addTags(DATABASE_TAG);
        Container noSqlStore = techTribes.addContainer("NoSQL Data Store", "Stores content from RSS/Atom feeds (blog posts) and tweets.", "MongoDB 2.2.x");
        noSqlStore.addTags(DATABASE_TAG);
        Container fileSystem = techTribes.addContainer("File System", "Stores search indexes.", "Local disk");
        fileSystem.addTags(FILE_SYSTEM_TAG);

        anonymousUser.uses(webApplication, "View people, tribes (businesses, communities and interest groups), content, events, jobs, etc from the local tech, digital and IT sector.");
        authenticatedUser.uses(webApplication, "Manage user profile and tribe membership.");
        adminUser.uses(webApplication, "Add people, add tribes and manage tribe membership.");

        webApplication.uses(relationalDatabase, "Reads from and writes data to");
        webApplication.uses(noSqlStore, "Reads from");
        webApplication.uses(fileSystem, "Reads from");

        contentUpdater.uses(relationalDatabase, "Reads from and writes data to");
        contentUpdater.uses(noSqlStore, "Reads from and writes data to");
        contentUpdater.uses(fileSystem, "Writes to");
        contentUpdater.uses(twitter, "Gets profile information and tweets from.");
        contentUpdater.uses(gitHub, "Gets information about public code repositories from.");
        contentUpdater.uses(blogs, "Gets content using RSS and Atom feeds from.");

        writeToFile();
    }

}