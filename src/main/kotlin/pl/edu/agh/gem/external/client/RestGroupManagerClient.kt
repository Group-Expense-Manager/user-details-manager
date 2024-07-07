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
import pl.edu.agh.gem.config.GroupManagerProperties
import pl.edu.agh.gem.dto.GroupMembersResponse
import pl.edu.agh.gem.headers.HeadersUtils.withAppAcceptType
import pl.edu.agh.gem.internal.client.GroupManagerClient
import pl.edu.agh.gem.internal.client.GroupManagerClientException
import pl.edu.agh.gem.internal.client.RetryableGroupManagerClientException
import pl.edu.agh.gem.model.GroupMember
import pl.edu.agh.gem.model.GroupMembers
import pl.edu.agh.gem.paths.Paths.INTERNAL

@Component
class RestGroupManagerClient(
    @Qualifier("GroupManagerRestTemplate") val restTemplate: RestTemplate,
    val groupManagerProperties: GroupManagerProperties,
) : GroupManagerClient {
    @Retry(name = "groupManager")
    override fun getMembers(groupId: String): GroupMembers {
        return try {
            restTemplate.exchange(
                resolveGroupMembersAddress(groupId),
                GET,
                HttpEntity<Any>(HttpHeaders().withAppAcceptType()),
                GroupMembersResponse::class.java,
            ).body?.toGroupMembers() ?: throw GroupManagerClientException("While trying to retrieve group members we receive empty body")
        } catch (ex: HttpClientErrorException) {
            logger.warn(ex) { "Client side exception while trying to retrieve group members" }
            throw GroupManagerClientException(ex.message)
        } catch (ex: HttpServerErrorException) {
            logger.warn(ex) { "Server side exception while trying to retrieve group members" }
            throw RetryableGroupManagerClientException(ex.message)
        } catch (ex: Exception) {
            logger.warn(ex) { "Unexpected exception while trying to retrieve group members" }
            throw GroupManagerClientException(ex.message)
        }
    }

    private fun resolveGroupMembersAddress(groupId: String) =
        "${groupManagerProperties.url}$INTERNAL/members/$groupId"

    fun GroupMembersResponse.toGroupMembers() = GroupMembers(members = members.map { GroupMember(it.id) })

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
