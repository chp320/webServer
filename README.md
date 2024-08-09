# 주의!
### File 입출력 클래스 처리
- FileInputStream, FileOutputStream 처리 시 파일 위치 제대로 지정하지 않으면 오류 발생!
- 입출력에 사용할 파일은 프로젝트 최상위 경로에 위치하여야 함
  - 이번의 경우 프로젝트 하위에 server_send.txt, client_send.txt 파일 생성하고 실행
```
    └── webServer
    ├── client_send.txt
    ├── server_send.txt
    ├── src
    │   └── lecture01
    │       ├── WebClient.java
    │       └── WebServer.java
    └── webServer.iml
```

### 실행 방법
1. 프로젝트 하위에 server_send.txt, client_send.txt 파일 생성
2. WebServer 실행
3. WebClient 실행


