package se.fearless.common.lua;


public interface LuaRuntimeErrorListener {
	void runtimeError(Object functionObject, String error, String stacktrace, Exception cause);
}
