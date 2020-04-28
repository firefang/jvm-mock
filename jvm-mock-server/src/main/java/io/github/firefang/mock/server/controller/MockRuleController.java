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

import io.github.firefang.mock.server.entity.domain.MockRuleDO;
import io.github.firefang.mock.server.entity.form.MockRuleForm;
import io.github.firefang.mock.server.service.MockRuleService;

@RestController
@RequestMapping("/nodes/{nodeId}/rules")
public class MockRuleController {
    private MockRuleService mockRuleSrv;

    public MockRuleController(MockRuleService mockRuleSrv) {
        this.mockRuleSrv = mockRuleSrv;
    }

    @PostMapping
    public Integer add(@PathVariable("nodeId") Integer nodeId, @Validated @RequestBody MockRuleForm form) {
        return mockRuleSrv.add(nodeId, form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        mockRuleSrv.delete(id);
    }

    @PutMapping("/{id}")
    public void update(@Validated @RequestBody MockRuleForm form, @PathVariable("id") Integer id) {
        mockRuleSrv.update(id, form);
    }

    @GetMapping
    public List<MockRuleDO> list(@PathVariable("nodeId") Integer nodeId, MockRuleDO condition) {
        condition.setNodeId(nodeId);
        return mockRuleSrv.list(condition);
    }

    @PostMapping("/{id}/enable")
    public void enable(@PathVariable("id") Integer id) {
        mockRuleSrv.enable(id);
    }

    @PostMapping("/{id}/disable")
    public void disable(@PathVariable("id") Integer id) {
        mockRuleSrv.disable(id);
    }
}
