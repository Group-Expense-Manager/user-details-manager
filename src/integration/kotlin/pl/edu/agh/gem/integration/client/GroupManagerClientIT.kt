package pl.edu.agh.gem.integration.client

import pl.edu.agh.gem.helper.group.createGroupMembersResponse
import pl.edu.agh.gem.integration.BaseIntegrationSpec
import pl.edu.agh.gem.integration.ability.stubMembersUrl
import pl.edu.agh.gem.integration.ability.stubUserGroupsUrl
import pl.edu.agh.gem.internal.client.GroupManagerClient
import pl.edu.agh.gem.util.DummyData.ANOTHER_USER_ID
import pl.edu.agh.gem.util.DummyData.GROUP_ID
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createUserGroupsResponse

class GroupManagerClientIT(
    private val groupManagerClient: GroupManagerClient,
) : BaseIntegrationSpec({
        should("get group members ids") {
            // given
            val members = arrayOf(USER_ID, ANOTHER_USER_ID)
            val groupMembersResponse = createGroupMembersResponse(*members)
            stubMembersUrl(groupMembersResponse, GROUP_ID)

            // when
            val result = groupManagerClient.getMembers(GROUP_ID)

            // then
            result.members.all {
                it.id in members
            }
        }

        should("get user groups") {
            // given
            val userGroups = arrayOf(GROUP_ID, ANOTHER_USER_ID)
            val userGroupsResponse = createUserGroupsResponse(GROUP_ID, ANOTHER_USER_ID)
            stubUserGroupsUrl(userGroupsResponse, USER_ID)

            // when
            val result = groupManagerClient.getGroups(USER_ID)

            // then
            result.all {
                it.groupId in userGroups
            }
        }
    })
