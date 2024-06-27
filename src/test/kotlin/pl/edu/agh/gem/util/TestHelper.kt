package pl.edu.agh.gem.util

import pl.edu.agh.gem.external.dto.DefaultAttachmentResponse
import pl.edu.agh.gem.external.dto.UserDetailsCreationRequest
import pl.edu.agh.gem.external.persistence.UserDetailsEntity
import pl.edu.agh.gem.internal.model.PaymentMethod
import pl.edu.agh.gem.internal.model.PaymentMethod.CASH
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

fun createBasicUserDetails(
    id: String = USER_ID,
    username: String = "user",
    attachmentId: String = ATTACHMENT_ID,
) = UserDetails(
    id = id,
    username = username,
    attachmentId = attachmentId,
)

fun createUserDetails(
    id: String = USER_ID,
    username: String = "user",
    firstName: String = "firstName",
    lastName: String = "lastName",
    phoneNumber: String = "123123213",
    bankAccountNumber: String = "2132 2343 0000 0000 0000",
    preferredPaymentMethod: PaymentMethod = CASH,
    attachmentId: String = ATTACHMENT_ID,
) = UserDetails(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    bankAccountNumber = bankAccountNumber,
    preferredPaymentMethod = preferredPaymentMethod,
    attachmentId = attachmentId,
)

fun createBasicUserDetailsEntity(
    id: String = USER_ID,
    username: String = "user",
    attachmentId: String = ATTACHMENT_ID,
) = UserDetailsEntity(
    id = id,
    username = username,
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
    const val ANOTHER_USER_ID = "anotherUserId"

    const val ATTACHMENT_ID = "attachmentId"
    const val GROUP_ID = "groupId"
}
