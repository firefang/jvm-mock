package io.github.firefang.mock.server.mapper;

import java.util.List;

import io.github.firefang.mock.server.entity.domain.MockNodeDO;

public interface IMockNodeMapper {

    void add(MockNodeDO entity);

    void deleteById(Integer id);

    void updateById(MockNodeDO entity);

    List<MockNodeDO> find(MockNodeDO condition);

    MockNodeDO findById(Integer id);

    MockNodeDO findByIp(String ip);
}
