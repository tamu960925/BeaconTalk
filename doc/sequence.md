# シーケンス図

## Bluetooth接続シーケンス
```mermaid
sequenceDiagram
    participant User
    participant MainActivity
    participant BluetoothAdapter
    participant BluetoothDevice
    participant ConnectedThread

    User->>MainActivity: アプリ起動
    MainActivity->>BluetoothAdapter: Bluetooth有効化要求
    BluetoothAdapter-->>MainActivity: Bluetooth状態
    MainActivity->>BluetoothAdapter: ペアリング済みデバイス取得
    BluetoothAdapter-->>MainActivity: デバイスリスト
    MainActivity->>User: デバイス選択表示
    User->>MainActivity: デバイス選択
    MainActivity->>BluetoothDevice: 接続要求
    BluetoothDevice-->>MainActivity: 接続結果
    MainActivity->>ConnectedThread: 通信スレッド開始
```

## メッセージ送信シーケンス
```mermaid
sequenceDiagram
    participant User
    participant MainActivity
    participant ConnectedThread
    participant RemoteDevice

    User->>MainActivity: メッセージ入力
    MainActivity->>ConnectedThread: メッセージ送信要求
    ConnectedThread->>RemoteDevice: メッセージ送信
    RemoteDevice-->>ConnectedThread: 受信確認
    ConnectedThread->>MainActivity: 送信結果通知
    MainActivity->>User: メッセージ表示