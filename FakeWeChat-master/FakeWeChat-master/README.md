## [FakeWeChat 仿微信](http://git.oschina.net/csumissu/FakeWeChat) ##
**界面高仿微信，数据来自[新浪微博API](http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI)。**
**仅供大家学习交流，不得用于商业用途。**


### 项目简述 ###
项目采用[MVP-Rxjava](https://github.com/googlesamples/android-architecture/tree/dev-todo-mvp-rxjava/)设计模式，完全Material Design风格，实现了微信的部分界面功能。[APK下载](http://git.oschina.net/csumissu/FakeWeChat/attach_files)  

![主界面](http://git.oschina.net/uploads/images/2016/0706/184449_0deed53d_17571.gif "主界面")

### 欢迎界面 ###
仿网易新闻客户端，在右上角添加了圆环倒计时的“跳过”功能，用于广告展示。  
  
![欢迎界面](http://git.oschina.net/uploads/images/2016/0706/184421_9530119e_17571.png "欢迎界面")

### 主界面 ###
#### “微信”标签页 ####
因为[新浪微博API](http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI)没有返回私信的接口，所以直接展示聊天的界面，采用[图灵聊天机器人](http://www.tuling123.com/)进行会话。  

![聊天界面](http://git.oschina.net/uploads/images/2016/0706/184501_1b4355b2_17571.png "聊天界面")

#### “通讯录”标签页 ####
根据姓名拼音进行分组排序，并实现了字母导航功能。另外ActionBar上点击“+”出现的菜单，能正常显示在ActionBar之下，是仿照系统中的ShareActionProvider，重写onCreateActionView()方法实现的。其实通过自定义ActionBar的布局能实现相同的界面效果，可能还更简单些。  
  
![通讯录界面](http://git.oschina.net/uploads/images/2016/0706/184515_0fc89b3c_17571.gif "通讯录界面")

#### “发现”和“我”标签页 ####
这两个标签页的数据简单、重复，故将布局类型分为了三类：[间隔](http://git.oschina.net/csumissu/FakeWeChat/blob/master/app/src/main/java/csumissu/fakewechat/common/TileGapDelegate.java)、[菜单](http://git.oschina.net/csumissu/FakeWeChat/blob/master/app/src/main/java/csumissu/fakewechat/common/TileMenuDelegate.java)、[个人信息](http://git.oschina.net/csumissu/FakeWeChat/blob/master/app/src/main/java/csumissu/fakewechat/common/TileInfoDelegate.java)，在RecyclerView.Adapter中采用聚合模式方便扩展。  

![发现界面](http://git.oschina.net/uploads/images/2016/0706/184528_af2972d0_17571.png "发现界面") 
![我的界面](http://git.oschina.net/uploads/images/2016/0706/184539_02bcda1c_17571.png "我的界面")

### 朋友圈 ###
* 头部使用AppBarLayout加CollapsingToolbarLayout实现可伸缩效果。
* 使用SwipeRefreshLayout进行下拉刷新，上滑更多通过RecyclerView的addOnScrollListener实现。
* 微博中的文字中关键字高亮、表情替换，参考[高仿新浪微博文字处理](http://www.cnblogs.com/lichenwei/p/4676214.html)。
* 图片查看使用的开源控件[PhotoView](https://github.com/chrisbanes/PhotoView)。

![朋友圈](http://git.oschina.net/uploads/images/2016/0706/184553_c30b50a6_17571.gif "朋友圈")

### 技术交流 ###
邮箱：csumissu@icloud.com

### 参考项目 ###
* [android-architecture]( https://github.com/googlesamples/android-architecture)
* [开源中国客户端](http://git.oschina.net/oschina/android-ap)

### 依赖包 ###
* [ButterKnife](https://github.com/JakeWharton/butterknife)    Bind Android views and callbacks to fields and methods.
* [RetroLambda](https://github.com/evant/gradle-retrolambda)    A gradle plugin for getting java lambda support in java 6, 7 and android
* [RxJava](https://github.com/ReactiveX/RxJava)    Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)    RxJava bindings for Android
* [RxLifecycle](https://github.com/trello/RxLifecycle)    Lifecycle handling APIs for Android apps using RxJava
* [Glide](https://github.com/bumptech/glide)    An image loading and caching library for Android focused on smooth scrolling
* [Retrofit](https://github.com/square/retrofit)    Type-safe HTTP client for Android and Java by Square, Inc.
* [OkHttp](https://github.com/square/okhttp)    An HTTP+HTTP/2 client for Android and Java applications.
* [Gson](https://github.com/google/gson)    A Java serialization/deserialization library that can convert Java Objects into JSON and back.
* [PhotoView](https://github.com/chrisbanes/PhotoView)    Implementation of ImageView for Android that supports zooming, by various touch gestures.
* [RecyclerView-FlexibleDivider](https://github.com/yqritc/RecyclerView-FlexibleDivider)    Android library providing simple way to control divider items (ItemDecoration) of RecyclerView
* [BottomNavigationBar](https://github.com/Ashok-Varma/BottomNavigation)    This Library helps users to use Bottom Navigation Bar (A new pattern from google) with ease and allows ton of customizations
* [CircleIndicator](https://github.com/ongakuer/CircleIndicator)    A lightweight viewpager indicator like in nexus 5 launcher
* [ProgressWheel](https://github.com/Todd-Davies/ProgressWheel)    A progress wheel for android, intended for use instead of the standard progress bar.
* [JPinyin](https://github.com/stuxuhai/jpinyin)    JPinyin是一个汉字转拼音的Java开源类库