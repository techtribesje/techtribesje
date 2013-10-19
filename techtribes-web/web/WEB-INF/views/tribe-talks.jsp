<div class="section talksSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
        <h1 class="noBackground"><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Talks</h1>

        <c:choose>
            <c:when test="${not empty talks}">
                <%@ include file="/WEB-INF/fragments/talks.jspf" %>
            </c:when>
            <c:otherwise>
                <p>
                We couldn't find any talks by this tribe. :-(
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>