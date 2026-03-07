package com.ainote.domain.usecase.user

import com.ainote.data.repository.UserDataRepository
import javax.inject.Inject

class SetUseMarkdownPreviewUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(useMarkdownPreview: Boolean) {
        userDataRepository.setUseMarkdownPreview(useMarkdownPreview)
    }
}
