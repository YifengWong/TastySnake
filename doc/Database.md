# Database Requirements

Database name: TastySnake.db

Table name: battle_record

## Source File

* [DBTestActivity.java](../app/src/main/java/com/example/stevennl/tastysnake/controller/test/DBTestActivity.java)：用于测试数据库功能。

* [BattleRecord.java](../app/src/main/java/com/example/stevennl/tastysnake/model/BattleRecord.java)：对应数据库表中的每一项。

* [DBHelper.java](../app/src/main/java/com/example/stevennl/tastysnake/util/DBHelper.java)：数据库交互，继承SQLiteOpenHelper实现。此类要写成单例(Singleton)，参考[SensorController.java](../app/src/main/java/com/example/stevennl/tastysnake/util/sensor/SensorController.java)和[BluetoothManager.java](../app/src/main/java/com/example/stevennl/tastysnake/util/bluetooth/BluetoothManager.java)。

## Attribute

|命名|类型|说明|
|---|---|---|
|timestamp|Java的时间类型|对战结束时的时间戳 (年/月/日 + 时/分/秒)
|win|boolean|对战结果：赢/输|
|cause|Snake.Type|对战结束的原因：OUT、SUICIDE或HIT_ENEMY|
|time|int|对战的持续时间(秒)|
|myLength|int|对战结束时自身蛇长|
|enemyLength|int|对战结束时对方蛇长|

[BattleRecord.java](../app/src/main/java/com/example/stevennl/tastysnake/model/BattleRecord.java)中的属性需按照上述命名和类型创建，数据库表中的字段名需按照上述命名创建，数据类型根据实际情况调整。

## Goal

在数据量足够的情况下，可以利用数据库中的信息进行玩家策略分析。