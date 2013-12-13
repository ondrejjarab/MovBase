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
				<li><a href="/">Filmy</a></li>
				<li><a href="/">Osobnosti</a></li>
				<li clas="right"><input type="text"></li>
				<li clas="right">
					<c:if test="${user == null}">
						<a href="/login">Prihlásenie cez Facbeook</a>
					</c:if>
					<c:if test="${user != null}">
						<img src="${user.getPhoto()}" alt="${user.getMeno()}">
						<a href="/logout">Odhlásiť sa</a>
					</c:if> 
				</li>
			</ul>