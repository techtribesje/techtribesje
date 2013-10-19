<div class="section">
    <div class="container">
        <h1>News feeds</h1>
        <c:forEach var="contentSource" items="${contentSources}">
            <c:if test="${contentSource.type ne 'Tech'}">
            <div class="row" style="margin-bottom: 8px; padding: 8px 0 8px 0;">
                <div class="span3">
                    <div style="float: left;">
                        <a href="<techtribesje:goto contentSource="${contentSource}"/>"><img src="${contentSource.profileImageUrl}" alt="Profile image" class="profileImageSmall" /></a>
                    </div>
                    &nbsp;<a href="<techtribesje:goto contentSource="${contentSource}"/>">${contentSource.name}</a>
                </div>

                <div class="span9">
                    <ul>
                <c:forEach var="newsFeed" items="${contentSource.newsFeeds}">
                        <li><a href="${newsFeed.url}" target="_blank">${newsFeed.url}</a> (<a href="http://validator.w3.org/feed/check.cgi?url=${newsFeed.url}" target="_blank">validate</a>)</li>
                </c:forEach>
                        <c:if test="${not empty contentSource.twitterId}">
                            <li><a href="http://twitter.com/${contentSource.twitterId}" target="_blank">@${contentSource.twitterId}</a></li>
                        </c:if>
                    </ul>
                </div>
            </div>
            </c:if>
        </c:forEach>
    </div>
</div>
