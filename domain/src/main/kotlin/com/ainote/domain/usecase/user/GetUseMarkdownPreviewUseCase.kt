package com.ainote.domain.usecase.user

import com.ainote.data.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUseMarkdownPreviewUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userDataRepository.useMarkdownPreview
    }
}
