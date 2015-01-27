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
                    "https://github.com/techtribesje/techtribesje",
                    "simonbrown"));

            creations.add(createCreation(2,
                    "Structurizr",
                    "Structurizr is an implementation of the C4 model as described in Simon Brown's Software Architecture for Developers book, which provides a way to easily and effectively communicate the software architecture of a software system. Structurizr allows you to create software architecture models and diagrams as code.",
                    "http://www.structurizr.com",
                    null,
                    "simonbrown"));

            creations.add(createCreation(3,
                    "bit.coin.je",
                    "We aim to build Jersey as the world's first #bitcoinisle. Here you find all the information you'll need to understand Bitcoin & Cryptocurrencies, how to get started with using it & which locally based companies are Bitcoin-friendly.",
                    "http://bit.coin.je/",
                    null,
                    "robbieandrews"));

            creations.add(createCreation(4,
                    "collaborate.je",
                    "Created in the wake of Island Innovators 2014, collaborate.je is a community driven group with the primary aims of encouraging collaboration within the creative community in Jersey and encouraging the development of a collaborative creative eco-system.",
                    "http://collaborate.je/",
                    null,
                    "mattchatterley"));

            creations.add(createCreation(5,
                    "Guernsey Bin Nights",
                    "A simple utility app for Android to help people living in Guernsey remember when to put their bin bags out and which bags to put out, e.g. general rubbish, clear recycling bags or blue recycling bags.",
                    "https://play.google.com/store/apps/details?id=gg.kodo.binbags",
                    null,
                    "adrianritchie"));
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

    private Creation createCreation(int id, String name, String description, String url, String codeUrl, String creator) throws Exception {
        Creation creation = new Creation(id);
        creation.setName(name);
        creation.setDescription(description);
        creation.setUrl(new URL(url));
        if (codeUrl != null) {
            creation.setCodeUrl(new URL(codeUrl));
        }
        creation.setContentSource(contentSourceComponent.findByShortName(creator));

        return creation;
    }

}
