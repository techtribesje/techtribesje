package je.techtribes.component.scheduledcontentupdater;

import com.structurizr.annotation.Component;

@Component(description = "Refreshes the data behind techtribes.je on a scheduled basis.")
public interface ScheduledContentUpdater {

    public void start();

    public void stop();

}