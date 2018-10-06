package com.example.guitartuner

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import io.flutter.plugin.common.EventChannel



import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import org.puredata.android.io.AudioParameters
import org.puredata.android.service.PdService
import org.puredata.android.utils.PdUiDispatcher
import org.puredata.core.PdBase
import org.puredata.core.PdListener
import java.io.IOException
import android.content.ComponentName
import android.os.IBinder
import android.content.ServiceConnection
import org.puredata.android.io.PdAudio.stopAudio
import org.puredata.core.utils.IoUtils
import org.puredata.android.io.PdAudio.initAudio
import java.io.File
import kotlin.coroutines.experimental.coroutineContext


@TargetApi(Build.VERSION_CODES.CUPCAKE)
class MainActivity : FlutterActivity() {

    private val CHANNEL = "matthew.mullin.io/audio"
    private val STREAM = "matthew.mullin.io/stream";

    private var eventSink: EventChannel.EventSink? = null

    private var pdService: PdService? = null
    private var dispatcher: PdUiDispatcher? = null

    private val pdConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            pdService = (service as PdService.PdBinder).service
            try {
                initPd()
                loadPatch()
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
                finish()
            }

        }

        override fun onServiceDisconnected(name: ComponentName) {
            // this method will never be called
        }
    }

    @Throws(IOException::class)
    private fun initPd() {
        // Configure the audio glue
        AudioParameters.init(this)
        val sampleRate = AudioParameters.suggestSampleRate()
        pdService!!.initAudio(sampleRate, 1, 2, 10.0f)
//        start()

        // Create and install the dispatcher
        dispatcher = PdUiDispatcher()
        PdBase.setReceiver(dispatcher)
        dispatcher!!.addListener("pitch", object : PdListener.Adapter() {
            override fun receiveFloat(source: String?, x: Float) {
                eventSink?.success(x)
            }

        })
    }

    private fun start() {
        if (!pdService!!.isRunning) {
            val intent = Intent(this@MainActivity,
                    MainActivity::class.java)

            pdService!!.startAudio(intent, R.drawable.icon,
                    "GuitarTuner", "Return to GuitarTuner.")

        }
    }

    @Throws(IOException::class)
    private fun loadPatch() {
        val dir = filesDir
        IoUtils.extractZipResource(
                resources.openRawResource(R.raw.tuner), dir, true)
        val patchFile = File(dir, "tuner.pd")
        PdBase.openPatch(patchFile.absolutePath)
    }

    private fun initSystemServices() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                if (pdService == null) return
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    start()
                } else {
                    pdService!!.stopAudio()
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun triggerNote(n: Int) {
        PdBase.sendFloat("midinote", n.toFloat())
        PdBase.sendBang("trigger")
//        pitchView.setCenterPitch(n)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)


//        val recorder = Recorder(applicationContext)
        initSystemServices()
        bindService(Intent(this@MainActivity, PdService::class.java), pdConnection, Context.BIND_AUTO_CREATE)


        MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "start_recording" -> {
                    // soundMeter.start()
//                    recorder.begin()
                    result.success("Started recording")
                }
                "stop_recording" -> {
                    // soundMeter.stop()
                    result.success("Stopped recording")
                }
                "get_amplitude" -> {
                    // val amplitude = soundMeter.amplitude
                    // result.success(amplitude)
//                    result.success(...)
                }
                else -> result.notImplemented()
            }
        }


        EventChannel(flutterView, STREAM).setStreamHandler(
                object : EventChannel.StreamHandler {
                    override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                        Log.w(TAG, "adding listener")
                        println("Matthew initing")
                        eventSink = events
                        start()
                        // Todo add send stream data events.success(...)
                    }

                    override fun onCancel(arguments: Any?) {
                        Log.w(TAG, "cancelling listener")
                        // Todo add Cancel stream logic
                    }
                }
        )
    }

}

