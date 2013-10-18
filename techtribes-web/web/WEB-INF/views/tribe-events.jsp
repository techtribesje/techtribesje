<%@ include file="/WEB-INF/fragments/tribe-profile.jspf" %>

<div class="eventsSection">
    <div class="subSectionHeading">Events</div>
    <c:choose>
    <c:when test="${not empty events}">
    <%@ include file="/WEB-INF/fragments/events.jspf" %>
    </c:when>
    <c:otherwise>
    The monkeys couldn't find any events being run by this tribe. :-(
    </c:otherwise>
    </c:choose>
</div>