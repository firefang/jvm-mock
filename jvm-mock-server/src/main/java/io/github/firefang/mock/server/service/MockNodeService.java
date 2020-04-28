package io.github.firefang.mock.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.firefang.mock.server.entity.domain.MockNodeDO;
import io.github.firefang.mock.server.entity.form.MockNodeForm;
import io.github.firefang.mock.server.mapper.IMockNodeMapper;
import io.github.firefang.mock.server.node.NodeClient;
import io.github.firefang.power.exception.BusinessException;

@Service
public class MockNodeService {
    private IMockNodeMapper mockNodeMapper;
    private MockRuleService mockRuleSrv;
    private NodeClient client;

    public MockNodeService(IMockNodeMapper mockNodeMapper, MockRuleService mockRuleSrv, NodeClient client) {
        this.mockNodeMapper = mockNodeMapper;
        this.mockRuleSrv = mockRuleSrv;
        this.client = client;
    }

    public Integer add(MockNodeForm form) {
        String ip = form.getIp();
        if (mockNodeMapper.findByIp(ip) != null) {
            throw new BusinessException("节点[" + ip + "]已存在");
        }
        MockNodeDO entity = transform(form);
        boolean online = client.ping(ip);
        entity.setOnline(online);
        mockNodeMapper.add(entity);
        return entity.getId();
    }

    public void delete(Integer id) {
        checkExist(id);
        if (!mockRuleSrv.isAllDisabled(id)) {
            throw new BusinessException("节点下存在启用的规则，请全部停用后删除节点");
        }
        mockRuleSrv.deleteByNodeId(id);
        mockNodeMapper.deleteById(id);
    }

    public void update(Integer id, MockNodeForm form) {
        MockNodeDO old = checkExist(id);
        MockNodeDO entity = transform(form);
        entity.setId(id);
        entity.setOnline(old.getOnline());
        mockNodeMapper.updateById(entity);
    }

    public List<MockNodeDO> list(MockNodeDO condition) {
        return mockNodeMapper.find(condition);
    }

    private MockNodeDO transform(MockNodeForm form) {
        MockNodeDO entity = new MockNodeDO();
        entity.setIp(form.getIp());
        entity.setRemark(form.getRemark());
        return entity;
    }

    private MockNodeDO checkExist(Integer id) {
        MockNodeDO entity = mockNodeMapper.findById(id);
        if (entity == null) {
            throw new BusinessException("节点不存在");
        }
        return entity;
    }
}
