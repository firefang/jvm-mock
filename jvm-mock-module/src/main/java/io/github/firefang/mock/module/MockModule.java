package io.github.firefang.mock.module;

import javax.annotation.Resource;

import org.kohsuke.MetaInfServices;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;

import io.github.firefang.mock.module.server.MockNodeServer;

@MetaInfServices(Module.class)
@Information(id = "mock-module")
public class MockModule implements Module, ModuleLifecycle {
    private MockNodeServer server;

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Override
    public void onLoad() throws Throwable {
        MockController controller = new MockController(moduleEventWatcher);
        server = new MockNodeServer();
        server.init(controller);
        server.start();
    }

    @Override
    public void onUnload() throws Throwable {
        if (server != null) {
            server.destory();
        }
    }

    @Override
    public void loadCompleted() {
    }

    @Override
    public void onActive() throws Throwable {
    }

    @Override
    public void onFrozen() throws Throwable {
    }

}
