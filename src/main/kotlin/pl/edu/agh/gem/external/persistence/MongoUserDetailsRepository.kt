package pl.edu.agh.gem.external.persistence

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository
import pl.edu.agh.gem.internal.model.UserDetails
import pl.edu.agh.gem.internal.persistance.UserDetailsRepository

@Repository
class MongoUserDetailsRepository(
    private val mongo: MongoTemplate,

) : UserDetailsRepository {
    override fun save(userDetails: UserDetails): UserDetails {
        return mongo.save(userDetails.toEntity()).toDomain()
    }

    private fun UserDetails.toEntity() =
        UserDetailsEntity(
            id = id,
            username = username,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            bankAccountNumber = bankAccountNumber,
            preferredPaymentMethod = preferredPaymentMethod,
            attachmentId = attachmentId,
        )
}
