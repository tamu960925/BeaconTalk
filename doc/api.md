# API仕様

## BluetoothAdapter
### メソッド
- `getDefaultAdapter()`: デフォルトアダプタ取得
- `getBondedDevices()`: ペアリング済みデバイス取得
- `startDiscovery()`: デバイス検索開始
- `isEnabled()`: Bluetooth有効状態確認

## BluetoothDevice
### メソッド
- `createRfcommSocketToServiceRecord(UUID)`: RFCOMMソケット作成
- `getName()`: デバイス名取得
- `getAddress()`: デバイスアドレス取得

## BluetoothSocket
### メソッド
- `connect()`: デバイス接続
- `getInputStream()`: 入力ストリーム取得
- `getOutputStream()`: 出力ストリーム取得
- `close()`: ソケットクローズ

## 使用するUUID
- `00001101-0000-1000-8000-00805F9B34FB`: SPP（シリアルポートプロファイル）