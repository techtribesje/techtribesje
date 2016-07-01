The following diagram shows the logical containers that make up the techtribes.je system. The diagram does not represent the physical number and location of containers - please see the [infrastructure](#InfrastructureArchitecture) and [deployment](#Deployment) sections for this information.

![techtribes.je - Containers](embed:Containers)

- __Web Application__: an Apache Tomcat 7 web server that is the single point of access for the techtribes.je website from the Internet.
- __Content Updater__: a standalone Java 7 application that updates information from Twitter, GitHub and blogs.
- __Relational Database__: a MySQL database that stores the majority of the data behind the techtribes.je website.
- __NoSQL Data Store__: a MongoDB database that stores the tweets and blog posts.
- __File System__: the file system stores Lucene search indexes.