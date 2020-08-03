package pl.kontroler.domain.mapper

import com.google.firebase.auth.FirebaseUser
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mapstruct.factory.Mappers
import org.mockito.Mockito


/**
 * @author Rafał Nowowieski
 */

class UserMapperTest {

    @Rule
    @JvmField
    val exception: ExpectedException = ExpectedException.none()

    @Test
    fun shouldSuccess_mapToModel() {
        val firebaseUser = Mockito.mock(FirebaseUser::class.java)
        Mockito.doReturn("user1").`when`(firebaseUser).displayName
        Mockito.doReturn("user1@email.com").`when`(firebaseUser).email

        val mapper = Mappers.getMapper(UserMapper::class.java)
        val user = mapper.mapToModel(firebaseUser)

        assertThat(user.name, `is`("user1"))
        assertThat(user.email, `is`("user1@email.com"))
    }

    @Test
    fun shouldThrowIllegalArgumentException_mapToModel() {
        val firebaseUser = Mockito.mock(FirebaseUser::class.java)
        Mockito.doReturn(null).`when`(firebaseUser).displayName
        Mockito.doReturn("user1@email.com").`when`(firebaseUser).email

        val mapper = Mappers.getMapper(UserMapper::class.java)
        exception.expect(IllegalArgumentException::class.java)
        mapper.mapToModel(firebaseUser)
    }

}