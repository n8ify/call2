package xyz.n8ify.call2.repository.entity

import javax.persistence.*

@Entity
@Table(name = "role")
data class RoleEntity(
    @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long,
    @Column(name = "name") val name: String
)