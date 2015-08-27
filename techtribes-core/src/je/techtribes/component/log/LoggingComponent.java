package je.techtribes.component.log;

import com.structurizr.annotation.Component;

/**
 * Provides logging facilities to all other components.
 */
@Component
public interface LoggingComponent {

    void debug(Object caller, String message);

    void info(Object caller, String message);

    void warn(Object caller, String message);

    void warn(Object caller, String message, Throwable throwable);

    void error(Object caller, String message);

    void error(Object caller, String message, Throwable throwable);

}
