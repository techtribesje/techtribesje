<div class="section tweetsSection">
    <div class="container">
        <h1>#digitaljersey</h1>

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
        <div class="pagingLinks">
        <c:choose>
            <c:when test="${showPagingLinks ne 'false'}">
                <c:if test="${currentPage > 1}">
                    <a href="/tweets/${currentPage-1}">&lt; Newer</a> |
                </c:if>
                Page ${currentPage}
                <c:if test="${currentPage < maxPage}">
                    | <a href="/tweets/${currentPage+1}">Older &gt;</a>
                </c:if>
            </c:when>
            <c:otherwise>
                <a href="/tweets/">More...</a>
            </c:otherwise>
        </c:choose>
        </div>
    </div>
</div>