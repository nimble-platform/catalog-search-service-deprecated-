package de.biba.triple.store.access.enums;

public enum Operator {

	EQUAL, GREATER_THAN, EQUAL_TO_OR_GREATER, SMALLER_THAN, EQUAL_TO_OR_SMALLER;

	public String toString() {
		switch (this) {
		case EQUAL:
			return "=";

		case GREATER_THAN:
			return ">";
		case EQUAL_TO_OR_GREATER:
			return ">=";
		case SMALLER_THAN:
			return "<";
		case EQUAL_TO_OR_SMALLER:
			return "<=";
		}

		return "??";
	}
}
