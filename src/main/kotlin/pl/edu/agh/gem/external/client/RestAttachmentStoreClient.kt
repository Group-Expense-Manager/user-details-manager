package pl.edu.agh.gem.external.client

import io.github.resilience4j.retry.annotation.Retry
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.GET
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import pl.edu.agh.gem.config.AttachmentStoreProperties
import pl.edu.agh.gem.external.dto.DefaultAttachmentResponse
import pl.edu.agh.gem.headers.HeadersUtils.withAppAcceptType
import pl.edu.agh.gem.internal.client.AttachmentStoreClient
import pl.edu.agh.gem.internal.client.AttachmentStoreClientException
import pl.edu.agh.gem.internal.client.RetryableAttachmentStoreClientException
import pl.edu.agh.gem.paths.Paths.INTERNAL

@Component
class RestAttachmentStoreClient(
    @Qualifier("AttachmentStoreRestTemplate") val restTemplate: RestTemplate,
    val attachmentStoreProperties: AttachmentStoreProperties,
) : AttachmentStoreClient {
    @Retry(name = "userDetailsManager")
    override fun getDefaultUserAttachmentId(): String {
        return try {
            restTemplate.exchange(
                resolveDefaultAttachmentIdAddress(),
                GET,
                HttpEntity<Any>(HttpHeaders().withAppAcceptType()),
                DefaultAttachmentResponse::class.java,
            ).body?.attachmentId ?: throw AttachmentStoreClientException("While trying to retrieve available currencies we receive empty body")
        } catch (ex: HttpClientErrorException) {
            logger.warn(ex) { "Client side exception while trying to retrieve available currencies" }
            throw AttachmentStoreClientException(ex.message)
        } catch (ex: HttpServerErrorException) {
            logger.warn(ex) { "Server side exception while trying to retrieve available currencies" }
            throw RetryableAttachmentStoreClientException(ex.message)
        } catch (ex: Exception) {
            logger.warn(ex) { "Unexpected exception while trying to retrieve available currencies" }
            throw AttachmentStoreClientException(ex.message)
        }
    }

    private fun resolveDefaultAttachmentIdAddress() =
        "${attachmentStoreProperties.url}$INTERNAL/attachments"

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
