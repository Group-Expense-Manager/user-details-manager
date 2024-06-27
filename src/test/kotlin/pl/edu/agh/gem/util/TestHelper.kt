package pl.edu.agh.gem.util

import pl.edu.agh.gem.external.dto.DefaultAttachmentResponse
import pl.edu.agh.gem.external.dto.UserDetailsCreationRequest
import pl.edu.agh.gem.external.persistence.UserDetailsEntity
import pl.edu.agh.gem.internal.model.PaymentMethod.NONE
import pl.edu.agh.gem.internal.model.UserDetails
import pl.edu.agh.gem.util.DummyData.ATTACHMENT_ID
import pl.edu.agh.gem.util.DummyData.USER_ID

fun createUserDetailRequest(
    id: String = USER_ID,
    username: String = "user",
) = UserDetailsCreationRequest(
    userId = id,
    username = username,
)

fun createUserDetails(
    id: String = USER_ID,
    username: String = "user",
    attachmentId: String = ATTACHMENT_ID,
) = UserDetails(
    id = id,
    username = username,
    firstName = null,
    lastName = null,
    phoneNumber = null,
    bankAccountNumber = null,
    preferredPaymentMethod = NONE,
    attachmentId = attachmentId,
)

fun createUserDetailsEntity(
    id: String = USER_ID,
    username: String = "user",
    attachmentId: String = ATTACHMENT_ID,
) = UserDetailsEntity(
    id = id,
    username = username,
    firstName = null,
    lastName = null,
    phoneNumber = null,
    bankAccountNumber = null,
    preferredPaymentMethod = NONE,
    attachmentId = attachmentId,
)

fun createDefaultAttachmentResponse(
    attachmentId: String = ATTACHMENT_ID,
) = DefaultAttachmentResponse(
    attachmentId = attachmentId,
)

object DummyData {
    const val USER_ID = "userId"
    const val ATTACHMENT_ID = "attachmentId"
}
