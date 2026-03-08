package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import androidx.core.net.toUri
import com.practicum.playlistmaker.sharing.data.storage.ExternalNavigator

class ExternalNavigatorImpl(
    private val context: Context
): ExternalNavigator {
    // Например, для sharing удобно сделать класс
    // ExternalNavigator, который будет отправлять Intent для открытия нужных приложений в системе.
    //    В коде выше мы превращаем намерения пользователя в конкретные действия:
//    Эти конкретные действия осуществляются с помощью Android и Context и находятся в ExternalNavigator.
    override fun shareLink(getShareAppLink: String) {
        //    Поделиться приложением — отправить ссылку на приложение в систему.
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getShareAppLink)
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_with))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    }

    override fun openTerms(getTermsLink: String) {
        //    Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
        val uri = getTermsLink.toUri()
        val browseIntent = Intent(Intent.ACTION_VIEW, uri)
        browseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browseIntent)
    }

    override fun openEmail(getSupportEmailData: EmailData) {
        //    Написать в поддержку — открыть почтовый клиент с адресом поддержки.
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.data = context.getString(R.string.mail_to).toUri()
        sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getSupportEmailData.email))
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getSupportEmailData.subject)
        sendIntent.putExtra(Intent.EXTRA_TEXT, getSupportEmailData.text)
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(sendIntent)
    }
}
