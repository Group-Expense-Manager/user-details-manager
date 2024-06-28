package pl.edu.agh.gem.util

import pl.edu.agh.gem.external.dto.DefaultAttachmentResponse
import pl.edu.agh.gem.external.dto.GroupDto
import pl.edu.agh.gem.external.dto.UserDetailsCreationRequest
import pl.edu.agh.gem.external.dto.UserGroupsResponse
import pl.edu.agh.gem.external.persistence.UserDetailsEntity
import pl.edu.agh.gem.internal.model.PaymentMethod
import pl.edu.agh.gem.internal.model.PaymentMethod.CASH
import pl.edu.agh.gem.internal.model.PaymentMethod.NONE
import pl.edu.agh.gem.internal.model.UserDetails
import pl.edu.agh.gem.util.DummyData.ANOTHER_GROUP_ID
import pl.edu.agh.gem.util.DummyData.ATTACHMENT_ID
import pl.edu.agh.gem.util.DummyData.GROUP_ID
import pl.edu.agh.gem.util.DummyData.USER_ID

fun createUserDetailsCreationRequest(
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

fun createGroupsUserDetails(
    ids: List<String> = listOf("id1", "id2", "id3"),
    usernames: List<String> = listOf("name1", "name2", "name3"),
    firstNames: List<String> = listOf("firstName1", "firstName2", "firstName3"),
    lastNames: List<String> = listOf("lastName1", "lastName2", "lastName3"),
): List<UserDetails> =
    ids.mapIndexed { index, id ->
        createUserDetails(
            id = id,
            username = usernames[index],
            firstName = firstNames[index],
            lastName = lastNames[index],
        )
    }

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

fun createUserGroupsResponse(
    vararg groups: String = arrayOf(GROUP_ID, ANOTHER_GROUP_ID),
) = UserGroupsResponse(groups = groups.map { GroupDto(it) })

object DummyData {
    const val USER_ID = "userId"
    const val ANOTHER_USER_ID = "anotherUserId"

    const val ATTACHMENT_ID = "attachmentId"
    const val GROUP_ID = "groupId"
    const val ANOTHER_GROUP_ID = "anotherGroupId"
}
