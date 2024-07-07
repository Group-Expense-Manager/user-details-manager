package pl.edu.agh.gem.integration.ability

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatusCode
import pl.edu.agh.gem.external.dto.DefaultAttachmentRequest
import pl.edu.agh.gem.headers.HeadersTestUtils.withAppContentType
import pl.edu.agh.gem.integration.environment.ProjectConfig.wiremock
import pl.edu.agh.gem.paths.Paths.INTERNAL

private fun createDefaultAttachmentUrl() = "$INTERNAL/attachments"

fun stubDefaultAttachmentUrl(body: Any?, userId: String, statusCode: HttpStatusCode = OK) {
    wiremock.stubFor(
        post(urlMatching(createDefaultAttachmentUrl()))
            .withRequestBody(
                equalToJson(
                    jacksonObjectMapper().writeValueAsString(DefaultAttachmentRequest(userId)),
                ),
            )
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
