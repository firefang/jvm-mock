package io.github.firefang.mock.server.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.firefang.mock.server.entity.domain.MockNodeDO;
import io.github.firefang.mock.server.entity.form.MockNodeForm;
import io.github.firefang.mock.server.service.MockNodeService;
import io.github.firefang.power.web.group.CreateGroup;

@RestController
@RequestMapping("/nodes")
public class MockNodeController {
    private MockNodeService mockNodeSrv;

    public MockNodeController(MockNodeService mockNodeSrv) {
        this.mockNodeSrv = mockNodeSrv;
    }

    @PostMapping
    public Integer add(@Validated(value = CreateGroup.class) @RequestBody MockNodeForm form) {
        return mockNodeSrv.add(form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        mockNodeSrv.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Validated @RequestBody MockNodeForm form, @PathVariable("id") Integer id) {
        mockNodeSrv.update(id, form);
    }

    @GetMapping
    public List<MockNodeDO> list(MockNodeDO condition) {
        return mockNodeSrv.list(condition);
    }
}
