package xyz.yuelai.config;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.ssl.SSLUtils;

/**
 * @author zhong
 * @date 2021-03-17 15:03:19 周三
 */
public final class ForestConfig {
    private static final ForestConfiguration CONFIGURATION;

    static {
        CONFIGURATION = ForestConfiguration.configuration();
        // 连接池最大连接数，默认值为500
        CONFIGURATION.setMaxConnections(123);
        // 每个路由的最大连接数，默认值为500
        CONFIGURATION.setMaxRouteConnections(222);
        // 请求超时时间，单位为毫秒, 默认值为3000
        CONFIGURATION.setTimeout(3000);
        // 连接超时时间，单位为毫秒, 默认值为2000
        CONFIGURATION.setConnectTimeout(2000);
        // 请求失败后重试次数，默认为0次不重试
        CONFIGURATION.setRetryCount(1);
        // 单向验证的HTTPS的默认SSL协议，默认为SSLv3
        CONFIGURATION.setSslProtocol(SSLUtils.SSL_3);
        // 打开或关闭日志，默认为true
        CONFIGURATION.setLogEnabled(true);
        // 允许打印响应日志
        CONFIGURATION.setLogResponseStatus(true);
        CONFIGURATION.setLogResponseContent(true);
    }

    public static <T> T createInstance(Class<T> musicApiClass) {
        return CONFIGURATION.createInstance(musicApiClass);
    }
}
