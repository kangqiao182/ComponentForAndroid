


### 测试
账号: qwer123
密码: qwer123

### SwipeBack 出现黑屏现象
[windowIsTranslucent和windowBackground对比](https://www.jianshu.com/p/1398ca679ae4)
小结：
1. android:windowBackground不影响Activity的生命周期,即启动设置了该属性的Activity的生命周期不会受到影响。
2. android:windowIsTranslucent = true会影响到Activity的生命周期，比如启动一个设置了该属性的Activity，若该activity对用户可见时，启动它的activity（即它下层的activity）并未完全不可见（即调用到了onPause，不会调用onStop方法），具体可见上面的操作对Activity生命周期的影响。
