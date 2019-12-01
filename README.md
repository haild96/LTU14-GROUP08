# LTU14-GROUP08
Đề tài: Chương trình chơi cờ vua giữa 2 người chơi với nhau sử dụng RMI.
<hr>
<h1>Đặc điểm của chương trình:</h1>
<ul>
  <li>Chương trình chơi cờ real-time giữa 2 người chơi với nhau</li> 
  <li>Chương trình phân tán</li> 
  <li>Sử dụng ngôn ngữ lập trình Java</li>
  <li>Sử dụng Docker</li>
  <li>Sử dụng RMI</li>
</ul>
<hr>
<h1>Hướng dẫn cài đặt và chạy chương trình:</h1><br>
<ul>
  <li>Cài đặt Docker trên Ubuntu 16.01</li> 
  <li>Chạy Server</li> 
  <li>Chạy client 1</li>
  <li>Chạy client 2</li>
</ul>
<h2>Bước 1: Cài đặt docker trên ubuntu 16.04 sử dụng các lệnh sau:</h2><br>
<ul> 
  <li>sudo apt-get update</li> 
  <li>sudo apt-get install apt-transport-https ca-certificates curl software-properties-common</li> 
  <li>curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -</li>
  <li>sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"</li>
  <li>sudo apt-get update</li>
  <li>apt-get install docker-ce</li>
</ul>
<br>
<h2>Bước 2: Chạy Server </h2><br>
 Mở Terminal và chạy các lệnh sau để lấy image về từ Docker hub và chạy container với name là server :<br>
<ul>
  <li>sudo docker pull haildbk/ubuntu-java:v1.3</li> 
  <li>sudo docker run  --name server --net=host  -ti --privileged -v $HOME:/root:ro -e DISPLAY=$DISPLAY  -d 132</li>
  <li>sudo docker attach server</li>
  <li>git clone https://github.com/haild96/LTU14-GROUP08.git</li>
  <li>cd LTU14-GROUP08</li>
  <li>javac Resurset/*.java</li>
  <li>java Resurset/startServer</li>
</ul>
<br>
<h2>Bước 3: Chạy Client1</h2><br>
 Mở Terminal và chạy các lệnh sau với container name là client1 :<br>
<ul>
  <li>sudo docker run  --name client1 --net=host  -ti --privileged -v $HOME:/root:ro -e DISPLAY=$DISPLAY  -d 132</li>
  <li>sudo docker attach client1</li>
  <li>git clone https://github.com/haild96/LTU14-GROUP08.git</li>
  <li>cd LTU14-GROUP08</li>
  <li>javac Resurset/*.java</li>
  <li>java Resurset/chess</li>
</ul>
<br>
<h2>Bước 4: Chạy Client2 </h2><br>
 Mở Terminal và chạy các lệnh sau với container name là client2 :<br>
<ul>
  <li>sudo docker run  --name client2 --net=host  -ti --privileged -v $HOME:/root:ro -e DISPLAY=$DISPLAY  -d 132</li>
  <li>sudo docker attach client2</li>
  <li>git clone https://github.com/haild96/LTU14-GROUP08.git</li>
  <li>cd LTU14-GROUP08</li>
  <li>javac Resurset/*.java</li>
  <li>java Resurset/chess</li>
</ul>
<br>

<hr>
<h1>Kết quả sau khi chạy chương trình</h1><br>
<h2>Server<h2>
  <img src="https://raw.githubusercontent.com/haild96/LTU14-GROUP08/master/Resurset/images/server.png">
<h2>Client 1 và Client 2</h2><br>
  <img src="https://raw.githubusercontent.com/haild96/LTU14-GROUP08/master/Resurset/images/clients.png">
