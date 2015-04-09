package se.fearless.common.lua;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.converter.KahluaEnumConverter;
import se.krka.kahlua.converter.KahluaNumberConverter;
import se.krka.kahlua.converter.KahluaTableConverter;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.j2se.Kahlua;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.require.Loadfile;
import se.krka.kahlua.require.LuaSourceProvider;
import se.krka.kahlua.require.Require;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class LuaVm {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Kahlua kahlua = new Kahlua();
	private final KahluaThread thread = kahlua.newThread();

	private LuaJavaClassExposer exposer;
	private final Require require;
	private final Loadfile loadFile;

	private LuaRuntimeErrorListener runtimeErrorListener;
	private final LuaSourceProvider luaSourceProvider;

	@Inject
	public LuaVm(LuaSourceProvider luaSourceProvider) {
		this.luaSourceProvider = luaSourceProvider;

		KahluaConverterManager luaConverterManager = kahlua.getConverterManager();
		KahluaNumberConverter.install(luaConverterManager);
		KahluaEnumConverter.install(luaConverterManager);
		new KahluaTableConverter(kahlua.getPlatform()).install(luaConverterManager);

		runtimeErrorListener = new LuaRuntimeErrorLogger();
		require = new Require(luaSourceProvider);
		loadFile = new Loadfile(luaSourceProvider);
		setUpEnvironment();
	}

	public LuaReturn luaCall(Object functionObject, Object... args) {
		LuaReturn ret = kahlua.getCaller().protectedCall(thread, functionObject, args);

		if (!ret.isSuccess()) {
			if (runtimeErrorListener != null) {
				runtimeErrorListener.runtimeError(functionObject, ret.getErrorString(),	ret.getLuaStackTrace(), ret.getJavaException());
			}
		}
		return ret;
	}

	public boolean runLuaFiles(String[] sourceNames) {
		boolean success = true;
		for (final String file : sourceNames) {
			success &= runLua(file);
		}
		return success;
	}

	public void clean() {
		setUpEnvironment();
	}

	private void setUpEnvironment() {
		kahlua.getPlatform().setupEnvironment(kahlua.getEnvironment());

		LuaCompiler.register(kahlua.getEnvironment());
		exposer = new LuaJavaClassExposer(kahlua.getConverterManager(), kahlua.getPlatform(), kahlua.getEnvironment());
		require.install(kahlua.getEnvironment());
		loadFile.install(kahlua.getEnvironment());
	}
	

	public boolean runLua(final String sourceName) {
		logger.info("Loading lua file {}.lua", sourceName);

		LuaClosure closure = loadClosure(sourceName);

		return luaCall(closure).isSuccess();
	}

	public void exposeClass(final Class<?> classToExpose) {
		exposer.exposeClass(Preconditions.checkNotNull(classToExpose));
	}

	public void exposeGlobalFunctions(final Object o) {
		exposer.exposeGlobalFunctions(Preconditions.checkNotNull(o));
	}

	public LuaClosure loadClosure(final String path) {
		try {
			Reader source = luaSourceProvider.getLuaSource(path);
			if (source == null) {
				throw new RuntimeException("Could not find lua source " + path);
			}
			return LuaCompiler.loadis(source, path, kahlua.getEnvironment());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Object> runClosure(LuaClosure closure, Object... parameters) {
		LuaReturn luaReturn = kahlua.getCaller().protectedCall(thread, closure, parameters);
		if (luaReturn.isSuccess()) {
			return luaReturn;
		}

		logger.error(luaReturn.getErrorString());
		logger.error(luaReturn.getLuaStackTrace());
		throw luaReturn.getJavaException();
	}

	public LuaJavaClassExposer getExposer() {
		return exposer;
	}

	public KahluaTable getEnvironment() {
		return kahlua.getEnvironment();
	}

	public void setRuntimeErrorListener(LuaRuntimeErrorListener errorListener) {
		if (errorListener == null) {
			errorListener = new LuaRuntimeErrorLogger();
		}
		this.runtimeErrorListener = errorListener;
	}

	public KahluaThread getThread() {
		return thread;
	}

	public Platform getPlatform() {
		return kahlua.getPlatform();
	}

	public KahluaConverterManager getConverter() {
		return kahlua.getConverterManager();
	}

	public Kahlua getKahlua() {
		return kahlua;
	}
}