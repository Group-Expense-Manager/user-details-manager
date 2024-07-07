package pl.edu.agh.gem.internal.service

import io.kotest.core.spec.style.ShouldSpec
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import pl.edu.agh.gem.internal.persistance.UserDetailsRepository
import pl.edu.agh.gem.util.createUserDetails

class UserDetailsServiceTest : ShouldSpec({
    val userDetailsRepository = mock<UserDetailsRepository>()
    val userDetailsService = UserDetailsService(userDetailsRepository)

    should("create user details successfully") {
        // given
        val userDetails = createUserDetails()
        whenever(userDetailsRepository.save(userDetails)).thenReturn(userDetails)

        // when
        userDetailsService.create(userDetails)

        // then
        verify(userDetailsRepository, times(1)).save(userDetails)
    }
},)
