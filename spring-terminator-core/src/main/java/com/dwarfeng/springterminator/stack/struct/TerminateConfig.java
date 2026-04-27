package com.dwarfeng.springterminator.stack.struct;

import com.dwarfeng.dutil.basic.prog.Buildable;
import com.dwarfeng.springterminator.stack.util.TerminateConfigUtil;

/**
 * 终止处理器配置。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class TerminateConfig {

    /**
     * 前置延时。
     *
     * <p>
     * 小于等于 0 时表示不启用前置延时。
     */
    private final long preDelay;

    /**
     * 后置延时。
     *
     * <p>
     * 小于等于 0 时表示不启用后置延时。
     */
    private final long postDelay;

    public TerminateConfig(long preDelay, long postDelay) {
        this(preDelay, postDelay, false);
    }

    private TerminateConfig(long preDelay, long postDelay, boolean paramReliable) {
        // 如果参数不可靠，则检查参数。
        if (!paramReliable) {
            TerminateConfigUtil.checkPreDelay(preDelay);
            TerminateConfigUtil.checkPostDelay(postDelay);
        }
        // 设置值。
        this.preDelay = preDelay;
        this.postDelay = postDelay;
    }

    public long getPreDelay() {
        return preDelay;
    }

    public long getPostDelay() {
        return postDelay;
    }

    @Override
    public String toString() {
        return "TerminateConfig{" +
                "preDelay=" + preDelay +
                ", postDelay=" + postDelay +
                '}';
    }

    /**
     * 终止处理器配置构造器。
     *
     * @author DwArFeng
     * @since 2.0.0
     */
    public static final class Builder implements Buildable<TerminateConfig> {

        public static final long MIN_DELAY = -1L;
        public static final long DEFAULT_PRE_DELAY = -1L;
        public static final long DEFAULT_POST_DELAY = -1L;

        private long preDelay = DEFAULT_PRE_DELAY;
        private long postDelay = DEFAULT_POST_DELAY;

        public Builder() {
        }

        public Builder setPreDelay(long preDelay) {
            this.preDelay = preDelay;
            return this;
        }

        public Builder setPostDelay(long postDelay) {
            this.postDelay = postDelay;
            return this;
        }

        @Override
        public TerminateConfig build() {
            TerminateConfigUtil.checkPreDelay(preDelay);
            TerminateConfigUtil.checkPostDelay(postDelay);
            return new TerminateConfig(preDelay, postDelay, true);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "preDelay=" + preDelay +
                    ", postDelay=" + postDelay +
                    '}';
        }
    }
}
