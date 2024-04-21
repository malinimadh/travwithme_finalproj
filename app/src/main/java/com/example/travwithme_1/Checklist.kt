package com.example.travwithme_1

import ChecklistAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class Checklist : AppCompatActivity() {

    private lateinit var checklistAdapter: ChecklistAdapter
    private val checklistItems = mutableListOf<String>()
    private lateinit var microphoneButton: ImageButton
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checklistview)

        val checklistRecyclerView = findViewById<RecyclerView>(R.id.checklistRecyclerView)
        val addItemButton   = findViewById<Button>(R.id.addItemButton)
        val newItemEditText = findViewById<EditText>(R.id.newItemEditText)
        microphoneButton = findViewById(R.id.microphone)

        checklistAdapter = ChecklistAdapter(checklistItems) { position ->
            // Remove the item at the specified position
            checklistItems.removeAt(position)
            // Notify the adapter that the item has been removed
            checklistAdapter.notifyItemRemoved(position)
        }

        // Initialize RecyclerView and adapter
        checklistAdapter = ChecklistAdapter(checklistItems) { position ->
            // Remove item from the list
            checklistItems.removeAt(position)
            checklistAdapter.notifyItemRemoved(position)
        }

        checklistRecyclerView.layoutManager = LinearLayoutManager(this)
        checklistRecyclerView.adapter = checklistAdapter

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                // Get the recognized speech text
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.get(0)?.let { spokenText ->
                    // Add the recognized text (spoken item) to the checklist
                    checklistItems.add(spokenText)
                    checklistAdapter.notifyItemInserted(checklistItems.size - 1)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        // Set long click listener on microphone button to start speech recognition
        microphoneButton.setOnLongClickListener {
            startRecording()
            true // Consume the long press event
        }

        // Set click listener on microphone button to stop speech recognition
        microphoneButton.setOnClickListener {
            stopRecording()
        }

        // Button click listener to add new item to checklist
        addItemButton.setOnClickListener {
            val newItem = newItemEditText.text.toString().trim()
            if (newItem.isNotEmpty()) {
                checklistItems.add(newItem)
                checklistAdapter.notifyItemInserted(checklistItems.size - 1)
                newItemEditText.text.clear()
            }
        }
    }
    private fun startRecording() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.startListening(intent)
    }

    private fun stopRecording() {
        speechRecognizer.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}
