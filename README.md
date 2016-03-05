## 看山日报

### 应用介绍
> 看山日报是我在自学Android中写的第二APP，在写代码的过程中，通过查阅资料和解决问题，学习到了很多新的知识，应用中图片资源来自知乎日报的官方客户端，图标来自网络，程序所使用的Api均来自[izzyleung](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)，感谢izzyleung提供此Api，感谢萌萌的刘看山。


### 下载链接
[看山日报](http://fir.im/ksrb)


### 应用效果
![KanShanNews](https://github.com/vanhelsingyyy/KanShanNews/blob/master/KanShanNews.gif)

### 完成功能
+ 启动图片的展示
+ 每日新闻的展示
+ 主题日报的展示
+ 已读新闻标记
+ 自定义``ViewPager``进行今日新闻的轮播
+ 使用``RecyclerView``多Item布局实现条目展示
+ 通过``SwpieRefreshLayout``实现上拉数据的刷新
+ 通过``RecyclerView``实现下拉自动加载更多
+ 通过``WebView``展示新闻内容
+ 通过``CoordinatorLayout``实现内容界面的视差和快速显示隐藏效果
+ 通过``RxJava+Retrofit``获取服务器数据
+ 通过``Picasso``展示丶缓存图片
+ 设置``OkHttp``的缓存策略来实现数据的离线缓存

### 依赖的官方支持
+ ``com.android.support:appcompat-v7:23.1.1``
+ ``com.android.support:design:23.1.1``
+ ``com.android.support:recyclerview-v7:23.1.1``

### 依赖的第三方库
+ [RxJava](https://github.com/ReactiveX/RxJava)
+ [Retrofit](https://github.com/square/retrofit)
+ [ButterKnife](https://github.com/JakeWharton/butterknife)
+ [Picasso](https://github.com/square/picasso)