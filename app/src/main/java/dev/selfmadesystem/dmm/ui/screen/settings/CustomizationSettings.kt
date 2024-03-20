package dev.selfmadesystem.dmm.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.selfmadesystem.dmm.R
import dev.selfmadesystem.dmm.domain.manager.InstallManager
import dev.selfmadesystem.dmm.domain.manager.PreferenceManager
import dev.selfmadesystem.dmm.ui.components.settings.SettingsItemChoice
import dev.selfmadesystem.dmm.ui.components.settings.SettingsSwitch
import dev.selfmadesystem.dmm.ui.components.settings.SettingsTextField
import dev.selfmadesystem.dmm.utils.DimenUtils
import org.koin.androidx.compose.get

class CustomizationSettings : Screen {

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content() {
        val ctx = LocalContext.current
        val installManager: InstallManager = get()
        val prefs: PreferenceManager = get()
        var profile by remember { mutableStateOf(prefs.currentProfile) }

        val currentProfileName = prefs.getCurrentProfileName()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            topBar = { TitleBar(scrollBehavior) },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { pv ->
            Column(
                modifier = Modifier
                    .padding(pv)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = DimenUtils.navBarPadding)
            ) {
                SettingsTextField(
                    label = "Profile Name", // TODO: stringResource(R.string.settings_app_name),
                    pref = currentProfileName,
                    onPrefChange = {
                        prefs.setCurrentProfileName(it)
                    }
                )

                SettingsTextField(
                    label = stringResource(R.string.settings_app_name),
                    pref = profile.appName,
                    onPrefChange = {
                        profile = profile.copy(appName = it)
                        prefs.currentProfile = profile
                    }
                )

                SettingsTextField(
                    label = stringResource(R.string.settings_package_name),
                    pref = profile.packageName,
                    onPrefChange = {
                        profile = profile.copy(packageName = it)
                        prefs.currentProfile = profile
                    }
                )

                SettingsSwitch( // TODO: Switch over to better icon management
                    label = stringResource(R.string.settings_app_icon),
                    secondaryLabel = stringResource(R.string.settings_app_icon_description),
                    pref = prefs.patchIcon,
                    onPrefChange = {
                        prefs.patchIcon = it
                    }
                )

                SettingsItemChoice(
                    label = stringResource(R.string.settings_channel),
                    pref = profile.channel,
                    labelFactory = {
                        ctx.getString(it.labelRes)
                    },
                    onPrefChange = {
                        profile = profile.copy(channel = it)
                        prefs.currentProfile = profile
                    }
                )
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun TitleBar(
        scrollBehavior: TopAppBarScrollBehavior
    ) {
        val navigator = LocalNavigator.currentOrThrow

        LargeTopAppBar(
            title = {
                Text(stringResource(R.string.settings_customization))
            },
            navigationIcon = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.action_back)
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }

}