# 优眼仪软件变更说明

记录优眼仪的软件变更情况

从v1.1.0开始记录

# v1.1.0

完成新的训练流程，发布新版

# v1.1.1

## 20160626

### 优眼仪训练变更到翻转拍新的流程，之前打包的时候打包错了

## 20160627

### 中间提交一次数据失败，就不一直提交了，改成最后完成时提交，重点更改

实现方案：

优眼顾问：

* 接收到项目开始指令，流程（提交更新接待表信息，成功以后开始训练），否则弹框说网络失败，让用户点击继续，再继续流程
* 训练结束，流程（提交结果，更新接待表信息，发送emqtt结束消息），否则弹框说网络错误，让用户点击继续，在继续流程
（如果中间提交数据成功，更新失败，解决办法记录一个流程的步骤，然后指更新接待信息）

优眼自助训练：

* 训练完成更新本地的接待信息，然后提交训练数据，更新接待信息，如果提交失败，结果保存数据库，最后结束的时候一次全部提交
* 只有这种模式，需要保存接待信息，并随时数据库更新，训练数据也是一样
* 取服务器上的接待信息，以本地为准，如果本地的已经结束，则更新网上的也结束
* 中间断网提交数据失败，则每次联网都只需要提交训练数据就行，接待的信息，一步一步往下更新就可以了

微信扫码训练：

* 微信扫码体验的时候，没有这个问题，本来就是最后一次提交

数据库更改的

* 新建reception的数据表，feedback的数据表，训练结果的表

# v1.1.2

## 20160704-温鑫林

### 优化：如果两个都是使用相同器械的（比如都是立体镜训练），不提示拿下板，在提示拿上去

分为两种

* 微信体验版-新的只体验立体镜散开和红绿可变集合了，就用不上了
* 自助训练版，根据上个处方和下个处方，生成单项全部结束的提示，和新的处方的准备的提示

## 20160701-荆为成
* 训练介绍主页:对机器按钮进行展示和解释其作用
* 二维码扫描页面：增加具体提示信息，如:请使用优眼App或微信扫描二维码
* 训练介绍主页面：增加提示信息，如请按下机器按钮进行下一步，直到开始训练。
* 翻转拍训练：在翻转拍介绍页面，提示请在看清屏幕中的文字或字母时按下机器按钮
* 立体经训练:将Z形分隔板的图片、以及正确放上去的图片进行详细的提示。
* 红绿可变:在训练介绍页面进行提示,将正方形、X、圆形合在一条线的图片当做提示进行展示;
* 裂隙尺：训练介绍页提示,将 单孔挡板/双控挡板 图片，和放置的位置图片，以及正确放置的图片进行详细的提示
* 红绿固定:将合成的正确图片放置中间进行提示。然后开始训练。

## 20160704-荆为成
* 修改训练提示的语音
* 修改翻转拍计算得分的算法

## 20160705-荆为成
* 5分钟检查体验版训练集合检查项目进行删减，只留下：左眼反转拍、右眼反转拍、立体镜散开、红绿可变集合、双眼反转拍这五项分别代表集合、散开、反转拍训练
* 红绿可变多一次错误机会

## 20160706-温鑫林
* 裂隙尺调整一步到位，现在的逻辑先调整到第一关，再根据实际情况，调整到真实的关数
* 裂隙尺调整设备的过程中，显示调整的界面
* uyu初始的二维码更换
* 大按钮的图片旋转更换

# v1.1.3

## 20160707-温鑫林
* 文章显示模式，3种，0：普通  1：词语（以空格分开）  2：诗词（以符号逗号和句号分开）
* 文字根据实际显示宽度居中
* ShowContent抽象出来进行显示
* 将List<ShowContent>替代原来的候选的内容

## 20160706-荆为成
* 训练设备两个立体镜时不出现拿下Z形分隔板的调试
* 修改裂隙尺的图片
* 编写训练完成时候的结果显示页面和语音提示

## 20160707-荆为成
* 解决结果页面语音播报出现的问题

## 20160708-荆为成
* 优眼自助、优眼顾问、微信自助模式下
* 立体镜训练中，把不是一个时请按键去掉，只留下，“可以看成稳定的一副图像请按键”，另外立体镜第一关等待时长从15秒变成40秒，其它关不变，每次训练长度从16关变成20关

## 20160708-温鑫林
* 优眼顾问控制训练，右眼等级不对
* 训练完左眼，右眼从负片开始的问题，需要重置

# v1.1.4

## 20160709-温鑫林
* 融合训练立体镜、裂隙尺、红绿固定融合上以后第二次按键无效

# v1.1.5

## 20160709-温鑫林
* 裂隙尺训练的时候不移动，怀疑处方给的等级不对，给的是0级，最小应该是1级（需要查看到底哪处方传错了，需要跟踪）
* deviceadjust调整的是当级的了，不会第一关的时候调整一个而距离，失败再回到第一关的时候就不会移动了
* 第一次第一关合上时间改成40s，3s太短不对

# v1.1.6

## 20160712-温鑫林
* 翻转拍时间去抖，加上设备如果没有转好，不接受按键指令，提示设备未调整好
* 每做完一项融像的训练，均将镜片归零，不然做完立体镜后若不归零，由于棱镜比较大，导致看下一项的使用说明都是重影的 (所有项目做完镜片归零，不移动位置还是比较快的)
* 翻转拍训练在内容选择的时候选择随机模式，一篇文章结束后再按键就没反应了，重发消息会闪退（优眼顾问控制的应该不显示循环模式，要不暂时的逻辑会有问题，先隐藏起来了，采用原来的单个循环模式）

## 20160712-荆为成
* 在初始界面，按确定键后出现UYU的大二维码，再按键也没有反应，退出软件后，重新进去，再按键出来了扫码训练的二维码

## 20160713-温鑫林
* 刘亚平优眼扫码训练翻转拍闪退，张凤仙优眼扫码翻转拍正常（发现镜片等级为第一级0，再往下减报错了）
* api的异常：http://app.uyu.com/device/api/v1/Category?ParentID=1&tk=75d40a034c4c5123764217c344f63bfa，发现selectcontentView初始化的时候就请求数据
* 网络掉线，快速显示网络异常的问题
    * 失败以后limit10次的异常，不死掉，显示弹出dialog可以继续重试
    * 使用黄峰手机做热点，wifi连接上，如果断开网络，会出现长时间提交，最后报无法连接上服务器
* 翻转拍的所有timer开始前，都先结束timer，最后结束的时候结束所有timer--应该可以解决练其他项目的时候显示翻转拍的提示
* 优眼顾问训练控制训练翻转拍的时候，改错了，隐藏了循环模式，但是循环模式设置成single了，应该是sequence，要不英文字母的时候，就一直不变了

## 20160714-温鑫林
* 翻转拍扫码自助训练的时候，记住上次训练的内容
    * 采用方式SharedPreference记录上次处方，最后的训练的内容和循环方式
    * 开始进入新的训练的时候，判断一下，是不是同一个人的处方，然后选择是不是用上次的处方的内容

## 20160715-温鑫林
* 翻转拍调整字体大小的逻辑有点问题，应该是跟着显示的一块变
* 翻转拍字体等级显示的调整，没有0级，1级到16级

# v1.1.7

## 20160719-温鑫林
* 接收优眼顾问发送的控制设置电机的指令，还是放在小项目中去执行，应为不同项目计算的距离不一样
* 根据isCanHandleControlsetMessage，保证只有立体镜可以接受处理控制设置指令
* 立体镜按等级计算距离，0级开始，每加一级往后屏幕距离往后调整10mm

## 20160720-温鑫林
* 设备端接收服务器推送的训练内容变更的情况
    * 先定义接收的服务器的内容的类
    * 接收到指令后，先去同步用户当前的图书
    * 按照发送的内容，显示发送的训练内容

## 20160717-荆为成
* 编写训练准备的自定义View
* 修改设备联网页面的布局
* 修改主页的布局
* 修改二维码扫描页面的布局
* 修改title的布局

## 20160718-荆为成
* 编写调试准备页面的左侧导航
* 编写准备页面的序号的添加

## 20160719-荆为成
* 修改底部小圆圈出现的bug
* 修改title的图标
* 修改训练结束页面的布局
* 修改结果展示页面的布局
* 在翻转拍、立体镜、红绿可变训练准备页面添加时间提示
* 编写Title中下载优眼APP的布局提示
* 设置中间Title不显示

## 20160720-荆为成
* 更换准备页的图片
* 给title加上所用时间提示

## 20160721-温鑫林
* 使用说明点击显示使用说明
    * 使用说明点击，暂停现在的训练项目
    * 点击关闭，恢复现在的训练项目
    * 只有在训练界面才显示使用说明的按钮

## 20160722-温鑫林
* 翻转拍左眼、右眼训练的时候，不提示项目结束的界面，同时跳过准备界面
* 解决：训练中弹出使用说明里面圆圈位置不居中的问题，调整高度跟外面的一致

# v1.1.8

## 20160727-温鑫林
* 修复bug：翻转拍的时候总提示设备未准备好，多发送了一次按键
* 视光师控制的时候，用户也可以发送训练内容

## 20160729-荆为成
* 微调一些显示的界面

# v.1.1.9

## 20160804-荆为成
* 优眼扫描时候的左边导航
* 修改裂隙尺的图片位置
* 修改红绿固定和红绿可变的倒计时的字体颜色为红色

## 20160805-温鑫林
* 解决结束后，刷新内容报错的问题，设备在调整的时候结束，没有内容了
* 视光师控制的时候，用户也可以发送训练内容

## 20160810-荆为成
* 解决优眼训练中途退出之后再次进入出现的异常
* 给红绿阅读训练添加等级提示信息

## 20160812-荆为成
* 优眼顾问控制训练的时候隐藏使用说明
* 修改所有网络超时提示框的地方的图标

## 20160813-荆为成
* 解决红绿可变等待时间长的问题

## 20160815-荆为成
* 编写训练设备根据推送消息自动更新软件功能
* 将静默安装功能添加到训练设备中

## 20160818-荆为成
* 编写训练设备屏幕高亮不变暗的功能
* 解决裂隙训练的时候倒计时出现0的问题，使用另外一种写法计时

# v1.1.10

## 20160822-荆为成
* 解决修改处方再进行训练出现的问题

## 20160824-荆为成
* 解决训练过程中推送升级消息的问题

## 20160827-温鑫林
* 使用新的推进的流程

## 20160829-温鑫林
* 防止命令解析错误，每隔500毫秒重发消息

## 20160901-温鑫林
* 根据电量判断是否充电，小于30开启充电，到达100停止充电
* 默认开启软件的时候开始充电

## 20160902-温鑫林
* 按键那一刻就变更状态，同时只处理一个按键，防止中间卡顿的时候报错

## 20160906-温鑫林
* 设置连接蓝牙成功后，将蓝牙信息传送至服务器
* 以后连接蓝牙成功后，也将蓝牙信息发送至服务器
* 同时获取cpuid，传送至服务器

# v2.0.0

## 20160918-温鑫林
* 自动重连蓝牙的功能，20s重连一次