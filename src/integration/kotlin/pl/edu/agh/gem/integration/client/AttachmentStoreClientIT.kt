package pl.edu.agh.gem.integration.client

import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.integration.BaseIntegrationSpec
import pl.edu.agh.gem.integration.ability.stubDefaultAttachmentUrl
import pl.edu.agh.gem.internal.client.AttachmentStoreClient
import pl.edu.agh.gem.util.DummyData.ATTACHMENT_ID
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createDefaultAttachmentResponse

class AttachmentStoreClientIT(
    private val attachmentStoreClient: AttachmentStoreClient,
) : BaseIntegrationSpec({
        should("get attachmentId") {
            // given
            val defaultAttachmentResponse = createDefaultAttachmentResponse()
            stubDefaultAttachmentUrl(defaultAttachmentResponse, USER_ID)

            // when
            val result = attachmentStoreClient.createDefaultUserAttachment(USER_ID)

            // then
            result shouldBe ATTACHMENT_ID
        }
    })
