# クラス詳細説明

## MainActivity

### 概要
アプリケーションのメイン画面を管理するクラスです。Bluetooth接続の確立、メッセージの送受信、UIの更新を行います。

### 主要メソッド
- `onCreate()`: 初期設定
- `setupBluetooth()`: Bluetoothの初期化
- `connectToDevice()`: デバイスへの接続
- `sendMessage()`: メッセージ送信
- `appendToChat()`: チャット履歴更新

### プロパティ
- `bluetoothAdapter`: Bluetoothアダプタ
- `bluetoothSocket`: Bluetoothソケット
- `connectedThread`: 通信スレッド
- `handler`: UI更新用ハンドラ

## ConnectedThread

### 概要
Bluetooth接続を管理し、データの送受信を行う内部クラスです。

### 主要メソッド
- `run()`: データ受信処理
- `write()`: データ送信処理

### プロパティ
- `mmInStream`: 入力ストリーム
- `mmOutStream`: 出力ストリーム