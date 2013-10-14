package je.techtribes.component.contentsource;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.component.log.LoggingComponentFactory;
import je.techtribes.domain.ContentItem;
import je.techtribes.domain.ContentSource;

import java.util.Collection;
import java.util.Iterator;

/**
 * Filters a given collection of ContentItem objects remove those that don't have valid ContentSource references.
 */
public class ContentItemFilter<T extends ContentItem> {

    public void filter(Collection<T> contentItems, ContentSourceComponent contentSourceComponent, boolean includeContentSourcesNotBeingAggregated) {
        Iterator<T> it = contentItems.iterator();
        while (it.hasNext()) {
            ContentItem contentItem = it.next();
            ContentSource contentSource = contentSourceComponent.findById(contentItem.getContentSourceId());
            if (contentSource != null && (includeContentSourcesNotBeingAggregated || contentSource.isContentAggregated())) {
                contentItem.setContentSource(contentSource);
            } else {
                LoggingComponentFactory.create().debug(this, "Filtering " + contentItem.getClass().getSimpleName() + " with associated content source " + contentItem.getContentSourceId());
                it.remove();
            }
        }
    }

}
