package ca.doophie.passwordpopper

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import ca.doophie.passwordpopper.databinding.ActivityMainBinding
import ca.doophie.passwordpopper.fragments.QRCodeReaderFragment
import ca.doophie.passwordpopper.web.PythonServerConnections
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connectionHandler: PythonServerConnections

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.sendButton.setOnClickListener {
            connectionHandler.sendData(binding.inputText.text.toString())
        }

        var qrCodeFrag: QRCodeReaderFragment? = null
        qrCodeFrag = QRCodeReaderFragment { scannedValue ->
            supportFragmentManager.beginTransaction()
                .remove(qrCodeFrag!!)
                .commit()

            // handle scanned value
            val splitValue = scannedValue.split("&")
            connectionHandler = PythonServerConnections(
                splitValue[0],
                splitValue[1].toInt(),
                Base64.decode(splitValue[2], Base64.NO_WRAP)
            )
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.camera_frame_layout, qrCodeFrag)
            .commit()
    }

}