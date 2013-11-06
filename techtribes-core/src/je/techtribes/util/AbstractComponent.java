package je.techtribes.util;

import je.techtribes.component.log.LoggingComponentFactory;

public abstract class AbstractComponent {

    public void logDebug(String message) {
        LoggingComponentFactory.create().debug(this, message);
    }

    public void logInfo(String message) {
        LoggingComponentFactory.create().info(this, message);
    }

    public void logWarn(String message) {
        LoggingComponentFactory.create().warn(this, message);
    }

    public void logWarn(ComponentException ce) {
        LoggingComponentFactory.create().warn(this, ce.getMessage(), ce.getCause());
    }

    public void logError(String message) {
        LoggingComponentFactory.create().error(this, message);
    }

    public void logError(ComponentException ce) {
        LoggingComponentFactory.create().error(this, ce.getMessage(), ce.getCause());
    }

}
