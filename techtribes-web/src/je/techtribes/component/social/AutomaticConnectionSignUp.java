package je.techtribes.component.social;

import je.techtribes.util.AbstractComponent;
import je.techtribes.component.contentsource.ContentSourceComponent;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

public class AutomaticConnectionSignUp extends AbstractComponent implements ConnectionSignUp {

    private ContentSourceComponent contentSourceComponent;

    public void setContentSourceComponent(ContentSourceComponent contentSourceComponent) {
        this.contentSourceComponent = contentSourceComponent;
    }

    @Override
    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();

        if (contentSourceComponent.findByTwitterId(profile.getUsername()) != null) {
            logInfo("Automatically signing up twitter ID " + profile.getUsername());

            return profile.getUsername();
        } else {
            logWarn("Somebody tried to sign-in with a Twitter ID of " + profile.getUsername() + " but we're not following them");
            return null;
        }
    }

}
