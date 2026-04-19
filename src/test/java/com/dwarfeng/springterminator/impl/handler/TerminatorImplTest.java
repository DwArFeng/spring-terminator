package com.dwarfeng.springterminator.impl.handler;

import com.dwarfeng.springterminator.sdk.util.ApplicationUtil;
import com.dwarfeng.springterminator.stack.exception.TerminateException;
import com.dwarfeng.springterminator.stack.handler.TerminateHandler;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
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
        private TerminateHandler terminateHandler;
        @Autowired
        @Qualifier("terminateHandler")
        private TerminateHandler terminateHandlerByName;
        @Autowired
        private ThreadPoolTaskExecutor executor;

        @Override
        public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
            Assert.assertNotNull(terminateHandler);
            Assert.assertSame(terminateHandler, terminateHandlerByName);

            executor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    terminateHandler.exit(0);
                } catch (TerminateException e) {
                    LOGGER.error("异常", e);
                    try {
                        terminateHandler.exit(-1);
                    } catch (TerminateException ex) {
                        LOGGER.error("终止失败", ex);
                    }
                } catch (Exception e) {
                    LOGGER.error("异常", e);
                }
            });
        }
    }
}
