package se.fearless.common.stats;

public interface StatType<T extends StatType> extends Comparable<T> {
	String getName();
}
