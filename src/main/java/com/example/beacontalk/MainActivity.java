package com.example.beacontalk;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BluetoothManager bluetoothManager;
    private static final String TAG = "BeaconTalk";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private TextView chatTextView;
    private EditText messageEditText;
    private Button sendButton;
    private final Handler handler = new Handler();
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_DISCOVERABLE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        chatTextView = findViewById(R.id.chatTextView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Initialize Bluetooth Manager
        bluetoothManager = new BluetoothManager(this);
        if (!bluetoothManager.isBluetoothAvailable()) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Enable Bluetooth if not enabled
        if (!bluetoothManager.isBluetoothEnabled()) {
            bluetoothManager.enableBluetooth(this, REQUEST_ENABLE_BT);
        } else {
            setupBluetooth();
        }

        // Set up data received listener
        bluetoothManager.setOnDataReceivedListener(message -> appendToChat("Peer: " + message));

        // Set up send button click listener
        sendButton.setOnClickListener(v -> sendMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                setupBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth is required", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == REQUEST_DISCOVERABLE) {
            if (resultCode == RESULT_OK) {
                startDiscovery();
            } else {
                Toast.makeText(this, "Device must be discoverable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupBluetooth() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) ==
            PackageManager.PERMISSION_GRANTED) {
            // Get paired devices
            Set<BluetoothDevice> pairedDevices = bluetoothManager.getPairedDevices();
            if (pairedDevices != null && pairedDevices.size() > 0) {
                // Show paired devices dialog
                showDeviceSelectionDialog(pairedDevices);
            } else {
                // Start discovery if no paired devices
                makeDiscoverable();
            }
        } else {
            Toast.makeText(this, "Bluetooth permissions required", Toast.LENGTH_SHORT).show();
            checkBluetoothPermissions();
        }
    }

    private void showDeviceSelectionDialog(Set<BluetoothDevice> devices) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) ==
            PackageManager.PERMISSION_GRANTED) {
            String[] deviceNames = new String[devices.size()];
            final BluetoothDevice[] deviceArray = devices.toArray(new BluetoothDevice[0]);

            int i = 0;
            for (BluetoothDevice device : devices) {
                deviceNames[i++] = device.getName() + "\n" + device.getAddress();
            }

            new AlertDialog.Builder(this)
                .setTitle("Select a device")
                .setItems(deviceNames, (dialog, which) -> connectToDevice(deviceArray[which]))
                .setNegativeButton("Cancel", null)
                .show();
        } else {
            Toast.makeText(this, "Bluetooth permissions required", Toast.LENGTH_SHORT).show();
            checkBluetoothPermissions();
        }
    }

    private void makeDiscoverable() {
        bluetoothManager.makeDiscoverable(this, REQUEST_DISCOVERABLE);
    }

    private void startDiscovery() {
        bluetoothManager.startDiscovery();
    }

    private void connectToDevice(BluetoothDevice device) {
        bluetoothManager.connectToDevice(device);
    }

    private void checkBluetoothPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                }, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, continue with Bluetooth setup
                if (!bluetoothManager.isBluetoothEnabled()) {
                    bluetoothManager.enableBluetooth(this, REQUEST_ENABLE_BT);
                } else {
                    setupBluetooth();
                }
            } else {
                Toast.makeText(this, "Permissions are required for Bluetooth functionality", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString();
        if (!message.isEmpty()) {
            bluetoothManager.sendMessage(message);
            appendToChat("You: " + message);
            messageEditText.setText("");
        }
    }

    private void appendToChat(final String message) {
        handler.post(() -> {
            String currentText = chatTextView.getText().toString();
            chatTextView.setText(currentText + "\n" + message);
        });
    }
}