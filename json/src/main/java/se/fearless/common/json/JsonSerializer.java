package se.fearless.common.json;

public interface JsonSerializer {

	String toJson(Object object);

	<T> T fromJson(String data, Class<T> klass);
}
