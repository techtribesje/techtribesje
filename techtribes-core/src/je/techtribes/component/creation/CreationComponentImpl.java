package je.techtribes.component.creation;

import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.ContentSource;
import je.techtribes.domain.Creation;
import je.techtribes.util.AbstractComponent;
import je.techtribes.util.comparator.CreationByNameComparator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CreationComponentImpl extends AbstractComponent implements CreationComponent {

    private ContentSourceComponent contentSourceComponent;

    CreationComponentImpl(ContentSourceComponent contentSourceComponent) {
        this.contentSourceComponent = contentSourceComponent;
    }

    @Override
    public List<Creation> getCreations() {
        List<Creation> creations = new ArrayList<>();

        try {
            creations.add(createCreation(1,
                    "techtribes.je",
                    "techtribes.je is the only way to keep up to date with the IT, tech and digital sector in Jersey and Guernsey, Channel Islands.",
                    "https://techtribes.je",
                    "simonbrown"));

            creations.add(createCreation(2,
                    "Structurizr",
                    "Structurizr is an implementation of the C4 model as described in Simon Brown's Software Architecture for Developers book, which provides a way to easily and effectively communicate the software architecture of a software system. Structurizr allows you to create software architecture models and diagrams as code.",
                    "http://www.structurizr.com",
                    "simonbrown"));

            creations.add(createCreation(3,
                    "bit.coin.je",
                    "We aim to build Jersey as the world's first #bitcoinisle. Here you find all the information you'll need to understand Bitcoin & Cryptocurrencies, how to get started with using it & which locally based companies are Bitcoin-friendly.",
                    "http://bit.coin.je/",
                    "robbieandrews"));
        } catch (Exception e) {
            logError(e.getMessage());
            throw new CreationException("Error getting list of creations", e);
        }

        Collections.sort(creations, new CreationByNameComparator());

        return creations;
    }

    @Override
    public List<Creation> getCreations(ContentSource contentSource) {
        if (contentSource == null) {
            return new ArrayList<>();
        }

        List<Creation> creations = getCreations();
        List<Creation> filteredCreations = new ArrayList<>();
        for (Creation creation : creations) {
            if (contentSource.equals(creation.getContentSource())) {
                filteredCreations.add(creation);
            }
        }

        return filteredCreations;
    }

    private Creation createCreation(int id, String name, String description, String url, String creator) throws Exception {
        Creation creation = new Creation(id);
        creation.setName(name);
        creation.setDescription(description);
        creation.setUrl(new URL(url));
        creation.setContentSource(contentSourceComponent.findByShortName(creator));

        return creation;
    }

}