# アーキテクチャ概要

## システム構成
本アプリケーションは以下の主要コンポーネントで構成されています：

1. **UI層**
   - MainActivity: ユーザーインターフェースを管理
   - activity_main.xml: 画面レイアウト定義

2. **Bluetooth通信層**
   - BluetoothAdapter: Bluetooth機能の管理
   - BluetoothSocket: デバイス間通信の確立
   - ConnectedThread: データ送受信処理

3. **データ層**
   - SharedPreferences: 設定情報の保存
   - メモリ内データ: チャット履歴の一時保存

## データフロー
1. ユーザーがメッセージを入力
2. MainActivityがメッセージを取得
3. ConnectedThreadがメッセージを送信
4. 相手デバイスがメッセージを受信
5. 受信したメッセージをUIに表示

## 依存関係
- Android SDK: 29以上
- AndroidXライブラリ
- Bluetooth関連API