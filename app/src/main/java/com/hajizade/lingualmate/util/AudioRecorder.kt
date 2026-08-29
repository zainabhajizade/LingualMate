package com.hajizade.lingualmate.util


import android.content.Context
import android.media.MediaRecorder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AudioRecorder @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null

    fun startRecording(): File? {
        outputFile = File(context.cacheDir, "audio_${System.currentTimeMillis()}.m4a")

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile?.absolutePath)

            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
                outputFile = null
            }
        }
        return outputFile
    }

    fun stopRecording(): File? {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaRecorder = null
        }
        return outputFile
    }
}