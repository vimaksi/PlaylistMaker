package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository
) : SharingInteractor {
    override fun shareLink() {
        repository.shareLink()
        //    Поделиться приложением — отправить ссылку на приложение в систему.
    }

    override fun openTerms() {
        repository.openTerms()
        //    Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
    }

    override fun openEmail() {
        //    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
        repository.openEmail()
    }
}
//    Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
//    В коде выше мы превращаем намерения пользователя в конкретные действия:
//    Поделиться приложением — отправить ссылку на приложение в систему.

//    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
//    Эти конкретные действия осуществляются с помощью Android и Context и находятся в com.practicum.playlistmaker.sharing.data.ExternalNavigator.