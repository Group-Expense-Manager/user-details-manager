package pl.edu.agh.gem.integration.controller

import org.springframework.http.HttpStatus.CREATED
import pl.edu.agh.gem.assertion.shouldHaveHttpStatus
import pl.edu.agh.gem.integration.BaseIntegrationSpec
import pl.edu.agh.gem.integration.ability.ServiceTestClient
import pl.edu.agh.gem.integration.ability.stubDefaultAttachmentUrl
import pl.edu.agh.gem.util.DummyData.USER_ID
import pl.edu.agh.gem.util.createDefaultAttachmentResponse
import pl.edu.agh.gem.util.createUserDetailRequest

class UserDetailsControllerIT(
    private val service: ServiceTestClient,
) : BaseIntegrationSpec(
    {
        should("create user details") {
            // given
            val userDetailsRequest = createUserDetailRequest()
            stubDefaultAttachmentUrl(createDefaultAttachmentResponse(), USER_ID)

            // when
            val response = service.createUserDetails(userDetailsRequest)

            // then
            response shouldHaveHttpStatus CREATED
        }
    },
)
