<div class="section tweetsSection">
    <div class="container">
        <h1>#${hashtag}</h1>

        <c:forEach var="tweet" items="${tweets}" varStatus="status">
        <c:if test="${status.index % 4 == 0}">
        <div class="row">
            </c:if>
            <div class="span3">
                <div class="tweet">
                    <img src="${tweet.profileImageUrl}" alt="Profile image" class="profileImageSmall" /></a>
                    <c:out value="${tweet.bodyAsHtml}" escapeXml="false" />
                    <br /><br />
                    <div class="metadata">
                        <a href="${tweet.permalink}" target="_blank">#</a> |
                        Posted <techtribesje:date date="${tweet.timestamp}"/>
                    </div>
                </div>
            </div>
            <c:if test="${status.index % 4 == 3}">
        </div>
        </c:if>

        <c:if test="${status.last and status.index % 4 ne 3}">
        <c:forEach begin="1" end="${3-(status.index % 4)}">
            <div class="span3">&nbsp;</div>
        </c:forEach>
    </div>
    </c:if>
    </c:forEach>
    </div>
</div>