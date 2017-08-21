# 前后端分离的MVC Servlet框架
## introduce
> see also [sshMVC](https://github.com/q294881866/sshMVC)
* The system into front end and back end.
* 前后端分离的Servlet框架，无需使用重量级框架。
## 计划列表
 * 采用nio实现网络通讯
   * 不依赖web服务器
   * jar包启动
   * 处理更加灵活
 * db层抽象
   * 关系数据库：druid
   * 其它
 * 序列化
   * json数据解析，只传输值
   * 前端nodejs，序列化与反序列化数据
   * 后端提供序列化与反序列化工具
