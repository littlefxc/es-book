package com.examples.esbook.elasticsearch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * ES 配置
 *
 * @author fengxuechao
 * @since 1.1.0
 */
@Configuration
@ConditionalOnClass({Client.class, RestHighLevelClient.class})
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfiguration implements DisposableBean {

    private static final Log logger = LogFactory.getLog(ElasticsearchConfiguration.class);

    private Closeable closeable;

    @Autowired
    private ElasticsearchProperties properties;


    /**
     * 创建 Elasticsearch RestHighLevelClient
     *
     * @return
     */
    @Bean("restHighLevelClient")
    @ConditionalOnMissingBean
    public RestHighLevelClient restHighLevelClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        List<HttpHost> list = createHttpHost();
        HttpHost[] array = list.toArray(new HttpHost[list.size()]);
        RestClientBuilder builder = RestClient.builder(array);
        //es账号密码设置
        if (StringUtils.hasText(properties.getUsername())) {
            String username = properties.getUsername();
            String password = properties.getPassword();
            UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(username, password);
            credentialsProvider.setCredentials(AuthScope.ANY, usernamePasswordCredentials);
            builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

                /**
                 * 这里可以设置一些参数，比如cookie存储、代理等等
                 * @param httpClientBuilder
                 * @return
                 */
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    httpClientBuilder.disableAuthCaching();
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            });
        }
        // RestHighLevelClient实例需要Rest low-level client builder构建
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        closeable = restHighLevelClient;
        return restHighLevelClient;
    }

    /**
     * 读取配置文件es信息构建 HttpHost 列表
     *
     * @return
     */
    private List<HttpHost> createHttpHost() {
        List<String> hostAndPortList = properties.getHostAndPortList();
        if (hostAndPortList.isEmpty()) {
            throw new IllegalArgumentException("必须配置elasticsearch节点信息");
        }
        List<HttpHost> list = new ArrayList<>(hostAndPortList.size());
        for (String s : hostAndPortList) {
            String[] hostAndPortArray = s.split(":");
            String hostname = hostAndPortArray[0];
            int port = Integer.parseInt(hostAndPortArray[1]);
            list.add(new HttpHost(hostname, port));
        }
        return list;
    }


    /**
     * 当不再需要时，需要关闭高级客户端实例，以便它所使用的所有资源以及底层的http客户端实例及其线程得到正确释放。
     * 通过close方法来完成，该方法将关闭内部的RestClient实例
     */
    @Override
    public void destroy() throws Exception {
        if (this.closeable != null) {
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("Closing Elasticsearch client");
                }
                try {
                    this.closeable.close();
                } catch (NoSuchMethodError ex) {
                    // Earlier versions of Elasticsearch had a different method name
                    ReflectionUtils.invokeMethod(
                            ReflectionUtils.findMethod(Closeable.class, "close"),
                            this.closeable);
                }
            } catch (final Exception ex) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error closing Elasticsearch client: ", ex);
                }
            }
        }
    }
}
