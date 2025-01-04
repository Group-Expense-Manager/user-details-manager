package pl.edu.agh.gem.external.client

import io.github.resilience4j.retry.annotation.Retry
import io.github.oshai.kotlinlogging.KotlinLogging
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
import pl.edu.agh.gem.external.dto.UserGroupsResponse
import pl.edu.agh.gem.headers.HeadersUtils.withAppAcceptType
import pl.edu.agh.gem.internal.client.GroupManagerClient
import pl.edu.agh.gem.internal.client.GroupManagerClientException
import pl.edu.agh.gem.internal.client.RetryableGroupManagerClientException
import pl.edu.agh.gem.internal.model.Group
import pl.edu.agh.gem.metrics.MeteredClient
import pl.edu.agh.gem.model.GroupMember
import pl.edu.agh.gem.model.GroupMembers
import pl.edu.agh.gem.paths.Paths.INTERNAL

@Component
@MeteredClient
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

    @Retry(name = "groupManager")
    override fun getGroups(userId: String): List<Group> {
        return try {
            restTemplate.exchange(
                resolveUserGroupsAddress(userId),
                GET,
                HttpEntity<Any>(HttpHeaders().withAppAcceptType()),
                UserGroupsResponse::class.java,
            ).body?.toDomain() ?: throw GroupManagerClientException("While trying to retrieve user groups we receive empty body")
        } catch (ex: HttpClientErrorException) {
            logger.warn(ex) { "Client side exception while trying to retrieve user groups" }
            throw GroupManagerClientException(ex.message)
        } catch (ex: HttpServerErrorException) {
            logger.warn(ex) { "Server side exception while trying to retrieve user groups" }
            throw RetryableGroupManagerClientException(ex.message)
        } catch (ex: Exception) {
            logger.warn(ex) { "Unexpected exception while trying to retrieve user groups" }
            throw GroupManagerClientException(ex.message)
        }
    }

    private fun resolveGroupMembersAddress(groupId: String) =
        "${groupManagerProperties.url}$INTERNAL/members/$groupId"

    private fun resolveUserGroupsAddress(userId: String) =
        "${groupManagerProperties.url}$INTERNAL/groups/users/$userId"

    fun GroupMembersResponse.toGroupMembers() = GroupMembers(members = members.map { GroupMember(it.id) })

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
