[TOC]

# UtilsDemo

个人建立的一个library项目，内部包含有各种Util方法，以及自定义的View，比如心电图View，ScanView等，通过输出为aar，文件，达到别人可以快速的导入的效果，将资源文件与对应的自定义View的文件封装。

# 说明

主Module是一个demo，用于示范使用。

欢迎大家来给我建议哦！如果这个项目帮到了你，请给我一个star哦！O(∩_∩)O谢谢！

Connect me by : im.noclay@gmail.com

# 作者

NoClay

# 使用

使用方式一般如下（部分使用方式可能不同，会在单独的文件中详细说明）：

1. 点击对应view或工具底下的下载地址，下载aar文件

![step1](http://storage1.imgchr.com/EXzzn.png)

2. 以Android Studio为例，点击File > new > new Module > import jar/ aar package

   ![step2](http://storage1.imgchr.com/EjpMq.png)

3. 像自己写的一样，使用即可，查看使用说明可点击使用说明：

   ![step3](http://storage1.imgchr.com/EXzzn.png)

# SDK列表

## UtilsTool

用于保存一些公用方法

## View

用于保存自定义view

### ECGWaveView

这个view是一个心电图的自定义View，可以加载心电图数据，显示对应的波形。

使用说明：[查看使用说明](DownloadAAR/ECGWaveView/ECGWaveView.md)

下载地址：[点击此处下载](DownloadAAR/ECGWaveView/ecgwaveview-release.aar)

使用效果：![效果图](http://storage1.imgchr.com/EXxRs.gif)



### FoldLineView

这个view是一个折线图的view，可以添加多条折线，只需要实现对应的数据接口即可。

使用说明：[查看使用说明](DownloadAAR/FoldLineView/FoldLineView.md)

下载地址：[点击此处下载](DownloadAAR/FoldLineView/foldlineview-release.aar)

使用效果：![效果图](http://storage1.imgchr.com/Vuf2R.gif)

## Bluetooth

这个SDK，封装了蓝牙通信相关的模块，使用了装饰者，单例，策略等设计模式，进行了抽象，蓝牙操作均通过Bluetooth进行，在UtilsDemo中利用SDK，实现了一个蓝牙聊天的功能，个人使用中，可以自由拓展。

使用说明：[查看使用说明](DownloadAAR/Bluetooth/Bluetooth.md)

下载地址：[点击此处下载](DownloadAAR/Bluetooth/bluetooth-release.aar)

使用效果：

![效果](http://storage1.imgchr.com/VgJ1J.png)