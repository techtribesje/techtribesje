<div class="section">
    <div class="container">
        <div id="infographics">

            <div class="row">
                <div class="span3">
                    <div class="statistic">${fn:length(people)}</div>
                    <div class="title"><a href="/people">people</a></div>
                </div>
                <div class="span9">
                    <c:forEach var="person" items="${people}">
                        <a href="<techtribesje:goto contentSource="${person}" />"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage <c:if test="${not person.active}">faded</c:if>" title="${person.name}" /></a>
                    </c:forEach>
                </div>
            </div>

            <hr />

            <div class="row">
                <div class="span8">
                    <c:forEach var="tribe" items="${businessTribes}">
                        <a href="<techtribesje:goto contentSource="${tribe}" />"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage <c:if test="${not tribe.active}">faded</c:if>" title="${tribe.name}" /></a>
                    </c:forEach>
                </div>
                <div class="span4">
                    <div class="statistic">${fn:length(businessTribes)}</div>
                    <div class="title"><a href="/business">businesses</a></div>
                </div>
            </div>

            <hr />

            <div class="row">
                <div class="span4">
                    <div class="statistic">${fn:length(communityTribes)}</div>
                    <div class="title"><a href="/community">communities</a></div>
                </div>
                <div class="span3">
                    <c:forEach var="tribe" items="${communityTribes}">
                        <a href="<techtribesje:goto contentSource="${tribe}" />"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage <c:if test="${not tribe.active}">faded</c:if>" title="${tribe.name}" /></a>
                    </c:forEach>
                </div>
                <div class="span3">
                    <div class="statistic">${fn:length(mediaTribes)}</div>
                    <div class="title"><a href="/media">media organisations</a></div>
                </div>
                <div class="span2">
                    <c:forEach var="tribe" items="${mediaTribes}">
                        <a href="<techtribesje:goto contentSource="${tribe}" />"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage <c:if test="${not tribe.active}">faded</c:if>" title="${tribe.name}" /></a>
                    </c:forEach>
                </div>
            </div>

            <hr />

            <div class="row">
                <div class="span6">
                    <div class="statistic">${numberOfNewsFeedEntries}</div>
                    <div class="title"><a href="/content">blog entries</a></div>
                </div>
                <div class="span6">
                    <div class="statistic" style="font-size: 100px;">${numberOfTweets}</div>
                    <div class="title"><a href="/tweets">tweets</a> <img src="/static/img/twitter.png" alt="Twitter" /></div>
                </div>
            </div>

            <hr />

            <div class="row">
                <div class="span3">
                    <div class="statistic">${fn:length(talks)}</div>
                    <div class="title"><a href="/talks">talks</a></div>
                </div>
                <div class="span1"><div class="title" style="padding-top: 60px; color: gray;">by</div></div>
                <div class="span3">
                    <div class="statistic">${fn:length(speakers)}</div>
                    <div class="title">local people</div>
                    <br />
                    <c:forEach var="person" items="${speakers}">
                        <a href="<techtribesje:goto contentSource="${person}" />"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImageSmall" title="${person.name}" /></a>
                    </c:forEach>
                </div>
                <div class="span1"><div class="title" style="padding-top: 60px; color: gray;">in</div></div>
                <div class="span4">
                    <div class="statistic">${fn:length(countries)}</div>
                    <div class="title">countries</div>
                    <br />
                    <c:forEach var="country" items="${countries}">
                        <img src="<techtribesje:flag name="${country}" />" alt="${country}" title="${country}" />
                    </c:forEach>
                </div>
            </div>

            <hr />

            <div class="row">
                <div class="span3">
                    <div class="title"><a href="/tech">Lots of interests</a></div>
                </div>
                <div class="span9">
                    <c:forEach var="tribe" items="${techTribes}">
                        <a href="<techtribesje:goto contentSource="${tribe}" />"><img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImage" title="${tribe.name}" /></a>
                    </c:forEach>
                </div>
            </div>

            <hr />

            <div class="row">
                <div class="span6">
                    <h3>Who?</h3>
                    <p>
                        Tech tribes is run by:
                        <br />
                        <c:forEach var="person" items="${team}">
                            <a href="/people/${person.shortName}"><img src="${person.profileImageUrl}" alt="Profile image" class="profileImage" title="${person.name}" /></a>
                        </c:forEach>
                    </p>
                </div>

                <div class="span6">
                    <h3>How?</h3>
                    <p>
                        techtribes.je is powered by Java, Apache Tomcat, MySQL, MongoDB, Apache Lucene, HTML5 and Twitter Bootstrap. It lives in the cloud.
                    </p>
                    <p>
                        You can validate <a href="http://validator.w3.org/check?uri=http%3A%2F%2Ftechtribes.je%2F" target="_blank">the HTML</a> and <a href="http://validator.w3.org/feed/check.cgi?url=http%3A%2F%2Ftechtribes.je%2Frss.xml" target="_blank">the RSS feed</a>.
                    </p>
                    <p>
                        The code behind techtribes.je is open source and available on <a href="https://github.com/techtribesje/techtribesje" target="_blank">GitHub</a>. This is build number <%= je.techtribes.util.Version.getBuildNumber() %> from commit <a href="https://github.com/techtribesje/techtribesje/tree/<%= je.techtribes.util.Version.getGitCommit() %>" target="_blank"><%= je.techtribes.util.Version.getGitCommit() %></a> ... in case you were interested. :-)
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>