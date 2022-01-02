package xyz.n8ify.call2.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import xyz.n8ify.call2.enums.Role
import xyz.n8ify.call2.repository.RoleRepository
import xyz.n8ify.call2.repository.UserRepository
import xyz.n8ify.call2.repository.entity.RoleEntity
import xyz.n8ify.call2.repository.entity.UserEntity

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String?): UserDetails {
        return userRepository.findByUsername(username!!).get().run {
            User(username, password, roles.map { SimpleGrantedAuthority(it.name) } )
        }
    }

    override fun saveUser(username: String, password: String, roles : MutableList<Role>): UserEntity {
        return userRepository.save(
            UserEntity(username = username
                , password = passwordEncoder.encode(password)
                , roles = roles.map { roleRepository.findByName(it.name) }.toMutableList() )
        )
    }

    override fun saveRole(role: RoleEntity): RoleEntity {
        return roleRepository.save(role)
    }

    override fun findByUsername(username: String): UserEntity? {
        return userRepository.findByUsername(username).orElseGet { null }
    }
}

interface UserService : UserDetailsService {

    fun saveUser(username: String, password: String, roles : MutableList<Role> = mutableListOf()) : UserEntity

    fun saveRole(role: RoleEntity) : RoleEntity

    fun findByUsername(username: String) : UserEntity?

}