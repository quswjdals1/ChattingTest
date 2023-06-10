<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<style type="text/css">
	#chatroom{
		width:500px;
		height: 500px;
		border: 1px solid black; 
		overflow: scroll;
	}
	img{
		width: 200px;
		height: 200px;
	}
</style>
</head>
<body>
<div id="chatroom"></div>
방 입장: <input type="text" id="roomId"> <br>
이름: <input type="text" id="name"><button type="button" id="roomBtn">입장</button><br>
채팅: <input type="text" id="message"> <button type="button" id="sendBtn">전송</button><br>
파일: <input type="file" id="file" name="file"><button type="button" id="fileBtn">전송</button>
</body>
<script type="text/javascript">
var selectedFile="";
$(()=>{
	var roomBtn=$("#roomBtn")
	var sendBtn = $("#sendBtn")
	var fileBtn = $("#fileBtn");
	
	roomBtn.on("click",function(){
		wsOpen()
	})
	sendBtn.on("click",function(){
		send()
	})
	fileBtn.on("click",function(){
		fileSend()
	})
	$('#file').on('change', fileSelect);
})

function wsOpen(){	
	ws = new WebSocket("ws://"+location.host+"/chattting");
	wsEvt();
}
function wsEvt(){
	ws.onopen=function(data){
		sendState("login")
	}
	ws.onmessage = function(data){
		var msg = JSON.parse(data.data);
		if(msg !=null&&msg!=""){
			var text="";
			if(msg.type=="l"){
				if(msg.state=="login"){
					text=msg.sender+"님이 입장하셨습니다."
				}else{
					text=msg.sender+"님이 퇴장하셨습니다."
				}
			}else if(msg.type=="m"){
				text=msg.sender+": "+msg.message
			}if(msg.type=="f"){
				readFile(msg);
			}
			$("#chatroom").append("<p>"+text+"</p>");
		}
	}
	window.addEventListener('beforeunload', function() {
		sendState("logout")
	})
	ws.onclose=function(data){
		sendState("logout")
	}
}
function send(){
	var uN=$("#name").val()
	var msg = $("#message").val();
	var roomId = $("#roomId").val();
	var obj={
			sender:uN,
			message:msg,
			room:roomId,
			type:"m"
	}
	ws.send(JSON.stringify(obj));
	$("#message").val("");
}
function sendState(state){
	var uN=$("#name").val()
	var roomId = $("#roomId").val();
	var obj={
			sender:uN,
			state: state,
			room:roomId,
			type:"l"
	}
	ws.send(JSON.stringify(obj));
	$("#message").val("");

}
function fileSelect(e){
	selectedFile = e.target.files[0];
}
function fileSend(){
	var formData = new FormData();
	formData.append("name",$("#name").val())
	formData.append("message",$("#message").val())
	formData.append("file",selectedFile)
	formData.append("room",$("#roomId").val())
	
 	$.ajax({
		url:'/fileSend',
		type: "POST",
		data: formData,
		processData: false,
		contentType: false,
		success: function(res){

				var uN=$("#name").val()
				var msg = $("#message").val();
				var roomId = $("#roomId").val();
				var obj={
						sender:uN,
						room:roomId,
						fname:selectedFile.name,
						type:"f"
				}
				ws.send(JSON.stringify(obj));

				
				
		},
		error: function(xhr){
			alert("상태: "+xhr.status)
		}
	}) 
}
function readFile(msg){
	var fname=msg.fname;
	$.ajax({
		url:'/fileRead',
		type: "POST",
		xhrFields: {
	        responseType: "blob",
	    },
		data: {'fname':fname},
		success: function(res){
			var imageUrl = URL.createObjectURL(res);
			var p=$("<p>"+msg.sender+"</p>")
		    var imgElement = $('<img>').attr('src', imageUrl);
			$("#chatroom").append(p);
			p.append(imgElement)
				
		},
		error: function(xhr){
			alert("상태: "+xhr.status)
		}
	}) 
}
	
</script>
</html>