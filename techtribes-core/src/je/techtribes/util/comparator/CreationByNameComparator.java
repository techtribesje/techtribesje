package je.techtribes.util.comparator;

import je.techtribes.domain.Creation;

import java.util.Comparator;

/**
 * Compares Creation objects by their name.
 */
public class CreationByNameComparator implements Comparator<Creation> {

	public int compare(Creation c1, Creation c2) {
		return c1.getName().compareTo(c2.getName());
	}

}