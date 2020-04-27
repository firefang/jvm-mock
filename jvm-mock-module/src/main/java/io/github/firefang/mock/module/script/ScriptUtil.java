package io.github.firefang.mock.module.script;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class ScriptUtil {

    public static Object execute(String script, ClassLoader classLoader, Object instance, Object... args)
            throws ScriptException {
        ScriptEngine engine = getEngine();
        Bindings bindings = new SimpleBindings();
        bindings.put("INSTANCE", instance);
        for (int i = 0, len = args.length; i < len; ++i) {
            bindings.put("ARG_" + i, args[i]);
        }
        return engine.eval(script, bindings);
    }

    public static ScriptEngine getEngine() {
        return Singleton.INS.getEngine();
    }

    private static enum Singleton {
        INS;

        private ScriptEngine engine;

        Singleton() {
            ScriptEngineManager manager = new ScriptEngineManager(ScriptUtil.class.getClassLoader());
            engine = manager.getEngineByName("groovy");
        }

        public ScriptEngine getEngine() {
            return engine;
        }

    }
}
