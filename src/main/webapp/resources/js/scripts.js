'use strict'

var editFlag = false;
var id = -1;
var page = null;

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();
	return Math.floor(date * random).toString();
};

var theMessage = function(msgText, userName) {
	return {
		text: msgText,
		name: userName,
		edited: false,
		deleted: false,
		id: uniqueId()
	};
};

function run(){
	document.addEventListener('click', delegateEvent);
	page = restore() || {	
							messages: [theMessage('Welcome!', 'Admin')],
							name: "",
							cond: true
						};	
	createPage(page);
}

function delegateEvent(evtObj) {
	if(evtObj.type === 'click'){
		if (evtObj.target.classList.contains('btn-success') 
			|| evtObj.target.classList.contains('btn-info'))
			onInputNameButtonClick(evtObj);
		else if (evtObj.target.classList.contains('btn-primary')){
			onSendMsgButtonClick(evtObj);
		}
		else if (evtObj.target.classList.contains('btn-default') 
			|| evtObj.target.classList.contains('glyphicon')){
			onEditMsgButtonClick(evtObj);
		}
	}
}

function greetingCreation(value){
	var greeting = (document.getElementsByTagName('h3')[0]) || document.createElement('h3');
	greeting.innerHTML = 'Hello, ' + page.name + '!';
	return greeting; 
}

function setAttr(obj, attrType, attrValue){
	var attr = document.createAttribute(attrType);
	attr.value = attrValue;
	obj.setAttributeNode(attr);
}

function childCreation(value, elType){
	var child = document.createElement(elType);
	setAttr(child, 'class', value);
	return child;
}

function iconCreation(btnClass, color){
	var sp = document.createElement('span');
	setAttr(sp, 'class', btnClass);
	setAttr(sp, 'style', 'color:' + color + ';');
	return sp;
}

function btnCreation(btnClass){
	var btn = document.createElement('button');
	setAttr(btn, 'type', 'button');
	setAttr(btn, 'class', 'btn btn-default btn-sm');
	var sp = iconCreation(btnClass, '#003264');
	btn.appendChild(sp);
	return btn;
}

function createMsg(msg){
	var userMessage = childCreation("userMessage", 'div');
	setAttr(userMessage, 'id', msg.id);

	var userName = childCreation("userName", 'div');
	userName.innerHTML = msg.name;
	userMessage.appendChild(userName);

	if(!msg.deleted){
		if(msg.edited){
			var sp = iconCreation("glyphicon glyphicon-pencil", '#ff0000');
			userMessage.appendChild(sp);
		}
		
		var text = childCreation("text", 'pre');
		text.innerHTML = msg.text;
		userMessage.appendChild(text);

		
		if(msg.name == page.name){
			var delBtn = btnCreation("glyphicon glyphicon-pencil", '#003264');
			var editBtn = btnCreation("glyphicon glyphicon-trash", '#003264');
			userMessage.appendChild(delBtn);
			userMessage.appendChild(editBtn);
		}
	}

	else{
		var sp = iconCreation("glyphicon glyphicon-trash", '#ff0000');
		userMessage.appendChild(sp);
	}
	return userMessage;
}

function updateName(page){
	var items = document.getElementsByClassName('inputName')[0];
	var greeting = greetingCreation(page.name);
	items.appendChild(greeting);
	document.getElementById("sendBtn").style.visibility = "visible";
	document.getElementById("coolMan").style.visibility = "visible";
	document.getElementById("inputMsgText").style.visibility = "visible";
	document.getElementById("form1").style.visibility = "hidden";
	document.getElementById("form2").style.visibility = "visible";
}

function createPage(page){
	serverCheck(page.cond);
	if(page.name.length > 0){
		updateName(page);		
	}
	var items = document.getElementsByClassName('history')[0];
	while(items.childNodes[0]){
		items.removeChild(items.childNodes[0]);
	}	
	for(var i = 0; i < page.messages.length; i++){
		var msg = page.messages[i];
		var userMessage = createMsg(msg);
		items.appendChild(userMessage);
	}
	items.scrollTop = 9999;
}

function onInputNameButtonClick(evtObj){
	var nameField;
	nameField = (evtObj.target.classList.contains('btn-success')) ? 
				document.getElementById('nameInputText') : document.getElementById('nameChangeText');
	setName(nameField.value);
	createPage(page);
	store(page);
}

function onSendMsgButtonClick(evtObj){
	var textField = document.getElementById('inputMsgText');
	(editFlag) ? sendEditedMsg(textField.value, evtObj) : sendMsg(textField.value, evtObj);
	textField.value = '';
	createPage(page);
	store(page);
}

function onEditMsgButtonClick(evtObj){
	var msg = (evtObj.target.hasChildNodes()) ? evtObj.target.parentElement : evtObj.target.parentElement.parentElement;
	id = msg.getAttribute('id');
	var indicator = (evtObj.target.hasChildNodes()) ? evtObj.target.firstElementChild.className : evtObj.target.className;
	(indicator == "glyphicon glyphicon-pencil") ? editMsg(id) : removeMsg(id);
}

function editMsg(id){
	var textToChange = "";
	for (var i = 0; i < page.messages.length; i++){
		var msg = page.messages[i];
		if(msg.id == id && msg.name == page.name && msg.id != 'deleted'){
			textToChange = msg.text;
			editFlag = true;
			break;
		}
		else if (msg.id == id){
			alert("you can't change this!");
			break;
		}
	}
	var field = document.getElementById("inputMsgText");
	field.value = textToChange;	
}

function removeMsg(id){
	for (var i = 0; i < page.messages.length; i++){
		var msg = page.messages[i];
		if(msg.id == id && msg.name == page.name){
			msg.deleted = true;
			/*for (var j = i; j < page.messages.length - 1; j++)
				page.messages[j] = page.messages[j + 1];
			page.messages.pop();*/
			break;
		}
		else if (msg.id == id){
			alert("you can't delete this!");
		}
	}
	createPage(page);
	store(page);
}

function sendMsg(value, evtObj){
	if(!value){
		return;
	}
	var objMsg = theMessage(value, page.name);
	page.messages.push(objMsg);
}

function sendEditedMsg(value, evtObj){
	for (var i = 0; i < page.messages.length; i++){
		var msg = page.messages[i];
		if(msg.id == id){
			page.messages[i] = theMessage(value, page.name);
			page.messages[i].edited = true;
			break;
		}
	}
	editFlag = false;
}

function setName(value){
	if(!value){
		return;
	}
	page.name = value;
}

function store(pageToSave) {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}
	localStorage.clear();
	localStorage.setItem("Chatting page", JSON.stringify(pageToSave));
}

function restore() {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}
	var item = localStorage.getItem("Chatting page");
	return item && JSON.parse(item);
}

function serverCheck(flag){
	if (flag){
		document.getElementById("greenLamp").style.visibility = "visible";
		document.getElementById("redLamp").style.visibility = "hidden";
	}
	else{
		document.getElementById("greenLamp").style.visibility = "hidden";
		document.getElementById("redLamp").style.visibility = "visible";
	}
}