package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingRepository
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingRepositoryImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : SharingRepository {
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
        return EmailData(
            context.getString(R.string.recipient),
            context.getString(R.string.letters_theme),
            context.getString(R.string.letter_text)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.agreement)
    }
}