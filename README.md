# TpaDemo
基于AfyBroker实现的跨服传送功能演示



假设你已经克隆了此项目，输入以下指令以构建项目。

```shell
gradlew build
```

将生成的 jar 包放入到 bukkit 服务器插件目录下，以及 broker-server 插件目录下。



指令与功能：

- /tpa [player] - 请求传送到对方所在位置
- /tpahere [player] - 请求对方传送到你的位置
- /tpaaccept [player] - 同意对方的传送请求
- /tpadeny [player] - 拒绝对方的传送请求

