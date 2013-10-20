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

            <c:forEach var="repo" items="${reposForCoder.gitHubRepositories}" varStatus="status">
                <c:if test="${status.index % 4 == 0}">
                    <div class="row">
                </c:if>
                        <div class="span3">
                            <div class="codeRepo">
                                <a href="${repo.url}" target="_blank"><img src="/static/img/techtribes/github.png" alt="GitHub" class="profileImageSmall" title="GitHub" /></a> <a href="${repo.url}" target="_blank">${repo.name}</a>
                                <p>
                                ${repo.truncatedDescription}
                                </p>
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

        </c:forEach>
    </div>
</div>