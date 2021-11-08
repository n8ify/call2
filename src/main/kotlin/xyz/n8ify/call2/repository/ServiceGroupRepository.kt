package xyz.n8ify.call2.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import xyz.n8ify.call2.repository.entity.ServiceEntity
import xyz.n8ify.call2.repository.entity.ServiceGroupEntity

@Repository
interface ServiceGroupRepository : JpaRepository<ServiceGroupEntity, String>