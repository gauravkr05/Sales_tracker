package com.kinmin.core.sync

import kotlinx.serialization.Serializable

/** Shapes that map to Supabase Postgres tables. */
@Serializable
data class OrderDto(
    val user_id: String,
    val party_name: String,
    val amount: Double,
    val photo_url: String?,
    val created_at: Long
)

@Serializable
data class ActivityDto(
    val user_id: String,
    val title: String,
    val note: String?,
    val photo_url: String?,
    val created_at: Long
)

@Serializable
data class RemarkDto(
    val user_id: String,
    val text: String,
    val created_at: Long
)

@Serializable
data class LocationPingDto(
    val user_id: String,
    val lat: Double,
    val lng: Double,
    val recorded_at: Long
)
