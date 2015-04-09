package se.fearlessgames.common.lua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuaRuntimeErrorLogger implements LuaRuntimeErrorListener {
	private static Logger log = LoggerFactory.getLogger(LuaRuntimeErrorLogger.class);

	@Override
	public void runtimeError(Object functionObject, String error, String stacktrace, Exception cause) {
		StringBuilder msg = new StringBuilder();
		msg.append("Error: ").append(error);
		if (functionObject != null) {
			msg.append(" from: ").append(functionObject);
		}
		log.warn(msg.toString());
		if (stacktrace != null) {
			log.warn("Stacktrace: " + stacktrace);
		}
		if (cause != null) {
			StringBuilder builder = new StringBuilder();
			builder.append("Java stacktrace: ").append(cause.getMessage()).append("\n");
			for (StackTraceElement element : cause.getStackTrace()) {
				builder.append("at ").append(element.getClassName()).append(".").
						append(element.getMethodName()).append(":").append(element.getLineNumber()).append("\n");
			}
			log.warn(builder.toString());
		}
	}
}
