package ca.doophie.passwordpopper

import android.app.Activity
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.content.ContextCompat
import ca.doophie.passwordpopper.data.ConnectionParam
import ca.doophie.passwordpopper.data.DatabaseHandler
import ca.doophie.passwordpopper.databinding.ActivityMainBinding
import ca.doophie.passwordpopper.fragments.AllCredentialsFragment
import ca.doophie.passwordpopper.fragments.QRCodeReaderFragment
import ca.doophie.passwordpopper.web.PythonServerConnections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    var connectionHandler: PythonServerConnections? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        DatabaseHandler.init(this)

        GlobalScope.launch {
            DatabaseHandler.instance?.connectionParamDao()?.getAll()?.forEach { connectionParam ->
                try {
                    connectToPythonServer(
                        connectionParam.connectionIP,
                        connectionParam.port,
                        connectionParam.key
                    )
                } catch (e: Exception) {
                    Log.d(TAG, "Failed connection to ${connectionParam.connectionIP}")
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, AllCredentialsFragment())
            .addToBackStack("AllCreds")
            .commit()

        binding.connectionButton.setOnClickListener {
            connectToPythonServer()
        }
    }

    override fun onResume() {
        super.onResume()

        testContinuedConnection()
    }

    private fun connectToPythonServer(ip: String, port: Int, b64Key: String) {
        // handle scanned value
        connectionHandler = PythonServerConnections(ip, port, Base64.decode(b64Key, Base64.NO_WRAP))

        connectionHandler?.connectToPythonServer { success ->
            if (success) {
                DatabaseHandler.instance?.connectionParamDao()?.insertAll(ConnectionParam(
                    ip,
                    port,
                    b64Key
                ))
            }

            onConnectionResult(success)
        }

        supportFragmentManager.popBackStack("AllCreds", 0)
    }

    private fun connectToPythonServer() {
        val qrCodeFrag = QRCodeReaderFragment.withCallback { scannedValue ->
            val splitValue = scannedValue.split("&")

            connectToPythonServer(splitValue[0], splitValue[1].toInt(), splitValue[2])
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, qrCodeFrag)
            .addToBackStack("QRCode")
            .commit()
    }

    private var failCount = 0

    private fun onConnectionResult(success: Boolean) {
        if (success) {
            failCount = 0
            testContinuedConnection()
            binding.connectionButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_green_light))
            val connectionString = "${getString(R.string.connected)} to ${connectionHandler!!.ip}:${connectionHandler!!.port}"
            binding.isConnectedText.text = connectionString
        } else {
            if (failCount > 4) {
                binding.connectionButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                binding.isConnectedText.text = getString(R.string.not_connected)
            } else {
                failCount += 1
                testContinuedConnection()
            }
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