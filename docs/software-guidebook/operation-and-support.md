## Operation and Support {#sample-guidebook-operation-and-support}This section provides information about the operational and support aspects of the techtribes.je website.

### Starting MySQL

MySQL is installed as a service, and should be running after a server restart. You can check this by using the following command:

	sudo netstat -tap | grep mysql
	
If you need to start MySQL, you can use the following command:

	sudo service mysql start

### Starting MongoDB

MongoDB is also installed as a service, and should be running after a server restart. You can check this by using the following commands:

	sudo netstat -tap | grep mongo
	tail /var/log/mongodb/mongodb.log
	
If you need to start MongoDB, you can use the following command:

	sudo service mongodb start
	
### Starting the Web Server

Apache Tomcat is also installed as a service, and should be running after a server restart. You can check this by using the following commands:

	ps -Af | grep tomcat
	tail /var/lib/tomcat7/logs/catalina.out
	
If you need to start Tomcat, you can use the following command:

	~techtribesje/bin/start-tomcat.sh
	
### Starting the Content Updater

The Content Updater is a standalone Java process that needs to be started manually after a server restart. You can do this with the following command (where XYZ is the build number):

	 ~techtribesje/bin/start-updater.sh XYZ

You can check the log file with the following command:

	~techtribesje/bin/updater-logs.sh XYZ

### Monitoring

The only monitoring on the techtribes.je website is [Pingdom](https://www.pingdom.com/), which is configured to test that the website is still accessible every 5 minutes. An e-mail is sent if the web server is detected to be unavailable.

### Backups

Both the MySQL and MongoDB databases are backed-up daily via a cron job at 3am GMT. You can check that this is scheduled with the following command:

	crontab -l
	
You should see something like this:

	0 3 * * * /home/techtribesje/bin/backup-data.sh > /dev/null
	
This shell script takes an export of the MySQL and MongoDB databases, copying them to a folder that is synced by Dropbox.