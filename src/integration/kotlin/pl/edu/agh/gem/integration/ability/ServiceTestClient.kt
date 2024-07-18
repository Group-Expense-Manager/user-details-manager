package pl.edu.agh.gem.integration.ability

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.test.web.servlet.client.MockMvcWebTestClient.bindToApplicationContext
import org.springframework.web.context.WebApplicationContext
import pl.edu.agh.gem.headers.HeadersUtils.withAppAcceptType
import pl.edu.agh.gem.headers.HeadersUtils.withAppContentType
import pl.edu.agh.gem.headers.HeadersUtils.withValidatedUser
import pl.edu.agh.gem.paths.Paths.EXTERNAL
import pl.edu.agh.gem.paths.Paths.INTERNAL
import pl.edu.agh.gem.security.GemUser

@Component
@Lazy
class ServiceTestClient(applicationContext: WebApplicationContext) {
    private val webClient = bindToApplicationContext(applicationContext)
        .configureClient()
        .build()

    fun createUserDetails(body: Any): ResponseSpec {
        return webClient.post()
            .uri { it.path("$INTERNAL/user-details").build() }
            .headers {
                it.withAppContentType()
            }
            .bodyValue(body)
            .exchange()
    }

    fun getInternalGroupUserDetails(groupId: String): ResponseSpec {
        return webClient.get()
            .uri { it.path("$INTERNAL/user-details/groups/$groupId").build() }
            .headers {
                it.withAppAcceptType()
            }
            .exchange()
    }

    fun getExternalGroupUserDetails(user: GemUser, groupId: String): ResponseSpec {
        return webClient.get()
            .uri { it.path("$EXTERNAL/user-details/groups/$groupId").build() }
            .headers {
                it.withValidatedUser(user).withAppAcceptType()
            }
            .exchange()
    }

    fun getUserDetails(user: GemUser): ResponseSpec {
        return webClient.get()
            .uri { it.path("$EXTERNAL/user-details").build() }
            .headers {
                it.withValidatedUser(user).withAppAcceptType()
            }
            .exchange()
    }

    fun getGroupMemberDetails(user: GemUser, groupId: String, groupMemberId: String): ResponseSpec {
        return webClient.get()
            .uri { it.path("$EXTERNAL/user-details/groups/$groupId/members/$groupMemberId").build() }
            .headers {
                it.withValidatedUser(user).withAppAcceptType()
            }
            .exchange()
    }

    fun updateGroupUserDetails(user: GemUser, body: Any): ResponseSpec {
        return webClient.put()
            .uri { it.path("$EXTERNAL/user-details").build() }
            .headers {
                it.withValidatedUser(user).withAppContentType()
            }
            .bodyValue(body)
            .exchange()
    }

    fun getUsername(userId: String): ResponseSpec {
        return webClient.get()
            .uri { it.path("$INTERNAL/user-details/username/$userId").build() }
            .headers {
                it.withAppAcceptType()
            }
            .exchange()
    }
}
