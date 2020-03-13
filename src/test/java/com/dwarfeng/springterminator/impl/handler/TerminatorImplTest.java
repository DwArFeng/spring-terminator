package com.dwarfeng.springterminator.impl.handler;

import com.dwarfeng.springterminator.sdk.util.ApplicationUtil;
import com.dwarfeng.springterminator.stack.handler.Terminator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

public class TerminatorImplTest {

    @Test
    public void test() {
        ApplicationUtil.launch("classpath:spring/application-context*.xml");
    }

    @Component
    public static class InternalKiller implements ApplicationListener<ContextRefreshedEvent> {

        @Autowired
        private Terminator terminator;
        @Autowired
        private ThreadPoolTaskExecutor threadPoolTaskExecutor;

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            threadPoolTaskExecutor.execute(() -> terminator.exit(111));
        }
    }
}