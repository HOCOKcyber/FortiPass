package com.hocok.fortipass.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.assist.AssistStructure
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillContext
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.InlinePresentation
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.autofill.inline.v1.InlineSuggestionUi
import com.hocok.fortipass.R
import com.hocok.fortipass.search.SearchActivity
import com.hocok.fortipass.service.model.ParseAccount
//createTotpCopyIntentSender
class FortiPassAutoFill: AutofillService() {
    
    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {

        Log.d("Stage", "Start")
        val context: List<FillContext> = request.fillContexts
        val structure: AssistStructure = context.last().structure

        val packageName = "com.hocok.fortipass"

        Log.d("Stage", "Parse")
        val parser = AccountParser(structure)
        parser.parseData()

        parser.createStructure()
        val parseStructure = parser.parserStructure!!

        withActivity(
            parseStructure = parseStructure,
            callback = callback,
            ignore = parser.autoFillIds.subList(2, parser.autoFillIds.size),
            request = request,
        )


//        withOutActivity(
//            parseStructure = parseStructure,
//            callback = callback
//        )

    }

    private fun withActivity(
        parseStructure: ParseAccount,
        callback: FillCallback,
        ignore: List<AutofillId>,
        request: FillRequest,
    ){

        val packageName = "com.hocok.fortipass"


        Log.d("Stage", "Intent")
        val intent = Intent(this, SearchActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val intentSender = pendingIntent.intentSender


        Log.d("Stage", "Preview")
        val preview = RemoteViews(packageName, R.layout.layout)
        preview.setTextViewText(R.id.text, "Найти в FortiPass")

        Log.d("Stage", "FillRespone")
        val fillResponse = FillResponse.Builder()
            .setAuthentication(arrayOf(parseStructure.accountId, parseStructure.passwordId), intentSender, preview)
//            .addDataset(
//                Dataset.Builder()
//                    .setValue(parseStructure.accountId,AutofillValue.forText("wwewewe"), preview)
//                    .setValue(
//                        parseStructure.passwordId,
//                        null
//                    )
//                    .setInlinePresentation(inlinePresentation)
//                    .setAuthentication(intentSender)
//                    .build()
//            )
            .apply {
                for (id in ignore){
                    setIgnoredIds(id)
                }
            }
            .build()

        Log.d("Stage", "CallBack")
        callback.onSuccess(fillResponse)
    }

    private fun withOutActivity(
        parseStructure: ParseAccount,
        callback: FillCallback,
    ){
        val packageName = "com.hocok.fortipass"

        Log.d("Stage", "Create createRemoteView")
        val loginPresentation = RemoteViews(packageName, R.layout.layout)
        loginPresentation.setTextViewText(R.id.text, "login")

        val passwordPresentation = RemoteViews(packageName, R.layout.layout)
        passwordPresentation.setTextViewText(R.id.text, "password")

        Log.d("Stage", "Create fillResponse")
        val fillResponse: FillResponse = FillResponse.Builder()
            .addDataset(
                Dataset.Builder()
                    .setValue(
                        parseStructure.accountId,
                        AutofillValue.forText("login from FortiPass"),
                        loginPresentation
                    )
                    .setValue(
                        parseStructure.passwordId,
                        AutofillValue.forText("password from FortiPass"),
                    )

                    .build())
            .build()

        Log.d("Stage", "pass")
        callback.onSuccess(fillResponse)
    }

    override fun onSaveRequest(p0: SaveRequest, p1: SaveCallback) {
    }
}




//        val slice = InlineSuggestionUi.newContentBuilder(pendingIntent)
//            .setContentDescription("Content Description")
//            .setTitle("Login")
//            .build()
//            .slice
//
//        val inlineSpec = request.inlineSuggestionsRequest?.inlinePresentationSpecs?.last()
//
//        val inlinePresentation =
//            InlinePresentation(
//                slice,
//                inlineSpec!!,
//                false,
//            )