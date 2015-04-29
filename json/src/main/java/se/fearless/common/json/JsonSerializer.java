package se.fearless.common.json;

public interface JsonSerializer {

	String toJson(Object object);

	<T> T fromJson(Class<T> klass, String data);
}
