package ca.doophie.passwordpopper

import android.app.Activity
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.content.ContextCompat
import ca.doophie.passwordpopper.databinding.ActivityMainBinding
import ca.doophie.passwordpopper.fragments.AllCredentialsFragment
import ca.doophie.passwordpopper.fragments.QRCodeReaderFragment
import ca.doophie.passwordpopper.web.PythonServerConnections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var connectionHandler: PythonServerConnections? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, AllCredentialsFragment())
            .addToBackStack("AllCreds")
            .commit()

        binding.connectionButton.setOnClickListener {
            connectToPythonServer()
        }
    }

    private fun connectToPythonServer() {
        var qrCodeFrag: QRCodeReaderFragment? = null
        qrCodeFrag = QRCodeReaderFragment { scannedValue ->

            // handle scanned value
            val splitValue = scannedValue.split("&")
            connectionHandler = PythonServerConnections(
                splitValue[0],
                splitValue[1].toInt(),
                Base64.decode(splitValue[2], Base64.NO_WRAP)
            )

            connectionHandler?.connectToPythonServer { success ->
                onConnectionResult(success)
            }

            supportFragmentManager.popBackStack("AllCreds", 0)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, qrCodeFrag)
            .addToBackStack("QRCode")
            .commit()
    }

    private fun onConnectionResult(success: Boolean) {
        if (success) {
            testContinuedConnection()
            binding.connectionButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_green_light))
            val connectionString = "${getString(R.string.connected)} to ${connectionHandler!!.ip}:${connectionHandler!!.port}"
            binding.isConnectedText.text = connectionString
        } else {
            binding.connectionButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.isConnectedText.text = getString(R.string.not_connected)
        }
    }

    private fun testContinuedConnection() {
        GlobalScope.async {
            Timer("Connection test", false).schedule(object : TimerTask() {
                override fun run() {
                    connectionHandler?.testConnection { success ->
                        onConnectionResult(success)
                    }
                }
            },5000)
        }
    }

}