package com.applabs.eventx.events.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.applabs.eventx.events.domain.model.Event
import com.applabs.eventx.events.util.Screen

@Composable
fun EventItem(
    event: Event,
    navHostController: NavHostController
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .wrapContentHeight()
            .padding(8.dp)
            .clickable {
                navHostController.navigate(Screen.Details.route + "/${event.event_id}")
            }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = event.event_name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventDetailItem(
                    icon = Icons.Default.Place,
                    text = event.event_location
                )
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                )
                EventDetailItem(
                    icon = Icons.Default.Event,
                    text = event.event_timeStamp
                )
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                )
                EventDetailItem(
                    icon = Icons.Default.Schedule,
                    text = event.event_duration
                )
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                )
                EventDetailItem(
                    icon = Icons.Default.Group,
                    text = "${event.event_participants.size} people"
                )
            }
        }
    }
}

@Composable
fun EventDetailItem(icon: ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .padding(8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
