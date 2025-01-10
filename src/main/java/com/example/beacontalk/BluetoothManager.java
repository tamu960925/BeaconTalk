package com.example.beacontalk;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    private static final String TAG = "BluetoothManager";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private ConnectedThread connectedThread;
    private final Context context;
    private MainActivity mainActivity;

    public BluetoothManager(Context context) {
        this.context = context.getApplicationContext();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this.context, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
    }

    public void setMainActivity(MainActivity activity) {
        this.mainActivity = activity;
    }

    public boolean isBluetoothAvailable() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void enableBluetooth(AppCompatActivity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, requestCode);
        } else {
            Toast.makeText(activity, "Bluetooth permissions required", Toast.LENGTH_SHORT).show();
        }
    }

    public Set<BluetoothDevice> getPairedDevices() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            return bluetoothAdapter.getBondedDevices();
        } else {
            Toast.makeText(context, "Bluetooth permissions required", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void makeDiscoverable(Activity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            activity.startActivityForResult(discoverableIntent, requestCode);
        } else {
            Toast.makeText(context, "Bluetooth advertise permission required", Toast.LENGTH_SHORT).show();
        }
    }

    public void startDiscovery() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();
        } else {
            Toast.makeText(context, "Bluetooth scan permission required", Toast.LENGTH_SHORT).show();
        }
    }

    public void connectToDevice(BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                connectedThread = new ConnectedThread(bluetoothSocket);
                connectedThread.start();
                Toast.makeText(context, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Error connecting to device", e);
                Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Bluetooth connect permission required", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(String message) {
        if (connectedThread != null) {
            connectedThread.write(message.getBytes());
        } else {
            Toast.makeText(context, "Not connected to a device", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOnDataReceivedListener(OnDataReceivedListener listener) {
        if (connectedThread != null) {
            connectedThread.setDataReceivedListener(listener);
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private OnDataReceivedListener dataReceivedListener;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error getting streams", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String receivedMessage = new String(buffer, 0, bytes);
                    if (dataReceivedListener != null) {
                        dataReceivedListener.onDataReceived(receivedMessage);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error reading from stream", e);
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "Error writing to stream", e);
            }
        }

        public void setDataReceivedListener(OnDataReceivedListener listener) {
            this.dataReceivedListener = listener;
        }
    }

    public interface OnDataReceivedListener {
        void onDataReceived(String message);
    }
}