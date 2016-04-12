<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<style>
body{
 font-size:15px;
 font-family:Helvetica;
 color:#0431B4;
} 
</style>
<title>Welcome to Dropbox 2.0</title>
</head>
<body>


<c:choose>
<c:when test ="${user != null }">
<p>
<FONT-SIZE="5"> <b><i>Welcome ${user.email} to Dropbox 2.0!</i></b> <br/>

<br/>
</p>
Feel free to create a new directory:
<form method="post" action="/directory">
	<input type="text" name ="textfield" value=""/>
	<input type="submit" name="button" value="add"/>
</form>
	</br>
Feel free to upload a new file:
	<form enctype="multipart/form-data" method="post" action="${uploadURL}"/>
		<input type="file" name="file" size="30" /> <input type="submit" />
	</form>
</br></br>
<b>Contents of ${currentPath}</b>
<br/>
<c:choose>
<c:when test ="${currentPath != '/'}">
<p>
<form method="post" action="/directory">
	<input type="hidden" name="key_value" value="../"/>
	<input type="submit" name="button" value="change" />../
</form>
</c:when>
</c:choose>
<br/><br/><br/>
<b>Subdirectories</b>
<br/><br/>
<c:forEach items="${subDirectories}" var="name" begin="0" varStatus="loop">
	<form method="post" action="/directory">
		<input type="hidden" name="key_value" value="${name}"/>
		<input type="submit" name="button" value="change"><input type="submit" name="button" value="delete">${name}
		<br/><br/><br/>
	</form>
</c:forEach>

<b>Files</b>
<br/>
<c:forEach items="${fileNames}" var="name" begin="0" varStatus="loop">
	<form method="get" action="/files">
		<input type="hidden" name="key_value" value="${name.name}"/>
		<input type="submit" name="button" value="download">
		<input type="submit" name="button" value="delete">${name.name}	
	</form>
	
	
</c:forEach>
<br/><br/><br/><br/>
<i>Tired of Dropbox 2.0? :( sign out <a href="${logout_url}">here</a></i><br/>			
</c:when>

<c:otherwise>
<p>
Welcome to the best Dropbox in the World, Dropbox 2.0! <br/>
<a href="${login_url}">Sign in or register</a>
</p>
</c:otherwise>
</c:choose>
</body>
</html>