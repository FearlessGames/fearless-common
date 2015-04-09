package se.fearless.common.lua;

import java.util.ArrayList;
import java.util.Collection;

public class MultipleErrorListener implements LuaRuntimeErrorListener {

	Collection<LuaRuntimeErrorListener> listeners;

	public MultipleErrorListener() {
		listeners = new ArrayList<LuaRuntimeErrorListener>();
	}

	public void addErrorListener(LuaRuntimeErrorListener listener) {
		listeners.add(listener);
	}

	@Override
	public void runtimeError(Object functionObject, String error, String stacktrace, Exception cause) {
		for (LuaRuntimeErrorListener listener : listeners) {
			listener.runtimeError(functionObject, error, stacktrace, cause);
		}
	}
}
