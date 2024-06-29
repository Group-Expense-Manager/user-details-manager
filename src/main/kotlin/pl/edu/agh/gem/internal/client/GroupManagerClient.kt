package pl.edu.agh.gem.internal.client

import pl.edu.agh.gem.internal.model.Group
import pl.edu.agh.gem.model.GroupMembers

interface GroupManagerClient {
    fun getMembers(groupId: String): GroupMembers
    fun getGroups(userId: String): List<Group>
}

class GroupManagerClientException(override val message: String?) : RuntimeException()

class RetryableGroupManagerClientException(override val message: String?) : RuntimeException()
