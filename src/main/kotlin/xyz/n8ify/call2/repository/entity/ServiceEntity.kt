package xyz.n8ify.call2.repository.entity

import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "service")
data class ServiceEntity(
    @Id val id: String,
    val groupId: String,
    val title: String,
    val description: String?,
    val url: String,
    @Lob val thumbnail: String?,
    val isLink: Boolean)