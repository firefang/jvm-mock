package io.github.firefang.mock.server.mapper;

import java.util.List;

import io.github.firefang.mock.server.entity.domain.MockRuleDO;

public interface IMockRuleMapper {

    void add(MockRuleDO entity);

    void deleteById(Integer id);

    void updateById(MockRuleDO entity);

    void updateWatcherIdById(MockRuleDO entity);

    List<MockRuleDO> find(MockRuleDO condition);

    MockRuleDO findById(Integer id);

    Long countEnabled(Integer nodeId);

    void deleteByNodeId(Integer nodeId);
}
