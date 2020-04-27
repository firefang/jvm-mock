package io.github.firefang.mock.module.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.firefang.mock.module.entity.dto.ResponseDTO;

public abstract class BaseReqhandler {
    protected ObjectMapper mapper = new ObjectMapper();

    public void get(HttpServletRequest req, HttpServletResponse resp, String[] pathArgs)
            throws ServletException, IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void post(HttpServletRequest req, HttpServletResponse resp, String[] pathArgs)
            throws ServletException, IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp, String[] pathArgs)
            throws ServletException, IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void put(HttpServletRequest req, HttpServletResponse resp, String[] pathArgs)
            throws ServletException, IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected String getRequestBody(HttpServletRequest req) throws IOException {
        StringWriter sw = new StringWriter(req.getContentLength());
        char[] buf = new char[1024];
        int len;
        try (BufferedReader in = req.getReader()) {
            while ((len = in.read(buf)) != -1) {
                sw.write(buf, 0, len);
            }
        }
        return sw.toString();
    }

    protected <T> T getRequestBodyMapped(HttpServletRequest req, Class<T> clazz) throws IOException {
        String json = getRequestBody(req);
        if (json.length() == 0) {
            return null;
        }
        return mapper.readValue(json, clazz);
    }

    protected void writeResponse(HttpServletResponse resp, int status, ResponseDTO content) throws IOException {
        resp.setStatus(status);
        if (content == null) {
            return;
        }
        resp.setCharacterEncoding("utf-8");
        String json = mapper.writeValueAsString(content);
        try (PrintWriter w = resp.getWriter()) {
            w.write(json);
        }
    }

    protected void writeResponse(HttpServletResponse resp, ResponseDTO content) throws IOException {
        writeResponse(resp, HttpServletResponse.SC_OK, content);
    }
}
