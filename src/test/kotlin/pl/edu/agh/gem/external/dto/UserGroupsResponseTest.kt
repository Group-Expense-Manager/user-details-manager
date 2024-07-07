package pl.edu.agh.gem.external.dto

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import pl.edu.agh.gem.util.createUserGroupsResponse

class UserGroupsResponseTest : ShouldSpec({
    should("map UserGroupsResponse to List<Groups> correctly") {
        // given
        val ids = arrayOf("id1", "id2", "id3")
        val userGroupsResponse = createUserGroupsResponse(*ids)

        // when
        val groups = userGroupsResponse.toDomain()

        // then
        groups.also {
            it.size shouldBe 3
            it.map { group -> group.groupId } shouldContainExactly ids.toList()
        }
    }
},)
