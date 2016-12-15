# Database

Name: TastySnake.db

Version: 1

Author: [@xuanqu](https://github.com/xuanqu)

Source: [DBHelper.java](../app/src/main/java/com/example/stevennl/tastysnake/util/DBHelper.java)

## Table

### 1. battle_record:

| Name | Type In Code | Type In DB | Comment |
|------|--------------|------------|---------|
|timestamp|java.util.Date|TEXT|对战结束时的时间戳 (年/月/日 + 时/分/秒)
|win|boolean|INTEGER|对战结果：赢/输|
|cause|Snake.MoveResult|INTEGER|对战结束的原因：OUT、SUICIDE或HIT_ENEMY|
|duration|int|INTEGER|对战的持续时间(秒)|
|myLength|int|INTEGER|对战结束时自身蛇长|
|enemyLength|int|INTEGER|对战结束时对方蛇长|

Source: [BattleRecord.java](../app/src/main/java/com/example/stevennl/tastysnake/model/BattleRecord.java)