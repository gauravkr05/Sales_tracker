package com.kinmin.core.model

enum class Role { SALESPERSON, ADMIN }

data class Salesperson(
    val id: String,
    val name: String,
    val role: Role,
    val routeName: String? = null
)

data class Party(
    val id: String,
    val name: String,
    val area: String? = null,
    val phone: String? = null
)

/** Represents a queued, locally-saved item awaiting upload. */
enum class SyncStatus { PENDING, SYNCING, SYNCED, FAILED }
