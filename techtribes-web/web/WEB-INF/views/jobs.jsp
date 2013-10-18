<div class="section jobsSection">
    <div class="sectionHeading">
        <div class="container">
            <h1>Jobs</h1>
        </div>
    </div>

    <div class="container">
        <c:choose>
            <c:when test="${not empty jobs}">
                <%@ include file="/WEB-INF/fragments/jobs.jspf" %>
            </c:when>
            <c:otherwise>
                <p>
                    Sorry, we don't have any jobs listed for you at the moment. Please do pop back in the future though.
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>