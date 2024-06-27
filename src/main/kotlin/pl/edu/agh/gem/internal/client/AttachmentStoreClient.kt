package pl.edu.agh.gem.internal.client

interface AttachmentStoreClient {
    fun getDefaultUserAttachmentId(): String
}

class AttachmentStoreClientException(override val message: String?) : RuntimeException()

class RetryableAttachmentStoreClientException(override val message: String?) : RuntimeException()
