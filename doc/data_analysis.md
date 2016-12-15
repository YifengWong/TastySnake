# Data Analysis

Use [database](./database.md) data to analyze user's behaviour.

Author: [@stevennL](https://github.com/stevennL) [@xuanqu](https://github.com/xuanqu)

Source: [AnalysisData.java](../app/src/main/java/com/example/stevennl/tastysnake/model/AnalysisData.java)

## Local

Data analysis in local device.

### Description

* 您到目前为止一共进行了N局游戏。

* 赢X局，其中智商碾压A局，侥幸获胜B局。

* 输Y局，其中被对方戏耍C局，因失误失败D局。

* 每一局的平均时长为T秒。

* 每一局你的蛇的平均长度为L1节。

* 每一局对方的蛇的平均长度为L2节。

* 您的能力指数为W。

* 您的技术评估为P。

### Definition

智商碾压：win=true && cause=HIT_ENEMY

侥幸获胜：win=true && (cause=OUT || cause=SUICIDE)

被对方戏耍：win=false && cause=HIT_ENEMY

因失误失败：win=false && (cause=OUT || cause=SUICIDE)

W = (100/N)\*((7\*A+5\*B)\*(18-log2(T+1))+(1\*C+3\*D)\*log2(T+2))

| P | Range |
|:-:|:-----:|
|王者|W >= 8500|
|大师|6100 <= W < 8500|
|黄金|3800 <= W < 6100|
|白银|1500 <= W < 3800|
|青铜|W < 1500|

Use [formula_test.m](./formula_test.m)(MATLAB) to test W and P.