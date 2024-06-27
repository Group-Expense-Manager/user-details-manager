package pl.edu.agh.gem.internal.service

import org.springframework.stereotype.Service
import pl.edu.agh.gem.internal.model.UserDetails
import pl.edu.agh.gem.internal.persistance.UserDetailsRepository

@Service
class UserDetailsService(
    private val userDetailsRepository: UserDetailsRepository,
) {
    fun create(userDetails: UserDetails) {
        userDetailsRepository.save(userDetails)
    }
}
