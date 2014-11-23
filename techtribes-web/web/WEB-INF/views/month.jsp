<div class="section calendarSection">
    <div class="container">
        <h1>${title}</h1>

        <div class="pagingLinks">
            <a href="${previousMonthLink.href}">&lt; ${previousMonthLink.title}</a>
            |
            <a href="${nextMonthLink.href}">${nextMonthLink.title} &gt;</a>
        </div>

        <div id="calendar">
            <c:forEach var="day" items="${viewModel}" varStatus="status">
                <c:if test="${status.index % 6 == 0}">
                    <div class="row" style="margin-bottom: 20px;">
                </c:if>
                        <div class="span2 <c:if test="${day.blank}">hidden-phone</c:if>" style="min-height: 200px;">
                            <div class="dayTitle <c:if test="${day.padding}">faded</c:if>">${day.title}</div>

                            <c:forEach var="event" items="${day.events}">
                                <p class="contentItem">
                                    <a href="<techtribesje:goto contentSource="${event.contentSource}"/>"><img src="${event.contentSource.profileImageUrl}" alt="Profile image" class="profileImageSmall" title="${event.contentSource.name}" /></a>
                                    <img src="<techtribesje:flag name="${event.island}" />" alt="${event.island}" title="${event.island}" />
                                    <a href="/events/${event.id}">${event.title}</a>
                                </p>
                            </c:forEach>

                            <c:forEach var="talk" items="${day.talks}">
                                <p class="contentItem">
                                    <a href="<techtribesje:goto contentSource="${talk.contentSource}"/>"><img src="${talk.contentSource.profileImageUrl}" alt="Profile image" class="profileImageSmall" title="${talk.contentSource.name}" /></a>
                                    <img src="<techtribesje:flag name="${talk.country}" />" alt="${talk.country}" title="${talk.country}" />
                                    <a href="/talks/${talk.id}">${talk.title}</a>
                                </p>
                            </c:forEach>

                            <c:forEach var="newsFeedEntry" items="${day.newsFeedEntries}">
                                <p class="contentItem">
                                    <a href="<techtribesje:goto contentSource="${newsFeedEntry.contentSource}"/>"><img src="${newsFeedEntry.contentSource.profileImageUrl}" alt="Profile image" class="profileImageSmall" title="${newsFeedEntry.contentSource.name}" /></a>
                                    <a href="${newsFeedEntry.permalink}">${newsFeedEntry.title}</a>
                                </p>
                            </c:forEach>
                        </div>
                <c:if test="${status.index % 6 == 5}">
                    </div>
                </c:if>
            </c:forEach>
        </div>

        <div class="pagingLinks">
            <a href="${previousMonthLink.href}">&lt; ${previousMonthLink.title}</a>
            |
            <a href="${nextMonthLink.href}">${nextMonthLink.title} &gt;</a>
        </div>
    </div>
</div>