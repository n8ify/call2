package xyz.n8ify.call2.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import xyz.n8ify.call2.repository.entity.UserEntity
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByUsername(username: String) : Optional<UserEntity>

}