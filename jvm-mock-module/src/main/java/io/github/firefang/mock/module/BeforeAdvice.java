package io.github.firefang.mock.module;

import com.alibaba.jvm.sandbox.api.ProcessControlException;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;

import io.github.firefang.mock.module.script.ScriptUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeforeAdvice extends AdviceListener {
    private String script;

    public BeforeAdvice(String script) {
        this.script = script;
    }

    @Override
    protected void before(Advice advice) throws Throwable {
        try {
            Object target = advice.getTarget();
            Object ret = ScriptUtil.execute(script, target.getClass().getClassLoader(), target,
                    advice.getParameterArray());
            if (ret == null) {
                log.warn("脚本返回NULL，忽略Mock");
                return;
            }
            ProcessController.returnImmediately(ret);
        } catch (ProcessControlException pe) {
            throw pe;
        } catch (Exception e) {
            ProcessController.throwsImmediately(e);
        }
    }

}
