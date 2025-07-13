package com.hocok.fortipass.service

import android.app.assist.AssistStructure
import android.util.Log
import android.view.autofill.AutofillId
import com.hocok.fortipass.service.model.ParseAccount

class AccountParser(private val structure: AssistStructure) {

    var parserStructure: ParseAccount? = null

    val autoFillIds = mutableListOf<AutofillId>()

    fun parseData(){
        for (i in 0 until structure.windowNodeCount){
            Log.d("Stage", "Parse windowNode $i")
            parseChild(structure.getWindowNodeAt(i).rootViewNode)
            Log.d("Stage", "autoFillIds: $autoFillIds")
        }
    }

    private fun parseChild(rootView: AssistStructure.ViewNode){
        for (i in 0 until rootView.childCount){
            val child = rootView.getChildAt(i)
            Log.d("Stage", "Getting hint child ${i}: ${child.autofillHints}")
            val hint = child.autofillHints
            hint?.let {
                autoFillIds.add(child.autofillId!!)
                Log.d("Fount view with autofillId", "${child.autofillId}")
            }
            parseChild(child)
        }
    }

     fun createStructure(){
        parserStructure = ParseAccount(autoFillIds[0], autoFillIds[1])
    }
}