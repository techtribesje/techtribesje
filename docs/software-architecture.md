This section provides an overview of the techtribes.je software architecture.

## Containers

The following diagram shows the logical containers that make up the techtribes.je system. The diagram does not represent the physical number and location of containers - please see the [infrastructure](#InfrastructureArchitecture) and [deployment](#Deployment) sections for this information.

![techtribes.je - Containers](structurizr:2)

- __Web Application__: an Apache Tomcat 7 web server that is the single point of access for the techtribes.je website from the Internet.
- __Content Updater__: a standalone Java 7 application that updates information from Twitter, GitHub and blogs.
- __Relational Database__: a MySQL database that stores the majority of the data behind the techtribes.je website.
- __NoSQL Data Store__: a MongoDB database that stores the tweets and blog posts.
- __File System__: the file system stores Lucene search indexes.

## Components - Content Updater 

The following diagram shows the components that make up the standalone Content Updater.

![techtribes.je - Content Updater - Components](structurizr:4)

In addition to some core components (detailed later), the standalone Content Updater process consists of the following components:

 - __Scheduled Content Updater__: This component orchestrates the update of information from Twitter, GitHub and blogs on a scheduled basis (i.e. every fifteen minutes). It also recalculates the "recent activity" and awards badges once per hour. It's a Spring Bean that uses the Spring scheduling annotations. See [je.techtribes.component.scheduledcontentupdater](https://github.com/techtribesje/techtribesje/tree/master/techtribes-updater/src/je/techtribes/component/scheduledcontentupdater) for the code.
 - __Twitter Connector__:  This component is responsible for connecting to Twitter in order to refresh profile information and retrieve tweets. It's a Spring Bean that uses the [Twitter4J library](http://twitter4j.org). Both of the REST and streaming APIs are used. See [je.techtribes.component.twitterconnector](https://github.com/techtribesje/techtribesje/tree/master/techtribes-updater/src/je/techtribes/component/twitterconnector) for the code.
 - __GitHub Connector__:  This component is responsible for connecting to GitHub in order to refresh repository information. It's a Spring Bean that uses the [Eclipse Mylyn GitHub connector](http://www.eclipse.org/mylyn/). See [je.techtribes.component.githubconnector](https://github.com/techtribesje/techtribesje/tree/master/techtribes-updater/src/je/techtribes/component/githubconnector) for the code.
 - __News Feed Connector__:  This component is responsible for connecting to RSS/Atom feeds in order to refresh the news and blog posts aggregated into the techtribes.je website. It's a Spring Bean that uses the [ROME library](http://rometools.github.io/rome/). See [je.techtribes.component.newsfeedconnector](https://github.com/techtribesje/techtribesje/tree/master/techtribes-updater/src/je/techtribes/component/newsfeedconnector) for the code.

## Components - Core 

The following components are used by the Web Application and the standalone Content Updater.

- __Content Source Component__: This component provides access to information about people and tribes (together, referred to as "content sources"), which are stored in MySQL. See [je.techtribes.component.contentsource](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/contentsource) for the code. 
- __News Feed Entry Component__: This component provides access to news and blog posts, which are stored in MongoDB. See [je.techtribes.component.newsfeedentry](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/newsfeedentry) for the code. 
- __Tweet Component__: This component provides access to tweets, which are stored in MongoDB. See [je.techtribes.component.tweet](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/tweet) for the code. 
- __Talk Component__: This component provides access to information about talks by local speakers, which are stored in MySQL. See [je.techtribes.component.talk](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/talk) for the code. 
- __Event Component__: This component provides access to information about local events (e.g. meetups, seminars, coding dojos, etc), which are stored in MySQL. See [je.techtribes.component.event](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/event) for the code. 
- __Job Component__: This component provides access to information about local job openings, which are stored in MySQL. See [je.techtribes.component.job](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/job) for the code. 
- __GitHub Component__: This component provides access to information about code repositories belonging to local people/tribes, which are stored in MySQL. See [je.techtribes.component.github](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/github) for the code. 
- __Search Component__: This component provides search facilities across the news, blog posts and tweets. Apache Lucene is used for the indexing and searching. See [je.techtribes.component.search](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/search) for the code. 
- __Activity Component__: This component provides access to the "recent activity" information, which is stored in MySQL and calculated by the Content Updater component. See [je.techtribes.component.activity](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/activity) for the code. 
- __Badge Component__: This component provides access to the badges that have been awarded to people/tribes as a result of their activity. See [je.techtribes.component.badge](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/badge) for the code. 
- __Logging Component__: This is a simple wrapper around Commons Logging and log4j. It's used by all other components. See [je.techtribes.component.log](https://github.com/techtribesje/techtribesje/tree/master/techtribes-core/src/je/techtribes/component/log) for the code.