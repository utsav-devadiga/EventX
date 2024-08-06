package com.applabs.eventx.events.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.applabs.eventx.events.domain.model.Event
import com.applabs.eventx.events.util.Screen

/**
 * @author Utsav Devadiga
 */

@Composable
fun EventItem(
    event: Event,
    navHostController: NavHostController
) {

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .width(200.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable {
                navHostController.navigate(Screen.Details.route + "/${event.event_id}")
            }
    ) {
        Text(text = event.event_name)
    }


}