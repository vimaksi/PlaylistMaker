package com.practicum.playlistmaker.settings.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.creator.TracksApplication
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.shareApp.setOnClickListener {
            viewModel.shareLink()
            //Поделиться приложением — отправить ссылку на приложение в систему.

        }

        binding.writeSupport.setOnClickListener {
            viewModel.openEmail()
            //Написать в поддержку — открыть почтовый клиент с адресом поддержки.
        }
        binding.userAgreement.setOnClickListener {
            viewModel.openTerms()
            //Пользовательское соглашение — открыть в браузере ссылку на пользовательское соглашение.
        }


        viewModel.observeState().observe(viewLifecycleOwner) {state ->
            if (binding.themeSwitcher.isChecked != state.isDarkTheme) {
                binding.themeSwitcher.isChecked = state.isDarkTheme
            }
        }

        binding.themeSwitcher.isChecked = viewModel.getTheme()
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.changeTheme(checked)
            (requireActivity().application as TracksApplication).switchTheme(checked)
        }
    }
}