<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta name="robots" content="index,follow">
	<link rel="stylesheet" type="text/css" href="/resources/style.css">
	<link rel="shortcut icon" href="/resources/img/favicon.gif">
	<script type="text/javascript" src="/resources/js/jquery.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.lightbox-0.5.min.js"></script>
	<script type="text/javascript" src="/resources/js/scripts.js"></script>
	<title>Movied.com - Not Just Another MOVIEDatabase</title>
  </head>
  <body>
		<div id="holder">
			<p class="center"><img src="/resources/img/logo.png" alt="logo" /></p>
			<ul id="menu">
				<li><a href="/">Home</a></li>
				<li<c:if test="${menu_filmy==true}"> id="active"</c:if>><a href="/movies">Filmy</a></li>
				<li<c:if test="${menu_osobnosti==true}"> id="active"</c:if>><a href="/people">Osobnosti</a></li>
				<li>
					<form method="get" action="/search">
						<input type="text" name="q">
						<button type="submit">Hľadať</button>
					</form>
				</li>
				<li class="fb_login">
					<c:if test="${user == null}">
						<img src="/resources/img/facebook.png" alt="">
						<a href="/login">Prihlásenie</a>
					</c:if>
					<c:if test="${user != null}">
						<img src="${user.getPhoto()}" alt="${user.getMeno()}">
						<a href="/logout">Odhlásiť sa</a>
					</c:if> 
				</li>
			</ul>