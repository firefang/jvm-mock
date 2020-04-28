package io.github.firefang.mock.server.node;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.github.firefang.mock.server.entity.domain.MockRuleDO;
import io.github.firefang.mock.server.entity.dto.ResponseDTO;
import io.github.firefang.power.exception.BusinessException;

@Component
public class NodeClient {
    private static final String CONTEXT_PATH = "/mock-node";
    private static final String PING_PATH = CONTEXT_PATH + "/ping/%d";
    private static final String ENABLE_PATH = CONTEXT_PATH + "/";
    private static final String DISABLE_PATH = CONTEXT_PATH + "/%d";

    private RestTemplate http;

    public NodeClient() {
        http = new RestTemplate();
        http.setErrorHandler(new NoopErrorHandler());
    }

    public boolean ping(String ip) {
        long t = System.nanoTime();
        String url = getUrl(ip, String.format(PING_PATH, t));
        ResponseEntity<ResponseDTO> resp;
        try {
            resp = http.getForEntity(url, ResponseDTO.class);
        } catch (RestClientException e) {
            return false;
        }
        if (resp.getStatusCode() == HttpStatus.OK) {
            ResponseDTO body = resp.getBody();
            return body.getCode() == ResponseDTO.SUCCESS && Long.valueOf((String) body.getData()).longValue() == t;
        }
        return false;
    }

    public Integer enable(String ip, MockRuleDO rule) {
        String url = getUrl(ip, ENABLE_PATH);
        ResponseEntity<ResponseDTO> resp;
        try {
            resp = http.postForEntity(url, rule, ResponseDTO.class);
        } catch (RestClientException e) {
            throw new BusinessException("请求节点失败");
        }
        return checkAndGetData(resp);
    }

    public boolean disable(String ip, Integer id) {
        String url = getUrl(ip, String.format(DISABLE_PATH, id));
        ResponseEntity<ResponseDTO> resp;
        try {
            resp = http.exchange(url, HttpMethod.DELETE, null, ResponseDTO.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new BusinessException("请求节点失败");
        }
        return checkAndGetData(resp);
    }

    @SuppressWarnings("unchecked")
    private <T> T checkAndGetData(ResponseEntity<ResponseDTO> resp) {
        ResponseDTO body = resp.getBody();
        if (resp.getStatusCode() == HttpStatus.OK) {
            if (body.getCode() == ResponseDTO.SUCCESS) {
                return (T) body.getData();
            }
        }
        throw new BusinessException(body.getMsg());
    }

    private String getUrl(String ip, String path) {
        return "http://" + ip + ":47996" + path;
    }

    private static class NoopErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
        }
    }
}
