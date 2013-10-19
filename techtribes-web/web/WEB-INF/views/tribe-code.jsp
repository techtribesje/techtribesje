<div class="section codeSection">
    <div class="container">
        <%@ include file="/WEB-INF/fragments/tribe-navigation.jspf" %>
        <h1><a href="<techtribesje:goto contentSource="${tribe}"/>"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" /></a> Code</h1>

        <c:choose>
        <c:when test="${not empty tribe.gitHubId}">
            <table class="table">
            <tbody>
            <c:forEach var="repo" items="${gitHubRepositories}">
            <tr>
                <td style="border-top: none;"><a href="${repo.url}" target="_blank"><img src="/static/img/techtribes/github.png" alt="GitHub" class="profileImageSmall" title="GitHub" /></a>&nbsp;<a href="${repo.url}" target="_blank">${repo.name}</a></td>
                <td style="border-top: none;">${repo.description}</td>
            </tr>
            </c:forEach>
            </tbody>
            </table>

        </c:when>
        <c:otherwise>
            <p>
            We couldn't find any code by this tribe. :-(
            </p>
        </c:otherwise>
        </c:choose>
    </div>
</div>