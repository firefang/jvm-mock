package io.github.firefang.mock.module.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.firefang.mock.module.MockController;
import io.github.firefang.mock.module.entity.domain.MockConfigDO;
import io.github.firefang.mock.module.entity.dto.ResponseDTO;
import io.github.firefang.mock.module.server.exception.MockException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockNodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MockController controller;
    private static final Map<Pattern, BaseReqhandler> handlers = new HashMap<>();

    public MockNodeServlet(MockController controller) {
        this.controller = controller;
        initReqHandlers();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        for (Entry<Pattern, BaseReqhandler> e : handlers.entrySet()) {
            Pattern pattern = e.getKey();
            Matcher matcher = pattern.matcher(path);
            if (matcher.lookingAt()) {
                int groupCount = matcher.groupCount();
                String[] args = new String[groupCount];
                if (groupCount > 0) {
                    for (int i = 0; i < matcher.groupCount(); i++) {
                        args[i] = matcher.group(i + 1);
                    }
                }
                BaseReqhandler handler = e.getValue();
                String method = req.getMethod().toLowerCase();
                try {
                    switch (method) {
                    case "get":
                        handler.get(req, resp, args);
                        break;
                    case "post":
                        handler.post(req, resp, args);
                        break;
                    case "delete":
                        handler.delete(req, resp, args);
                        break;
                    case "put":
                        handler.put(req, resp, args);
                        break;
                    default:
                        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        break;
                    }
                } catch (UnsupportedOperationException ex) {
                    resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                } catch (Exception ex2) {
                    log.error("处理请求失败", ex2);
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                return;
            }
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void initReqHandlers() {
        handlers.put(Pattern.compile("^/$"), new CreateMockHandler());
        handlers.put(Pattern.compile("^/(\\d+)$"), new DeleteMockHandler());
        handlers.put(Pattern.compile("^/ping/(.+)$"), new PingHandler());
    }

    private class CreateMockHandler extends BaseReqhandler {
        @Override
        public void post(HttpServletRequest req, HttpServletResponse resp, String[] pathArgs)
                throws ServletException, IOException {
            MockConfigDO config = getRequestBodyMapped(req, MockConfigDO.class);
            ResponseDTO dto = new ResponseDTO();
            dto.setCode(ResponseDTO.SOURCE + ResponseDTO.BUS_ERR);
            if (config == null) {
                dto.setMsg("请求体不能为空");
                writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, dto);
                return;
            }
            StringBuilder errMsg = new StringBuilder();
            if (config.getClassPattern() == null || config.getClassPattern().length() == 0) {
                errMsg.append("classPattern不能为空;");
            }
            if (config.getMethodPattern() == null || config.getMethodPattern().length() == 0) {
                errMsg.append("methodPattern不能为空;");
            }
            if (config.getScript() == null || config.getScript().length() == 0) {
                errMsg.append("script不能为空;");
            }
            if (errMsg.length() > 0) {
                dto.setMsg(errMsg.toString());
                writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, dto);
                return;
            }
            try {
                int id = controller.enableMock(config);
                dto.setCode(ResponseDTO.SUCCESS);
                dto.setData(id);
            } catch (MockException e) {
                dto.setMsg("规则冲突 : " + e.getConflictClassPattern() + "#" + e.getConflictMethodPattern());
            }
            writeResponse(resp, dto);
        }
    }

    private class DeleteMockHandler extends BaseReqhandler {
        @Override
        public void delete(HttpServletRequest req, HttpServletResponse resp, String[] pathArgs)
                throws ServletException, IOException {
            int id;
            try {
                id = Integer.valueOf(pathArgs[0]).intValue();
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, pathArgs[0] + "不能转换为整数");
                return;
            }
            ResponseDTO dto = new ResponseDTO();
            if (controller.disableMock(id)) {
                dto.setCode(ResponseDTO.SUCCESS);
            } else {
                dto.setCode(ResponseDTO.SOURCE + ResponseDTO.BUS_ERR);
            }
            writeResponse(resp, dto);
        }
    }

    private class PingHandler extends BaseReqhandler {
        @Override
        public void get(HttpServletRequest req, HttpServletResponse resp, String[] pathArgs)
                throws ServletException, IOException {
            String token = pathArgs[0];
            ResponseDTO dto = new ResponseDTO();
            dto.setCode(ResponseDTO.SUCCESS);
            dto.setData(token);
            writeResponse(resp, dto);
        }
    }
}
