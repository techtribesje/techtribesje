<?xml version="1.0"?>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page session="false" %>
<%@ page import="javax.servlet.jsp.jstl.core.Config" %>
<%@ page import="java.util.Locale" %>
<%@ page contentType="application/xml;charset=UTF-8" language="java" %>
<%
    Config.set(request, Config.FMT_LOCALE, new Locale("en", "GB"));
    Config.set(request, Config.FMT_FALLBACK_LOCALE, Locale.ENGLISH);
%>
<rss version="2.0">
<channel>
  <title>techtribes.je - content from local people and tribes</title>
  <link>http://techtribes.je</link>
  <description>techtribes.je - content from local people and tribes</description>
  <language>en</language>
  <copyright/>
  <lastBuildDate><fmt:formatDate value="${newsFeedEntries[0].timestamp}" pattern="EEE, dd MMM yyyy HH:mm:ss z" timeZone="GMT"/></lastBuildDate>
  <generator/>
  <docs>http://backend.userland.com/rss</docs>
  <c:forEach var="newsFeedEntry" items="${newsFeedEntries}" end="9">
  <item>
    <title>[${newsFeedEntry.contentSource.name}] <c:out value="${newsFeedEntry.title}" escapeXml="true"/></title>
    <link><c:out value="${newsFeedEntry.permalink}" escapeXml="true"/></link>
    <description>
      <c:out value="${newsFeedEntry.truncatedBody}" escapeXml="true"/>
    </description>
    <guid isPermaLink="true"><c:out value="${newsFeedEntry.permalink}" escapeXml="true"/></guid>
    <pubDate><fmt:formatDate value="${newsFeedEntry.timestamp}" pattern="EEE, dd MMM yyyy HH:mm:ss z" timeZone="GMT"/></pubDate>
  </item>
  </c:forEach>
  </channel>
</rss>
