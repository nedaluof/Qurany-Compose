package com.nedaluof.data.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.nedaluof.data.enum.ConnectivityStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by nedaluof on 1/31/2021.
 */
@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
fun Context.connectivityFlow() = callbackFlow {
  val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  // Create Callback
  val callback = networkCallback { connectionState ->
    trySend(connectionState)
  }

  // Register Callback
  connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)

  // Set current state
  val currentState = getCurrentConnectivityState(connectivityManager)
  trySend(currentState)

  // Remove callback when not used
  awaitClose {
    // Remove listeners
    connectivityManager.unregisterNetworkCallback(callback)
  }
}

@SuppressLint("MissingPermission")
private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): ConnectivityStatus {
  var currentState = ConnectivityStatus.NOT_CONNECTED

  // Retrieve current status of connectivity
  connectivityManager.allNetworks.forEach { network ->
    val networkCapability = connectivityManager.getNetworkCapabilities(network)

    networkCapability?.let {
      if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
        currentState = ConnectivityStatus.CONNECTED
        return@forEach
      }
    }
  }
  return currentState
}

fun networkCallback(callback: (ConnectivityStatus) -> Unit): ConnectivityManager.NetworkCallback {
  return object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
      callback(ConnectivityStatus.CONNECTED)
    }

    override fun onLost(network: Network) {
      callback(ConnectivityStatus.NOT_CONNECTED)
    }
  }
}