package je.techtribes.util;

import com.structurizr.annotation.UsesComponent;
import je.techtribes.component.log.LoggingComponent;
import je.techtribes.component.log.LoggingComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractComponent {

    @UsesComponent(description = "Writes logs messages using")
    private LoggingComponent loggingComponent;

    @Autowired
    public void setLoggingComponent(LoggingComponent loggingComponent) {
        this.loggingComponent = loggingComponent;
    }

    public void logDebug(String message) {
        loggingComponent.debug(this, message);
    }

    public void logInfo(String message) {
        loggingComponent.info(this, message);
    }

    public void logWarn(String message) {
        loggingComponent.warn(this, message);
    }

    public void logWarn(ComponentException ce) {
        loggingComponent.warn(this, ce.getMessage(), ce.getCause());
    }

    public void logError(String message) {
        loggingComponent.error(this, message);
    }

    public void logError(ComponentException ce) {
        loggingComponent.error(this, ce.getMessage(), ce.getCause());
    }

}
