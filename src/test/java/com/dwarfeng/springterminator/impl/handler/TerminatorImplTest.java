package com.dwarfeng.springterminator.impl.handler;

import com.dwarfeng.springterminator.sdk.util.ApplicationUtil;
import com.dwarfeng.springterminator.stack.handler.Terminator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        private static final Logger LOGGER = LoggerFactory.getLogger(InternalKiller.class);

        @Autowired
        private Terminator terminator;
        @Autowired
        private ThreadPoolTaskExecutor executor;

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            executor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    terminator.exit(0);
                } catch (Exception e) {
                    LOGGER.error("异常", e);
                    terminator.exit(-1);
                }
            });
        }
    }
}