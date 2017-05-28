package com.example.andrej.mit_lab_3

import android.content.Context
import android.graphics.Color
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.widget.RelativeLayout
import android.widget.TextView
import org.openintents.sensorsimulator.hardware.Sensor
import org.openintents.sensorsimulator.hardware.SensorEvent
import org.openintents.sensorsimulator.hardware.SensorEventListener
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val info = findViewById(R.id.infoLayout) as RelativeLayout
        val textView = findViewById(R.id.textView) as TextView


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val task = SystemManagerSimulatorConnector(this)
        task.execute()
        systemManagerSimulator = task.systemManagerSimulator

        accelerometerListener = object : SensorEventListener {
            val coordinateTextViews = info.getCoordinateTextViews()

            var currentTime = 0L
            var lastTime = 0L
            val DELAY = 200L

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

            override fun onSensorChanged(event: SensorEvent?) {
//                if (event?.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                if (event?.type == Sensor.TYPE_ACCELEROMETER) {
                    val coordinates = event.values?.getCoordinate() ?: CoordinateSet(-1F, 0F, 1F)
                    val (x, y, z) = coordinates

                    val accelerationSquareRoot = (x * x + y * y + z * z) /
                            (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)

//                    val accelerationSquareRoot = Math.sqrt((x * x + y * y + z * z).toDouble())
                    currentTime = System.currentTimeMillis()

                    if (accelerationSquareRoot > 1.1 && isDelayPass(currentTime, lastTime)) {
                        coordinateTextViews(coordinates)
                        textView.text = coordinates.toString()

                        with(this@MainActivity) {
                            findViewById(R.id.rootLayout).setBackgroundColor(randomColor())
                            findViewById(R.id.frame).setBackgroundColor(coordinates.accelerationVisualisation())
                        }

                        lastTime = System.currentTimeMillis()
                    }
                }
            }

            private fun isDelayPass(currentTime: Long, lastTime: Long) =
                    currentTime - lastTime >= DELAY
        }
    }


    var accelerometerListener by Delegates.notNull<SensorEventListener>()
    var systemManagerSimulator by Delegates.notNull<SensorManagerSimulator>()

    override fun onResume() {
        super.onResume()
        Register(systemManagerSimulator).execute(accelerometerListener)
    }

    override fun onStop() {

        Unregister(systemManagerSimulator).execute(accelerometerListener)
        super.onStop()
    }
}


class Unregister(val systemManagerSimulator: SensorManagerSimulator) : AsyncTask<SensorEventListener, Unit, Unit>() {

    override fun doInBackground(vararg params: SensorEventListener?) {
        systemManagerSimulator.unregisterListener(params[0])
    }
}

class Register(val systemManagerSimulator: SensorManagerSimulator) : AsyncTask<SensorEventListener, Unit, Unit>() {

    override fun doInBackground(vararg params: SensorEventListener?) {
        systemManagerSimulator.registerListener(params[0],
                systemManagerSimulator.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST)
    }
}

class SystemManagerSimulatorConnector(mainActivity: MainActivity) : AsyncTask<Unit, Unit, SensorManagerSimulator>() {
    val systemManagerSimulator = SensorManagerSimulator.getSystemService(mainActivity, Context.SENSOR_SERVICE)

    override fun doInBackground(vararg params: Unit?): SensorManagerSimulator {
        systemManagerSimulator.connectSimulator()
        return systemManagerSimulator
    }
}


fun randomInt(max: Int) = Math.round(max * Math.random()).toInt()


fun randomColor() = Color.rgb(
        randomInt(256),
        randomInt(256),
        randomInt(256))

data class CoordinateTextView(val textViewX: TextView,
                              val textViewY: TextView,
                              val textViewZ: TextView)

data class CoordinateSet<T>(val x: T, val y: T, val z: T)

fun CoordinateSet<Float>.accelerationVisualisation() = Color.rgb(
        Math.round(255 * x * x / (x * x + y * y + z * z)),
        Math.round(255 * y * y / (x * x + y * y + z * z)),
        Math.round(255 * z * z / (x * x + y * y + z * z))
)

fun FloatArray.getCoordinate() = CoordinateSet(get(0), get(1), get(2))

fun RelativeLayout.getCoordinateTextViews() = CoordinateTextView(
        findViewById(R.id.textViewX) as TextView,
        findViewById(R.id.textViewY) as TextView,
        findViewById(R.id.textViewZ) as TextView)

operator fun CoordinateTextView.invoke(coordinateSet: CoordinateSet<*>) =
        with(coordinateSet) {
            textViewX.text = x.toString()
            textViewY.text = y.toString()
            textViewZ.text = z.toString()
        }