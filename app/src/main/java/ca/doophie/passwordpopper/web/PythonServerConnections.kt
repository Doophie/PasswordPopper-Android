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
import java.io.DataInputStream
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

    fun connectToPythonServer(complete: (Boolean)->Unit) {
        GlobalScope.launch {
            try {
                socket = Socket(ip, port)

                testConnection(complete)
            } catch (e: Exception) {
                Log.e(TAG, "Network Error $e")
                complete(false)
            }
        }
    }

    fun testConnection(confirmCallback: (Boolean)->Unit) {
        GlobalScope.launch {
            try {
                val out = DataOutputStream(socket.getOutputStream())

                out.write("ping-test".toByteArray(Charset.forName("utf-8")))

                Log.d(TAG, "Sent ping test")

                out.flush()

                val inS = DataInputStream(socket.getInputStream())

                val readBytes = ByteArray(1024)
                val r = inS.read(readBytes)

                Log.d(TAG, "Got ${String(readBytes, 0, r, Charset.forName("utf-8"))}")

                confirmCallback(String(readBytes, 0, r, Charset.forName("utf-8")) == "ack")
            } catch (e: Exception) {
                Log.e(TAG, "Send Ping Error: $e")
                confirmCallback(false)
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