package com.example.linguasyne

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linguasyne.ui.theme.LinguaSyneTheme
import org.intellij.lang.annotations.JdkConstants

class RevisionSummaryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ReviewTermViewModel()

        setContent {

            LinguaSyneTheme(
                false,
            ) {
                Surface(
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    Summary(
                        SummaryValues(
                            viewModel.summaryTotalCorrect,
                            viewModel.summaryTotalIncorrect
                        )
                    )
                }
            }
        }
    }

    data class SummaryValues(val totalCorrect: Int, val totalIncorrect: Int)


    @Composable
    fun Summary(sv: SummaryValues) {
        Column(
            modifier = Modifier
                .padding(all = 10.dp)
                //.background(MaterialTheme.colors.background)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Summary",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h1
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Total correct: ${sv.totalCorrect}",
                color = MaterialTheme.colors.primary
            )
            Text(
                "Total incorrect: ${sv.totalIncorrect}",
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { this@RevisionSummaryActivity.finish() },
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            )
            {
                Text("Continue")
            }
        }
    }

    @Preview
    @Composable
    fun PreviewSummary() {
        LinguaSyneTheme(
            darkTheme = false
        ) {
            Surface(
                modifier = Modifier.background(Color.Red)
            ) {
                Summary(SummaryValues(10, 10))
            }
        }
    }
}