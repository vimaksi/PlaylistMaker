package com.example.practicum.playlist.domain.sharing.impl

import android.content.Context
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
        //    Поделиться приложением — отправить ссылку на приложение в систему.
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
        //    Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
    }

    override fun openSupport() {
        //    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.link_android_developer)
    }

    private fun getSupportEmailData(): EmailData {
        return  EmailData(context.getString(R.string.recipient),
            context.getString(R.string.letters_theme),
            context.getString(R.string.letter_text))
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.agreement)
        //    Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
    }
//    В коде выше мы превращаем намерения пользователя в конкретные действия:
//    Поделиться приложением — отправить ссылку на приложение в систему.

//    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
//    Эти конкретные действия осуществляются с помощью Android и Context и находятся в com.practicum.playlistmaker.sharing.data.ExternalNavigator.
}