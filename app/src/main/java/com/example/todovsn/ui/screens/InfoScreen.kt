package com.example.todovsn.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todovsn.R
import com.example.todovsn.ToDoAppBar
import com.example.todovsn.ui.navigation.NavDestination

object InfoDestination : NavDestination {
    override val route = "info"
    override val titleRes = R.string.info
}

private val PrimaryColor = Color(0xFF1E293B)
private val AccentColor = Color(0xFF3B82F6)
private val BackgroundGradientStart = Color(0xFFF8FAFC)
private val BackgroundGradientEnd = Color(0xFFE2E8F0)
private val CardBackground = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ToDoAppBar(
                title = stringResource(InfoDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = onBackPressed
            )
        },
    ) { innerPadding ->
        Info(
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun Info(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundGradientStart,
                        BackgroundGradientEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            DeveloperHeader()

            SectionDivider()

            AboutAppSection()

            SectionDivider()

            BuiltWithSection()

            SectionDivider()

            LinksSection(uriHandler)

            SectionDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "© 2026 Samrat Parajuli",
                fontSize = 13.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionDivider() {
    Spacer(modifier = Modifier.height(32.dp))
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .padding(vertical = 4.dp),
        color = TextSecondary.copy(alpha = 0.15f)
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun DeveloperHeader() {
    Card(
        modifier = Modifier.size(140.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.photo),
            contentDescription = "Developer Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = "Samrat Parajuli",
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = "Android Developer",
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = AccentColor,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "\"Building modern Android apps with Kotlin and Jetpack Compose.\"",
        fontSize = 14.sp,
        fontStyle = FontStyle.Italic,
        color = TextSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
private fun InfoCard(
    icon: String,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 22.sp, modifier = Modifier.padding(end = 8.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun AboutAppSection() {
    InfoCard(icon = "🚀", title = "About the App") {
        Text(
            text = "ToDoVsn is a lightweight task manager built using Jetpack Compose, " +
                    "Material 3, Room, MVVM, Coroutines and Flow.",
            fontSize = 16.sp,
            color = TextSecondary,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Version 1.2.0",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = AccentColor
        )
    }
}

@Composable
private fun BuiltWithSection() {
    InfoCard(icon = "🛠", title = "Built With") {
        val techStack = listOf(
            "Kotlin",
            "Jetpack Compose",
            "Room Database",
            "MVVM",
            "Material 3",
            "Navigation Compose"
        )
        techStack.forEach { tech ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "•",
                    fontSize = 16.sp,
                    color = AccentColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = tech,
                    fontSize = 15.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun LinksSection(uriHandler: UriHandler) {
    InfoCard(icon = "🌐", title = "Links") {
        LinkRow(
            icon = "🌍",
            label = "Portfolio",
            url = "https://www.samratparajuli0.com.np/",
            uriHandler = uriHandler
        )
        LinkRow(
            icon = "💻",
            label = "GitHub",
            url = "https://github.com/SamratVsn",
            uriHandler = uriHandler
        )
        LinkRow(
            icon = "🔗",
            label = "LinkedIn",
            url = "https://www.linkedin.com/in/samratvsn/",
            uriHandler = uriHandler
        )
    }
}

@Composable
private fun LinkRow(
    icon: String,
    label: String,
    url: String,
    uriHandler: UriHandler
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { uriHandler.openUri(url) },
        color = Color(0xFFF8FAFC),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 20.sp, modifier = Modifier.padding(end = 14.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_share),
                contentDescription = "Open $label",
                tint = AccentColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}