package com.examples.esbook.elasticsearch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties for Elasticsearch.
 *
 * @author fengxuechao
 * @since 1.0.2
 */
@Deprecated
@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchProperties {

    private List<String> hostAndPortList;

    private String username;

    private String password;
}
