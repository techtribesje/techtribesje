<%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

<div class="talksSection">
    <div class="subSectionHeading">Talks by the tribe's members</div>

    <c:choose>
        <c:when test="${not empty talks}">
            <%@ include file="/WEB-INF/fragments/talks.jspf" %>
        </c:when>
        <c:otherwise>
            The monkeys couldn't find any talks by this tribe. :-(
        </c:otherwise>
    </c:choose>
</div>