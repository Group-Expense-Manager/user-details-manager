package pl.edu.agh.gem.integration.client

import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.integration.BaseIntegrationSpec
import pl.edu.agh.gem.integration.ability.stubDefaultAttachmentIdUrl
import pl.edu.agh.gem.internal.client.AttachmentStoreClient
import pl.edu.agh.gem.util.DummyData.ATTACHMENT_ID
import pl.edu.agh.gem.util.createDefaultAttachmentResponse

class AttachmentStoreClientIT(
    private val attachmentStoreClient: AttachmentStoreClient,
) : BaseIntegrationSpec({
    should("get default attachmentId") {
        // given
        val defaultAttachmentResponse = createDefaultAttachmentResponse()
        stubDefaultAttachmentIdUrl(defaultAttachmentResponse)

        // when
        val result = attachmentStoreClient.getDefaultUserAttachmentId()

        // then
        result shouldBe ATTACHMENT_ID
    }
},)
