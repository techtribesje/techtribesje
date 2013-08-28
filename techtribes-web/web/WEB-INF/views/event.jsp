<div class="eventsSection">
    <div class="subSectionHeading">${event.title}</div>

    <p style="clear: both;">
        <c:out value="${event.description}" escapeXml="false" />
    </p>

    <p>
        <a href="/events/${event.id}/icalendar">Add to calendar</a>
        |
        <a href="${event.url}" target="_blank">Read more...</a>
    </p>

    <c:if test="${not empty event.location}">
    <div class="embeddedMap">
        <c:url var="googleMapsUrl" value="https://maps.google.co.uk">
            <c:param name="q" value="${event.location}, ${event.island}" />
            <c:param name="output" value="embed" />
        </c:url>
        <iframe width="640" height="480" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="${googleMapsUrl}"></iframe><br /><small><a href="https://maps.google.co.uk/?q=Radisson+Blu+Waterfront+Hotel,+St+Helier,+Jersey&amp;ie=UTF8&amp;hq=Radisson+Blu+Waterfront+Hotel,+St+Helier,+Jersey&amp;t=m&amp;ll=49.184088,-2.116842&amp;spn=0.013464,0.027466&amp;z=15&amp;iwloc=A&amp;source=embed" style="color:#0000FF;text-align:left">View Larger Map</a></small>
    </div>
    </c:if>

    <div class="eventMetadata">
        <p>
            <c:set var="person" value="${event.contentSource}" />
            <a href="<techtribesje:goto contentSource="${event.contentSource}"/>"><img src="${event.contentSource.profileImageUrl}" alt="Profile image" class="profileImageSmall" /></a>
            <a href="<techtribesje:goto contentSource="${event.contentSource}"/>">${event.contentSource.name}</a>
            |
            <a href="/events/${event.id}/icalendar"><img src="/static/img/add-to-calendar.png" alt="Add to calendar" /></a>
            <techtribesje:date date="${event.startDate}" />
            <c:if test="${not empty event.endDate}">
                until <techtribesje:date date="${event.endDate}" showTime="true" />
            </c:if>
            |
            <c:if test="${not empty event.location}">
                <c:url var="googleMapsUrl" value="http://maps.google.co.uk">
                    <c:param name="q" value="${event.location}, ${event.island}" />
                </c:url>
                <a href="${googleMapsUrl}" target="_blank">${event.location}</a>
            </c:if>
            <img src="<techtribesje:flag name="${event.island}" />" alt="${event.island}" title="${event.island}" />
        </p>
    </div>
</div>