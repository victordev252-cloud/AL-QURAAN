package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.localization.Strings
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.QuranGold

enum class NavTab {
    HOME,
    QURAN,
    SEARCH,
    TASBIH,
    BOOKMARKS,
    SETTINGS
}

@Composable
fun QuranBottomNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    strings: Strings,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            NavTab.HOME to (strings.home to (Icons.Filled.Home to Icons.Outlined.Home)),
            NavTab.QURAN to (strings.quran to (Icons.Filled.MenuBook to Icons.Outlined.MenuBook)),
            NavTab.SEARCH to (strings.search to (Icons.Filled.Search to Icons.Outlined.Search)),
            NavTab.TASBIH to (strings.tasbih to (Icons.Filled.AutoAwesome to Icons.Outlined.AutoAwesome)),
            NavTab.BOOKMARKS to (strings.bookmarks to (Icons.Filled.Bookmarks to Icons.Outlined.Bookmarks)),
            NavTab.SETTINGS to (strings.settings to (Icons.Filled.Settings to Icons.Outlined.Settings))
        )

        items.forEach { (tab, details) ->
            val (label, icons) = details
            val (filledIcon, outlinedIcon) = icons
            val isSelected = selectedTab == tab

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) filledIcon else outlinedIcon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontFamily = OutfitFontFamily,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = PrimaryGreen,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
            )
        }
    }
}
