package je.techtribes.updater;

import je.techtribes.component.log.LoggingComponent;
import je.techtribes.component.scheduledcontentupdater.ScheduledContentUpdater;
import je.techtribes.util.DateUtils;
import je.techtribes.util.Version;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.TimeZone;

/**
 * Standalone application that updates tweets, news, badges, etc.
 */
public class Main {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(DateUtils.UTC_TIME_ZONE));

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LoggingComponent loggingComponent = (LoggingComponent)applicationContext.getBean("loggingComponent");
        loggingComponent.info(Main.class, "techtribes.je updater, version " + Version.getBuildNumber() + " built on " + Version.getBuildTimestamp());

        final ScheduledContentUpdater scu = (ScheduledContentUpdater)applicationContext.getBean("contentUpdater");

        Runtime.getRuntime().addShutdownHook(new Thread() {
                 public void run() {
                     scu.stop();
                 }
             });

        scu.start();
    }

}
