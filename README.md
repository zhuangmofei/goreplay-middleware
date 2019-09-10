# goreplay-middleware
https://goreplay.org/  middleware in Java，help to analyze response

基于GoReplay(https://goreplay.org)的middleware而实现的，帮助我们在完成流量复制之后，对original response和replay response的对比
如果你也只是需要对response code进行对比，那么直接拿来就能用，但是如果你需要进行更复杂的对比，或者是要对其中的token进行mapping,权限校验
等复杂操作，则需要对当前版本进行自定义开发。

1.--output-http-track-response --input-raw-track-response 两个参数是帮助我们打印original response 和 replay response的，可以通过查看response的格式，帮助我们进一步的开发程序

2.--output-file 可以先将request,original response和replay response输出到文件中

3.上传jar包到宿主服务器上，然后创建一个sh文件，用于执行jar包，或者也可以通过java -cp命令

4.如果创建sh文件：
  #!/usr/bin/env bash
  java -jar yourownprojectname.jar
  
5.通过gor --input-raw :80 --middleware "./syrup.sh" --output-http-track-response --input-raw-track-response --output-http "http://target ip:80" 来完成流量的复制

6.在当前目录先就能看到match.log 和 unmatch.log 两个文件，其中记录了复制的请求和原请求的对比结果

*如果本项目能给你带来些许的灵感和帮助，请给一个鼓励的Star吧 ！ Thanks !
