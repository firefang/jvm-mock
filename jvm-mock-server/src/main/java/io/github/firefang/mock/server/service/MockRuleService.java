package io.github.firefang.mock.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.firefang.mock.server.entity.domain.MockNodeDO;
import io.github.firefang.mock.server.entity.domain.MockRuleDO;
import io.github.firefang.mock.server.entity.form.MockRuleForm;
import io.github.firefang.mock.server.mapper.IMockNodeMapper;
import io.github.firefang.mock.server.mapper.IMockRuleMapper;
import io.github.firefang.mock.server.node.NodeClient;
import io.github.firefang.power.exception.BusinessException;

@Service
public class MockRuleService {
    private IMockRuleMapper mockRuleMapper;
    private IMockNodeMapper mockNodeMapper;
    private NodeClient client;

    public MockRuleService(IMockRuleMapper mockRuleMapper, IMockNodeMapper mockNodeMapper, NodeClient client) {
        this.mockRuleMapper = mockRuleMapper;
        this.mockNodeMapper = mockNodeMapper;
        this.client = client;
    }

    public Integer add(Integer nodeId, MockRuleForm form) {
        MockRuleDO entity = transform(form);
        entity.setNodeId(nodeId);
        mockRuleMapper.add(entity);
        return entity.getId();
    }

    public void delete(Integer id) {
        MockRuleDO old = checkExist(id);
        if (old.getWatcherId() != null) {
            throw new BusinessException("规则已启用，请停用后删除");
        }
        mockRuleMapper.deleteById(id);
    }

    public void update(Integer id, MockRuleForm form) {
        MockRuleDO old = checkExist(id);
        if (old.getWatcherId() != null) {
            throw new BusinessException("规则已启用，请停用后修改");
        }
        MockRuleDO entity = transform(form);
        entity.setId(id);
        mockRuleMapper.updateById(entity);
    }

    public List<MockRuleDO> list(MockRuleDO condition) {
        return mockRuleMapper.find(condition);
    }

    public void enable(Integer id) {
        MockRuleDO rule = checkExist(id);
        String ip = getNodeIp(rule.getNodeId());
        MockRuleForm form = new MockRuleForm();
        form.setClassPattern(rule.getClassPattern());
        form.setMethodPattern(rule.getMethodPattern());
        form.setScript(rule.getScript());
        Integer watcherId = client.enable(ip, form);
        rule.setWatcherId(watcherId);
        mockRuleMapper.updateWatcherIdById(rule);
    }

    public void disable(Integer id) {
        MockRuleDO rule = checkExist(id);
        if (rule.getWatcherId() == null) {
            throw new BusinessException("规则未启用，无法停用");
        }
        String ip = getNodeIp(rule.getNodeId());
        client.disable(ip, rule.getWatcherId());
        rule.setWatcherId(null);
        mockRuleMapper.updateWatcherIdById(rule);

    }

    // ------------------------------------------------------------

    public boolean isAllDisabled(Integer nodeId) {
        return mockRuleMapper.countEnabled(nodeId) == 0;
    }

    public void deleteByNodeId(Integer nodeId) {
        mockRuleMapper.deleteByNodeId(nodeId);
    }

    private MockRuleDO transform(MockRuleForm form) {
        MockRuleDO entity = new MockRuleDO();
        entity.setClassPattern(form.getClassPattern());
        entity.setMethodPattern(form.getMethodPattern());
        entity.setScript(form.getScript());
        return entity;
    }

    private MockRuleDO checkExist(Integer id) {
        MockRuleDO entity = mockRuleMapper.findById(id);
        if (entity == null) {
            throw new BusinessException("规则不存在");
        }
        return entity;
    }

    private String getNodeIp(Integer nodeId) {
        MockNodeDO node = mockNodeMapper.findById(nodeId);
        if (node == null) {
            throw new BusinessException("节点不存在");
        }
        return node.getIp();
    }
}
