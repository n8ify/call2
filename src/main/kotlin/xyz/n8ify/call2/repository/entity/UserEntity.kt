package xyz.n8ify.call2.repository.entity

import javax.persistence.*

@Entity
@Table(name = "user")
data class UserEntity(
    @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long? = null,
    @Column(name = "username") val username: String,
    @Column(name = "password") val password: String,
    @ManyToMany(fetch = FetchType.EAGER) val roles: MutableList<RoleEntity> = mutableListOf(),
)
