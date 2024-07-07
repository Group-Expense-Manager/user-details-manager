package pl.edu.agh.gem.internal.persistance

import pl.edu.agh.gem.internal.model.UserDetails

interface UserDetailsRepository {
    fun save(userDetails: UserDetails): UserDetails
}
