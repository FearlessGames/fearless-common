package se.fearless.common.json;

import com.google.gson.Gson;

public class GsonSerializer implements JsonSerializer {

	private final Gson gson;

	public GsonSerializer() {
		gson = new Gson();
	}


	@Override
	public String toJson(Object object) {
		return gson.toJson(object);
	}

	@Override
	public <T> T fromJson(String data, Class<T> klass) {
		return gson.fromJson(data, klass);
	}
}
