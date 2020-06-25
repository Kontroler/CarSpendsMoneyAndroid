package pl.kontroler.domain.mapper

import com.google.firebase.auth.FirebaseUser
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import pl.kontroler.domain.model.User


/**
 * @author Rafał Nowowieski
 */

@Mapper
interface UserMapper {

    @Mappings(
        Mapping(target = "name", source = "firebaseUser.displayName"),
        Mapping(target = "email", source = "firebaseUser.email")
    )
    fun mapToModel(firebaseUser: FirebaseUser): User

}