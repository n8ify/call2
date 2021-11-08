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
    @Lob val thumbnailUrl: String?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ServiceEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , description = $description , url = $url , thumbnailUrl = $thumbnailUrl )"
    }
}