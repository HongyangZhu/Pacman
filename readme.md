# Pacman



## 项目结构
```text
.______      ___       ______ .___  ___.      ___      .__   __.
|   _  \    /   \     /      ||   \/   |     /   \     |  \ |  |
|  |_)  |  /  ^  \   |  ,----'|  \  /  |    /  ^  \    |   \|  |
|   ___/  /  /_\  \  |  |     |  |\/|  |   /  /_\  \   |  . `  |
|  |     /  _____  \ |  `----.|  |  |  |  /  _____  \  |  |\   |
| _|    /__/     \__\ \______||__|  |__| /__/     \__\ |__| \__|
│  .gitignore
│  pom.xml
│  readme.md
├─src
│  ├─main
│  │  └─java
│  │      │  JavaPlayer.java
│  │      │
│  │      ├─constant
│  │      │      Constants.java--定数
│  │      │
│  │      ├─controller
│  │      │      StrategyController.java--行进路线控制器
│  │      │
│  │      ├─pojo
│  │      │      LocationInfo.java--位置bean
│  │      │      MapInfo.java--地图信息bean
│  │      │      Node.java--结点bean
│  │      │
│  │      └─utils
│  │              AStar.java--A星算法
│  │              CommentUtils.java--共通方法
│  │
│  └─test
│      └─java
│              JavaPlayerTest.java
```