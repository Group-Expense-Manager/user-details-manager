package pl.edu.agh.gem.internal.client

interface AttachmentStoreClient {
    fun createDefaultUserAttachment(userId: String): String
}

class AttachmentStoreClientException(override val message: String?) : RuntimeException()

class RetryableAttachmentStoreClientException(override val message: String?) : RuntimeException()
