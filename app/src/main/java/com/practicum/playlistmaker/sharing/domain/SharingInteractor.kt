package com.practicum.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp()
    //    Поделиться приложением — отправить ссылку на приложение в систему.
    fun openTerms()
    fun openSupport()
    //    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
//    SharingInteractor для передачи информации в сторонние приложения
}