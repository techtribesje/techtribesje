package je.techtribes.component.scheduledcontentupdater;

import com.structurizr.annotation.Component;

/**
 * Refreshes the data behind techtribes.je on a scheduled basis.
 */
@Component
public interface ScheduledContentUpdater {

    public void start();

    public void stop();

}