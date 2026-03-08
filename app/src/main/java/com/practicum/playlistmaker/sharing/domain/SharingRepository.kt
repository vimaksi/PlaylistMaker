package com.practicum.playlistmaker.sharing.domain

interface SharingRepository{
    fun shareLink()
    fun openTerms()
    fun openEmail()
}