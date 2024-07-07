# 岐大生時間割アプリ（現在閉鎖中）

Android Studioのプロジェクト。<br>
現在はサービス閉鎖中です。ポートフォリオの参考としてレポジトリ公開しています。<br>
※レポジトリ内に講義情報等は含まれていません。

## アプリ概要
岐阜大学生の時間割を一瞬で作成するAndroidアプリです。<br>
アプリから岐阜大学生ポータルサイトにログインすると、時間割表のHTMLを解析して取得します。<br>
※下のデモ画像では、ログイン画面を省略しています。

<img width="210" alt="時間割アプリデモ" src="https://github.com/oor30/Timetable/assets/66106684/eac89846-dd32-40c6-8596-bd14f3db5913">
<img width="194" alt="スクリーンショット 2024-07-08 0 13 39" src="https://github.com/oor30/Timetable/assets/66106684/a71fa09e-ae75-43f3-b9f9-fcabdd72e8b3">
<img width="194" alt="スクリーンショット 2024-07-08 0 54 16" src="https://github.com/oor30/Timetable/assets/66106684/87137d20-c068-43cd-a91b-1e767a784746">
<img width="194" alt="スクリーンショット 2024-07-08 0 16 18" src="https://github.com/oor30/Timetable/assets/66106684/85d0f167-f282-485f-ba62-8108df033266">
<img width="194" alt="スクリーンショット 2024-07-08 0 17 16" src="https://github.com/oor30/Timetable/assets/66106684/0507c279-6280-4d53-b6d3-eb9d9925c38d">


## なぜ作ったか

学生は時間割をアプリで管理することが多く、様々な時間割アプリが存在しますが、最初に必ず講義の情報を1つ1つ入力しなければなりません。<br>
講義のシラバスはWebサイトにすべて公開されているので、時間割とシラバスを結びつけることで簡単な時間割アプリが作れると考えました。<br>
また、シラバスには講義の区分や単位数なども掲載されているため、単位管理にも使えます。

## 使い方

1. アプリを起動（このとき時間割は真っ白の状態）
2. 「ポータルサイトから追加」を押す
3. 自分のアカウントでログイン
4. My時間割を表示して、ダウンロードボタンを押す
5. 時間割がアプリに反映されます

時間割に反映される講義情報は、講義名、教授、教室、シラバスURLです。

## 仕組み

講義情報は、スクレイピングで取得して、Firebaseのデータベース（Firestore）にアップロードしています。（現在閉鎖中）<br>
アプリでMy時間割をダウンロードすると、My時間割の講義IDをデータベースに照合して、アプリにデータを落とします。

## その他機能

- 時間割を表示
  - 教示、教室、シラバスURL
  - 成績比率のメモ（出席：課題：試験）
- 課題管理
  - 課題タイトル、期限、メモを記録
  - 共有機能

### 課題の共有

課題作成時に、公開設定にすると、データベースに課題が登録されます。<br>
データベースに登録された課題は、同じ講義を登録しているユーザーにも表示されるようになります。

<img width="601" alt="タスク共有デモ" src="https://github.com/oor30/Timetable/assets/66106684/a2b3d22b-09d8-44c0-8c9a-613bdbfbed37">
