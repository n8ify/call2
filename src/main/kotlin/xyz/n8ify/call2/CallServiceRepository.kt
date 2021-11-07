package xyz.n8ify.call2

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CallServiceRepository : JpaRepository<ServiceInfo, String>