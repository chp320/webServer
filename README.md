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


### nginx 설명
1. web서버로 nginx를 사용 (맥북의 경우, 설치는 ``brew install nginx`` 로 진행)
2. nginx에서 최상위 폴더인 ``도큐먼트 루트`` 는 설정파일 ``/usr/local/etc/nginx/nginx.conf``의 location 부분의 root 영역이며
   맥의 경우 ``/usr/local/var/www`` 가 root 가 된다.
3. 만일 ``도큐먼트 루트`` 수정을 할 경우, nginx.conf 의 root 뒷 부분에 변경하고자 하는 경로를 입력하면 된다.

#### nginx 의 파일 종류 구분 (Content-Type)
1. 파일의 종류는 확장자로 구분하며
2. HTTP response 헤더의 ``Content-Type`` 에 파일 종류를 리턴
3. nginx 의 Content-Type 목록은 ``/usr/local/etc/nginx/mime.types`` 에 정의

#### nginx 의 access log
1. html 파일 혹은 이미지에 대한 request 인입 시, nginx 는 access log 에 기록을 남김
2. nginx 의 access log 기본 경로는 ``/usr/local/var/log/nginx`` 이며, access.log 파일에 기록됨
```
예시) 
127.0.0.1 - - [09/Aug/2024:23:54:59 +0900] "GET /google-logo.png HTTP/1.1" 200 80440 "-" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"
IP 주소 / client 아이덴티티 / (인증 사용 시) user id / 처리 시각 / request line / status code / 송신된 바이트수 / referer (어떤 페이지를 클릭 후 이동했는지 이동 전의 페이지 url 정보) / user-agent
-> client 아이덴티티, user id 는 미사용하기 때문에 "-" 로 출력됨
-> 송신된 바이트수: 헤더를 제외한 바디 영역의 데이터 사이즈 의미
```

## 웹 서버 만들기
- client_send.txt 에는 브라우저에서 서버로 request 했던 내용을 저장하였고, 그 응답 값으로 client_recv.txt 에 기록을 했다.
- 즉, 서버는 http response 를 크게 status line, header 와 body 데이터를 전송하며 header 는 공백 라인을 통해 header 의 끝을 표기하
- 위 내용을 토대로 WebServer.java 에서 http response 를 보내도록 하여 브라우저로 하여금 nginx 웹서버로 인식하게 한다
```
1) status line -> "HTTP/1.1 200 OK" 리턴하도록 하드코딩
2) http response header
3) 공백 라인 (헤더 구분) -> 하드코딩
4) http request line 에 요청했던 파일 내용
```
### response header 정보
- HTTP response header 정보는 RFC 에 규정한 '필수 여부(요구 레벨)' 에 따라 작성
  - MUST, REQUIRED, SHOULD, MAY
  - (필수) MUST, REQUIRED
  - (선택) SHOULD, MAY
```
< response header 예시 >
Server: nginx/1.27.0                    -> 서버 명칭. 필수는 아니지만(MAY) 반환.
Date: Fri, 09 Aug 2024 15:22:34 GMT     -> 필수는 아니지만(SHOULD) 반환.
Content-Type: text/html
Content-Length: 615                     -> response body의 데이터 사이즈. 무시. 
Last-Modified: Wed, 29 May 2024 14:30:32 GMT
Connection: keep-alive
ETag: "66573c08-267"
Accept-Ranges: bytes
```
=> 상기 내용 중, ``Date``, ``Server``, ``Connection``, ``Content-Type`` 을 response header 에 반환
