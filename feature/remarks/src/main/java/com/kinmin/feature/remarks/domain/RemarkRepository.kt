package com.kinmin.feature.remarks.domain

import com.kinmin.core.common.Resource

interface RemarkRepository {
    suspend fun addRemark(text: String): Resource<Unit>
}
