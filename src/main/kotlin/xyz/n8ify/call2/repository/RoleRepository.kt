package xyz.n8ify.call2.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import xyz.n8ify.call2.repository.entity.RoleEntity
import xyz.n8ify.call2.repository.entity.UserEntity

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {

    fun findByName(name: String) : RoleEntity

}