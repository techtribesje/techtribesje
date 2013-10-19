<div class="section eventsSection">
    <div class="container">
        <h1>
            <a href="<techtribesje:goto contentSource="${event.contentSource}"/>"><img src="${event.contentSource.profileImageUrl}" alt="Profile image" class="profileImage" /></a>
            ${event.title}
        </h1>

        <p style="clear: both;">
            <c:out value="${event.description}" escapeXml="false" />
        </p>

        <p>
            <img src="<techtribesje:flag name="${event.island}" />" alt="${event.island}" title="${event.island}" />
            <techtribesje:date date="${event.startDate}" />
            <c:if test="${not empty event.location}">
                |
                <c:url var="googleMapsUrl" value="http://maps.google.co.uk">
                    <c:param name="q" value="${event.location}, ${event.island}" />
                </c:url>
                <a href="${googleMapsUrl}" target="_blank">${event.location}</a>
            </c:if>
            |
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
            <iframe width="640" height="480" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="${googleMapsUrl}"></iframe><br /><small><a href="https://maps.google.co.uk/?q=Radisson+Blu+Waterfront+Hotel,+St+Helier,+Jersey&amp;ie=UTF8&amp;hq=Radisson+Blu+Waterfront+Hotel,+St+Helier,+Jersey&amp;t=m&amp;ll=49.184088,-2.116842&amp;spn=0.013464,0.027466&amp;z=15&amp;iwloc=A&amp;source=embed" style="text-align:left" target="_blank">View Larger Map</a></small>
        </div>
        </c:if>

    </div>
</div>