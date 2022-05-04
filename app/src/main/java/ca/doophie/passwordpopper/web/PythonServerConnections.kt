package ca.doophie.passwordpopper.web

import android.util.Base64
import android.util.Log
import ca.doophie.passwordpopper.MainActivity
import ca.doophie.passwordpopper.crypto.AESEncrypt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset

@OptIn(DelicateCoroutinesApi::class)
class PythonServerConnections(
    val ip: String,
    val port: Int,
    val key: ByteArray
) {

    private val aesEncryptor = AESEncrypt()

    companion object {
        private const val TAG = "PythonServerConnections"
    }

    private lateinit var socket: Socket

    init {
        connectToPythonServer()
    }

    private fun connectToPythonServer() {
        GlobalScope.launch {
            try {
                socket = Socket(ip, port)
            } catch (e: Exception) {
                Log.e(TAG, "Network Error $e")
            }
        }
    }

    fun sendData(data: String) {
        GlobalScope.launch {
            try {
                val out = DataOutputStream(socket.getOutputStream())

                out.write(Base64.encode(aesEncryptor.encrypt(key, data), Base64.NO_WRAP))

                Log.d(TAG, "Sent data $data")

                out.flush()
            } catch (e: Exception) {
                Log.e(TAG, "Send Data Error: $e")
            }
        }
    }
}