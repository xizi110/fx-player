package xyz.yuelai.config;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.ssl.SSLUtils;

/**
 * @author zhong
 * @date 2021-03-17 15:03:19 周三
 */
public final class ForestConfig {

    private static ForestConfiguration config() {
        ForestConfiguration configuration = ForestConfiguration.configuration();
        // 连接池最大连接数，默认值为500
        configuration.setMaxConnections(123);
        // 每个路由的最大连接数，默认值为500
        configuration.setMaxRouteConnections(222);
        // 请求超时时间，单位为毫秒, 默认值为3000
        configuration.setTimeout(3000);
        // 连接超时时间，单位为毫秒, 默认值为2000
        configuration.setConnectTimeout(2000);
        // 请求失败后重试次数，默认为0次不重试
        configuration.setRetryCount(3);
        // 单向验证的HTTPS的默认SSL协议，默认为SSLv3
        configuration.setSslProtocol(SSLUtils.SSL_3);
        // 打开或关闭日志，默认为true
        configuration.setLogEnabled(true);
        return configuration;
    }

    public static <T> T createInstance(Class<T> musicApiClass) {
        return config().createInstance(musicApiClass);
    }
}
