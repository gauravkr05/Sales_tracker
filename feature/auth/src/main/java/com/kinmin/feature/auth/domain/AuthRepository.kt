package com.kinmin.feature.auth.domain

import com.kinmin.core.common.Resource
import com.kinmin.core.model.Salesperson

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<Salesperson>
    suspend fun currentUser(): Salesperson?
    suspend fun logout()
}
