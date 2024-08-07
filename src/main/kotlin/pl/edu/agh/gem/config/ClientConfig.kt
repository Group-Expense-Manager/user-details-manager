package pl.edu.agh.gem.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class ClientConfig {
    @Bean
    @Qualifier("AttachmentStoreRestTemplate")
    fun attachmentStoreRestTemplate(attachmentStoreProperties: AttachmentStoreProperties): RestTemplate {
        return RestTemplateBuilder()
            .setConnectTimeout(attachmentStoreProperties.connectTimeout)
            .setReadTimeout(attachmentStoreProperties.readTimeout)
            .build()
    }

    @Bean
    @Qualifier("GroupManagerRestTemplate")
    fun groupManagerRestTemplate(groupManagerProperties: GroupManagerProperties): RestTemplate {
        return RestTemplateBuilder()
            .setConnectTimeout(groupManagerProperties.connectTimeout)
            .setReadTimeout(groupManagerProperties.readTimeout)
            .build()
    }
}

@ConfigurationProperties(prefix = "attachment-store")
data class AttachmentStoreProperties(
    val url: String,
    val connectTimeout: Duration,
    val readTimeout: Duration,
)

@ConfigurationProperties(prefix = "group-manager")
data class GroupManagerProperties(
    val url: String,
    val connectTimeout: Duration,
    val readTimeout: Duration,
)
