# BeaconTalk アプリケーション

## 概要
このアプリケーションは、Bluetoothを使用して2台のAndroidデバイス間でテキストメッセージを送受信するBeaconTalkアプリです。

## 主な機能
- Bluetoothデバイスの検出とペアリング
- テキストメッセージの送受信
- チャット履歴の表示

## 必要なパーミッション
アプリケーションの動作には以下のパーミッションが必要です：
- BLUETOOTH
- BLUETOOTH_ADMIN  
- BLUETOOTH_CONNECT
- BLUETOOTH_ADVERTISE
- BLUETOOTH_SCAN
- ACCESS_FINE_LOCATION
- ACCESS_COARSE_LOCATION

## 主要なクラス
### MainActivity
アプリケーションのメイン画面を管理するクラスです。以下の機能を提供します：
- Bluetoothの初期化と接続
- デバイスの検出と選択
- メッセージの送受信
- チャット履歴の表示

### ConnectedThread
Bluetooth接続を管理し、データの送受信を行う内部クラスです。

## ビルドと実行
1. プロジェクトをクローンまたはダウンロード
2. Android Studioでプロジェクトを開く
3. 必要な依存関係をインストール
4. 実機またはエミュレータでアプリを実行

## 使用方法
1. アプリを起動
2. Bluetoothが有効でない場合は有効化
3. ペアリング済みデバイスから接続先を選択
4. メッセージを入力して送信

## 注意事項
- Android 6.0以上で動作確認
- 位置情報パーミッションが必要
- 実機でのテストを推奨