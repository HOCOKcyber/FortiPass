package com.hocok.fortipass.search

import android.app.Activity
import android.app.assist.AssistStructure
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.autofill.Dataset
import android.service.autofill.FillResponse
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillManager
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.service.AccountParser
import com.hocok.fortipass.service.model.ParseAccount

class SearchActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("autoFill search Activity", "start")


        val structure = intent.getParcelableExtra<AssistStructure>(AutofillManager.EXTRA_ASSIST_STRUCTURE)!!

        val parser = AccountParser(structure)
        parser.parseData()

        parser.createStructure()

        val parseAccount = parser.parserStructure!!

        Log.d("autofill SearchActivity", "$parseAccount")

        sendResultToApp(
            text = "WOW",
            parseAccount)


        setContent {
            FortiPassTheme {
                val text by remember { mutableStateOf("wewe") }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    /*TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        }
                    )*/
                    Button(
                        onClick = {
                            Log.d("search Activity", "send prepare")

                        }
                    ) {
                        Text(
                            text = "Send"
                        )
                    }
                }
            }
        }


    }

    private fun sendResultToApp(text: String, parseAccount: ParseAccount) {
        val preview = RemoteViews(packageName, R.layout.layout)
        preview.setTextViewText(R.id.text, "Найти в FortiPass")

        val dataset = Dataset.Builder()
                .setValue(
                    parseAccount.accountId,
                    AutofillValue.forText("e,deldeld"),
                    preview
                )
                .setValue(
                    parseAccount.passwordId,
                    AutofillValue.forText("password from FortiPass")
                )
                .build()

        Log.d("search Activity", "Create fillResponse")
        val fillResponse = FillResponse.Builder()
            .addDataset(dataset)
            .build()

        val replyIntent = Intent().apply {
            putExtra(AutofillManager.EXTRA_AUTHENTICATION_RESULT, fillResponse)
        }

        Log.d("search Activity", "send: $text")
        Log.d("search Activity", "Dataset response: $dataset")
        this.setResult(Activity.RESULT_OK, replyIntent)
        Log.d("search Activity", "result: " +
                "${replyIntent.getParcelableExtra<FillResponse>(AutofillManager.EXTRA_AUTHENTICATION_RESULT)}")
        this.finish()
    }
}

