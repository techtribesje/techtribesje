<div class="talksSection">
    <div class="subSectionHeading">${talk.title}</div>

    <p style="clear: both;">
        <c:out value="${talk.description}" escapeXml="false" />
    </p>

    <p>
        <c:if test="${not empty talk.slidesUrl}">
            <a href="${talk.slidesUrl}" target="_blank">Slides</a> |
        </c:if>
        <c:if test="${not empty talk.videoUrl}">
            <a href="${talk.videoUrl}" target="_blank">Audio/video</a> |
        </c:if>
        <a href="${talk.url}" target="_blank">Read more...</a>
    </p>

    <c:if test="${not empty talk.videoUrl}">
    <div class="embeddedVideo">
        <techtribesje:embedVideo url="${talk.videoUrl}" />
    </div>
    </c:if>

    <div class="eventMetadata">
        <p>
            <c:set var="person" value="${talk.contentSource}" />
            <a href="<techtribesje:goto contentSource="${talk.contentSource}"/>"><img src="${talk.contentSource.profileImageUrl}" alt="Profile image" class="profileImageSmall" /></a> <a href="/people/${talk.contentSource.shortName}">${talk.contentSource.name}</a>
            |
            ${talk.eventName}
            |
            ${talk.type}
            |
            <techtribesje:date date="${talk.date}" showTime="false"/>
            |
            <c:if test="${not empty talk.city}">${talk.city}, </c:if>${talk.country} <img src="<techtribesje:flag name="${talk.country}" />" alt="${talk.country}" class="flag" />
        </p>
    </div>
</div>