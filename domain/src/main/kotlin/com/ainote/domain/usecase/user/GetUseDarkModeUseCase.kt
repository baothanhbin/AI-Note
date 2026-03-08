package com.ainote.domain.usecase.user

import com.ainote.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUseDarkModeUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userDataRepository.useDarkMode
    }
}
