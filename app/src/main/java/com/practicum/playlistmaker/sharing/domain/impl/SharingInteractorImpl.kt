package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository
) : SharingInteractor {
    override fun shareApp() {
        repository.shareApp()
        //    Поделиться приложением — отправить ссылку на приложение в систему.
    }

    override fun openTerms() {
        repository.openTerms()
        //    Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
    }

    override fun openSupport() {
        //    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
        repository.openSupport()
    }
}
//    Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
//    В коде выше мы превращаем намерения пользователя в конкретные действия:
//    Поделиться приложением — отправить ссылку на приложение в систему.

//    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
//    Эти конкретные действия осуществляются с помощью Android и Context и находятся в com.practicum.playlistmaker.sharing.data.ExternalNavigator.