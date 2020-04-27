package io.github.firefang.mock.module;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.util.GaStringUtils;

import io.github.firefang.mock.module.entity.domain.MockConfigDO;
import io.github.firefang.mock.module.server.exception.MockException;
import lombok.Data;

public class MockController {
    private ModuleEventWatcher moduleEventWatcher;
    private Map<Integer, MockPattern> patterns = new ConcurrentHashMap<>(48);

    public MockController(ModuleEventWatcher moduleEventWatcher) {
        this.moduleEventWatcher = moduleEventWatcher;
    }

    public synchronized int enableMock(MockConfigDO config) throws MockException {
        MockPattern pattern = new MockPattern(config);
        String classPattern = pattern.getClassPattern();
        String methodPattern = pattern.getMethodPattern();
        for (MockPattern mp : patterns.values()) {
            if (GaStringUtils.matching(classPattern, mp.getClassPattern())
                    && GaStringUtils.matching(methodPattern, mp.getMethodPattern())) {
                // 规则冲突
                throw new MockException(mp.getClassPattern(), mp.getMethodPattern());
            }
        }
        EventWatcher watcher = new EventWatchBuilder(moduleEventWatcher).onClass(config.getClassPattern())
                .onBehavior(config.getMethodPattern()).onWatching().onWatch(new BeforeAdvice(config.getScript()));
        int watchId = watcher.getWatchId();
        patterns.put(watchId, pattern);
        return watchId;
    }

    public boolean disableMock(int watcherId) {
        boolean[] ret = new boolean[1];
        patterns.computeIfPresent(watcherId, (k, v) -> {
            moduleEventWatcher.delete(watcherId);
            ret[0] = true;
            return null;
        });
        return ret[0];
    }

    @Data
    private class MockPattern {
        private String classPattern;
        private String methodPattern;

        public MockPattern(MockConfigDO config) {
            this.classPattern = config.getClassPattern();
            this.methodPattern = config.getMethodPattern();
        }

    }
}
