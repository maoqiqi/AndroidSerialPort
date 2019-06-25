# AndroidSerialPort

[ ![SERIAL_PORT](https://api.bintray.com/packages/maoqiqi/AndroidSerialPort/serial_port/images/download.svg) ](https://bintray.com/maoqiqi/AndroidSerialPort/serial_port/_latestVersion)
[ ![SERIAL_PORT_EXTENSION](https://api.bintray.com/packages/maoqiqi/AndroidSerialPort/serial_port_extension/images/download.svg) ](https://bintray.com/maoqiqi/AndroidSerialPort/serial_port_extension/_latestVersion)
[ ![API](https://img.shields.io/badge/API-14%2B-blue.svg) ](https://developer.android.com/about/versions/android-4.0.html)
[ ![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg) ](http://www.apache.org/licenses/LICENSE-2.0)
[ ![Author](https://img.shields.io/badge/Author-March-orange.svg) ](fengqi.mao.march@gmail.com)

Fork自Google开源的Android串口通信Demo，修改成Android Studio下的串口通信实例项目，已通过测试。


## 目录

* [Download](#Download)
* [Usage](#Usage)
* [Screenshot](#Screenshot)
* [About](#About)
* [License](#License)


## Download

下载选择：

* `serial_port`：对Google开源的Android串口通信Demo的封装。

  Download [the JARs](https://jcenter.bintray.com/com/codearms/maoqiqi/serial_port) or configure this dependency:

  ```
  implementation 'com.codearms.maoqiqi:serial_port:1.0.1'
  ```

* `serial_port_extension`：对serial_port的一个拓展，多了可以设置串口数据位、停止位、校验位三个参数。

  Download [the JARs](https://jcenter.bintray.com/com/codearms/maoqiqi/serial_port_extension) or configure this dependency:

  ```
  implementation 'com.codearms.maoqiqi:serial_port_extension:1.0.1'
  ```


## Usage

* 打开串口：`open(String path, int baudRate, int dataBits, int stopBits, int parity, int flags)`。
* 发送数据。数据的发送很简单，通过SerialPort对象的getOutputStream()方法获取到输出流，然后把数据写入到这个流中就行了。

  ```
  outputStream = serialPort.getOutputStream();
  outputStream.write(StringUtils.str2HexStr(data).getBytes());
  ```

* 接受数据。数据的接收，通过SerialPort对象的getInputStream()方法获取到输入流，然后读流的就行了。不过一般会开一个线程去读数据。

  ```
  public class SerialReadThread extends Thread {

      private static final String TAG = SerialReadThread.class.getSimpleName();

      private BufferedInputStream bufferedInputStream;

      public SerialReadThread(InputStream inputStream) {
          bufferedInputStream = new BufferedInputStream(inputStream);
      }

      @Override
      public void run() {
          byte[] buffer = new byte[1024];
          int size;

          LogUtils.d(TAG, "-------开始读取串口数据-------");

          while (!isInterrupted()) {
              try {
                  int available = bufferedInputStream.available();
                  if (available > 0) {
                      size = bufferedInputStream.read(buffer);
                      if (size > 0) {
                          onReceive(buffer, size);
                      }
                  } else {
                      // 暂停一点时间,免得一直循环造成CPU占用率过高
                      SystemClock.sleep(1);
                  }
              } catch (Exception e) {
                  e.printStackTrace();
                  LogUtils.d(TAG, "-------读取串口数据失败-------");
                  break;
              }
          }

          LogUtils.d(TAG, "-------结束读取串口数据-------");
      }

      // 处理获取到的数据
      private void onReceive(final byte[] buffer, final int size) {
          LogUtils.d(TAG, "onReceive:" + new String(buffer, 0, size));
      }

      // 关闭读取串口数据线程
      public void close() {
          try {
              bufferedInputStream.close();
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
              interrupt();
          }
      }
  }
  ```

  ```
  readThread = new SerialReadThread(serialPort.getInputStream());
  readThread.start();
  ```

* 关闭串口：`public void close()`。

串口详细介绍请参考[Android 串口编程](SERIAL_PORT.md)。


## Screenshot

<img src="/screenshot/Screenshot_1.png" width="280px" />
<img src="/screenshot/Screenshot_2.png" width="280px" />


## About

* **作者**：March
* **邮箱**：fengqi.mao.march@gmail.com
* **头条**：https://toutiao.io/u/425956/subjects
* **简书**：https://www.jianshu.com/u/02f2491c607d
* **掘金**：https://juejin.im/user/5b484473e51d45199940e2ae
* **CSDN**：http://blog.csdn.net/u011810138
* **SegmentFault**：https://segmentfault.com/u/maoqiqi
* **StackOverFlow**：https://stackoverflow.com/users/8223522


## License

```
   Copyright 2019 maoqiqi

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```