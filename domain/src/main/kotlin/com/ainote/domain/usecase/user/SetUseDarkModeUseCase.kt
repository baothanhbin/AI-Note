package com.ainote.domain.usecase.user

import com.ainote.domain.repository.UserDataRepository
import javax.inject.Inject

class SetUseDarkModeUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(useDarkMode: Boolean) {
        userDataRepository.setUseDarkMode(useDarkMode)
    }
}
