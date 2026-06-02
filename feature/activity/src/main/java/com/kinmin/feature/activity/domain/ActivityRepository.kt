package com.kinmin.feature.activity.domain

import com.kinmin.core.common.Resource
import java.io.File

interface ActivityRepository {
    suspend fun logActivity(title: String, note: String?, photo: File?): Resource<Unit>
}
