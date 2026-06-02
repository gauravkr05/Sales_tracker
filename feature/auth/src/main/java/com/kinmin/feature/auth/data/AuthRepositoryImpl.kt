package com.kinmin.feature.auth.data

import com.kinmin.core.common.Resource
import com.kinmin.core.common.safeCall
import com.kinmin.core.datastore.SessionStore
import com.kinmin.core.model.Role
import com.kinmin.core.model.Salesperson
import com.kinmin.feature.auth.domain.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
private data class ProfileRow(
    val id: String,
    val name: String,
    val role: String,
    @SerialName("route_name") val routeName: String? = null
)

class AuthRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient,
    private val session: SessionStore
) : AuthRepository {

    // NOTE: supabase-kt query DSL can change between versions. Adjust the
    // select{} block to match the version you sync if needed.
    override suspend fun login(email: String, password: String): Resource<Salesperson> = safeCall {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        val uid = supabase.auth.currentUserOrNull()?.id ?: error("No session")
        val profile = supabase.postgrest.from("profiles")
            .select { filter { eq("id", uid) } }
            .decodeSingle<ProfileRow>()
        session.saveUser(profile.id, profile.name)
        profile.toModel()
    }

    override suspend fun currentUser(): Salesperson? {
        val uid = supabase.auth.currentUserOrNull()?.id ?: return null
        return runCatching {
            supabase.postgrest.from("profiles")
                .select { filter { eq("id", uid) } }
                .decodeSingle<ProfileRow>()
                .toModel()
        }.getOrNull()
    }

    override suspend fun logout() {
        supabase.auth.signOut()
        session.clear()
    }

    private fun ProfileRow.toModel() = Salesperson(
        id = id,
        name = name,
        role = if (role.equals("ADMIN", true)) Role.ADMIN else Role.SALESPERSON,
        routeName = routeName
    )
}
