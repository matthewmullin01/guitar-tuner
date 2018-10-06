package com.example.guitartuner


import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import org.puredata.android.io.AudioParameters
import org.puredata.android.service.PdService
import org.puredata.android.utils.PdUiDispatcher
import org.puredata.core.PdBase
import org.puredata.core.PdListener
import java.io.IOException

class Recorder(context: Context) {
    private var dispatcher: PdUiDispatcher? = null
    private var context = context
    private var pdService: PdService? = null

//    private val pdConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            pdService = (service as PdService.PdBinder).service
//            try {
//                initPd()
//                loadPatch()
//            } catch (e: IOException) {
//                Log.e(TAG, e.toString())
////                finish()
//            }
//
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            // this method will never be called
//        }
//    }

    fun begin() {
        initSystemServices()
        initPd()

//       bindService(Intent(this, PdService::class.java), pdConnection, Context.BIND_AUTO_CREATE)
    }

    fun end() {
//        super.onDestroy()
//        unbindService(pdConnection)
    }


    @Throws(IOException::class)
    private fun initPd() {
        pdService = PdService()
        println(TAG + " Initing")

        // Configure the audio glue
//        AudioParameters.init(context)
//        val sampleRate = AudioParameters.suggestSampleRate()
//        pdService!!.initAudio(sampleRate, 1, 2, 10.0f)
        pdService!!.initAudio(8400, 1, 2, 10.0f)
        start()

        // Create and install the dispatcher
        dispatcher = PdUiDispatcher()
        PdBase.setReceiver(dispatcher)
        dispatcher!!.addListener("pitch", object : PdListener.Adapter() {
            override fun receiveFloat(source: String?, x: Float) {
                println(TAG + x.toString() + " Hz")
                // Todo use x -> ie. pitchView!!.setCurrentPitch(x)
            }
        })
    }

    private fun start() {
        if (!pdService!!.isRunning) {


            val intent = Intent(context, MainActivity::class.java)
            pdService!!.startAudio()
//            pdService!!.startAudio(intent, R.drawable.icon,
//                    "GuitarTunerz", "Return to GuitarTunerz.")
        }
    }

//    @Throws(IOException::class)
//    private fun loadPatch() {
//        val dir = filesDir
//        IoUtils.extractZipResource(
//                resources.openRawResource(R.raw.tuner), dir, true)
//        val patchFile = File(dir, "tuner.pd")
//        PdBase.openPatch(patchFile.absolutePath)
//    }

    private fun initSystemServices() {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
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
        // Todo -> pitchView!!.setCenterPitch(n)
    }


//                triggerNote(40) // E (low) is MIDI note 40.
//                triggerNote(45) // A is MIDI note 45.
//                triggerNote(50) // D is MIDI note 50.
//                triggerNote(55) // G is MIDI note 55.
//                triggerNote(59) // B is MIDI note 59.
//                triggerNote(64) // E (high) is MIDI note 64.


    companion object {

        private val TAG = "GuitarTuner"
    }
}