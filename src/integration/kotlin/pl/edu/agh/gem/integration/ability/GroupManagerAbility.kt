package pl.edu.agh.gem.integration.ability

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatusCode
import pl.edu.agh.gem.headers.HeadersTestUtils.withAppContentType
import pl.edu.agh.gem.integration.environment.ProjectConfig.wiremock
import pl.edu.agh.gem.paths.Paths.INTERNAL

private fun getMembersUrl(groupId: String) = "$INTERNAL/members/$groupId"

private fun getUserGroupsUrl(userId: String) = "$INTERNAL/groups/users/$userId"

fun stubMembersUrl(
    body: Any?,
    groupId: String,
    statusCode: HttpStatusCode = OK,
) {
    wiremock.stubFor(
        get(urlMatching(getMembersUrl(groupId)))
            .willReturn(
                aResponse()
                    .withStatus(statusCode.value())
                    .withAppContentType()
                    .withBody(
                        jacksonObjectMapper().writeValueAsString(body),
                    ),
            ),
    )
}

fun stubUserGroupsUrl(
    body: Any?,
    userId: String,
    statusCode: HttpStatusCode = OK,
) {
    wiremock.stubFor(
        get(urlMatching(getUserGroupsUrl(userId)))
            .willReturn(
                aResponse()
                    .withStatus(statusCode.value())
                    .withAppContentType()
                    .withBody(
                        jacksonObjectMapper().writeValueAsString(body),
                    ),
            ),
    )
}
