package se.fearless.common.stats;

import se.fearless.common.publisher.SimplePublisher;
import se.fearless.common.publisher.Subscriber;

public abstract class AbstractStat implements Stat {

	private final String name;
	protected SimplePublisher<Stat> publisher = new SimplePublisher<>();

	protected AbstractStat(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return name + ": " + getValue();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AbstractStat)) {
			return false;
		}

		AbstractStat stat = (AbstractStat) o;

		if (!name.equals(stat.name)) {
			return false;
		}
		return Double.compare(getValue(), stat.getValue()) == 0;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		return (int) (result * 31 + getValue());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void subscribe(Subscriber<Stat> statSubscriber) {
		publisher.subscribe(statSubscriber);
	}

	@Override
	public void unsubscribe(Subscriber<Stat> statSubscriber) {
		publisher.unsubscribe(statSubscriber);
	}
}
