package io.github.firefang.mock.module.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import io.github.firefang.mock.module.MockController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockNodeServer {
    private static final int PORT = 47996;
    private Server httpServer;

    public void init(MockController controller) {
        initServer();
        initContextHandler(controller);
    }

    public void start() throws Exception {
        httpServer.start();
    }

    public void destory() {
        if (httpServer != null) {
            try {
                httpServer.stop();
            } catch (Exception e) {
                log.warn("停止HTTP Server失败", e);
            }
        }
    }

    private void initServer() {
        httpServer = new Server(PORT);
        QueuedThreadPool qtp = (QueuedThreadPool) httpServer.getThreadPool();
        qtp.setDaemon(true);
        qtp.setName("mock-node-pool");
    }

    private void initContextHandler(MockController controller) {
        ServletHandler handler = new ServletHandler();
        httpServer.setHandler(handler);
        ServletHolder holder = new ServletHolder(new MockNodeServlet(controller));
        handler.addServletWithMapping(holder, "/mock-node/*");
    }
}
