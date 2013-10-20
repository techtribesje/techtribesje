<div class="section codeSection">
    <div class="container">
        <h1>Code</h1>

        <p>
            Some of the locals like to get their hands dirty with a bit of coding, but these aren't your typical 9-5'ers. Take a look at what they've been up to...
        </p>

        <c:forEach var="reposForCoder" items="${reposByCoder}">
            <br /><br />
            <h2>
                <a href="<techtribesje:goto contentSource="${reposForCoder.contentSource}"/>"><img src="${reposForCoder.contentSource.profileImageUrl}" alt="Profile image" class="profileImage" /></a>
                <a href="<techtribesje:goto contentSource="${reposForCoder.contentSource}"/>">${reposForCoder.contentSource.name}</a>
            </h2>

            <c:set var="gitHubRepositories" value="${reposForCoder.gitHubRepositories}" />
            <%@ include file="/WEB-INF/fragments/code.jspf" %>

        </c:forEach>

    </div>
</div>