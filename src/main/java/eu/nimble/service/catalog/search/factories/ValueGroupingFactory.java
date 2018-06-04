package eu.nimble.service.catalog.search.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.nimble.service.catalog.search.impl.dao.Group;

public class ValueGroupingFactory {

	public Map<String, List<Group>> generateGrouping(int amountOfGroups, List<String> values,
			String shortPropertyName) {
		Map<String, List<Group>> result = new HashMap<String, List<Group>>();
		if (values != null && values.size() > 0) {

			float min = getMinOfData(values);
			float max = getMaxOfData(values);
			float stepRate = (max - min) / (float) amountOfGroups;
			List<Group> discreditedGroups = new ArrayList<Group>();
			for (int i = 0; i < amountOfGroups; i++) {
				Group group = new Group();
				float newMin = min + (stepRate * i);
				float newMax = min + (stepRate * (i + 1));
				if (newMin > 2) {
					newMin = round(newMin);
					newMax = round(newMax);
				}
				group.setDescription("From: " + newMin + " to " + newMax);
				group.setMin(newMin);
				group.setMax(newMax);
				group.setProperty(shortPropertyName);
				discreditedGroups.add(group);
			}
			result.put(shortPropertyName, discreditedGroups);
		}
		return result;
	}

	private float getMinOfData(List<String> values) {
		float min = 999999;
		for (String value : values) {
			float number = Float.valueOf(value);
			if (number < min) {
				min = number;
			}
		}

		return min;
	}

	private float getMaxOfData(List<String> values) {
		float max = -999999;
		for (String value : values) {
			float number = Float.valueOf(value);
			if (number > max) {
				max = number;
			}
		}

		return max;
	}

	private float round(float value) {
		int n = (int) value * 100;
		return n / 100f;
	}
}
