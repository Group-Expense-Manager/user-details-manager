package pl.edu.agh.gem.external.client

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.POST
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
import pl.edu.agh.gem.metrics.MeteredClient
import pl.edu.agh.gem.paths.Paths.INTERNAL

@Component
@MeteredClient
class RestAttachmentStoreClient(
    @Qualifier("AttachmentStoreRestTemplate") val restTemplate: RestTemplate,
    val attachmentStoreProperties: AttachmentStoreProperties,
) : AttachmentStoreClient {
    @Retry(name = "attachmentStore")
    override fun createDefaultUserAttachment(userId: String): String {
        return try {
            restTemplate.exchange(
                resolveDefaultAttachmentIdAddress(userId),
                POST,
                HttpEntity<Any>(HttpHeaders().withAppAcceptType()),
                DefaultAttachmentResponse::class.java,
            ).body?.id ?: throw AttachmentStoreClientException("While trying to retrieve attachmentId we receive empty body")
        } catch (ex: HttpClientErrorException) {
            logger.warn(ex) { "Client side exception while trying to retrieve attachmentId" }
            throw AttachmentStoreClientException(ex.message)
        } catch (ex: HttpServerErrorException) {
            logger.warn(ex) { "Server side exception while trying to retrieve attachmentId" }
            throw RetryableAttachmentStoreClientException(ex.message)
        } catch (ex: Exception) {
            logger.warn(ex) { "Unexpected exception while trying to retrieve attachmentId" }
            throw AttachmentStoreClientException(ex.message)
        }
    }

    private fun resolveDefaultAttachmentIdAddress(userId: String) = "${attachmentStoreProperties.url}$INTERNAL/users/$userId/generate"

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
