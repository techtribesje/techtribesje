<div class="section talksSection">
    <div class="container">
        <h1>
            <a href="<techtribesje:goto contentSource="${talk.contentSource}"/>"><img src="${talk.contentSource.profileImageUrl}" alt="Profile image" class="profileImage" /></a>
            ${talk.title}
        </h1>

        <p>
            <c:out value="${talk.description}" escapeXml="false" />
        </p>

        <c:if test="${not empty talk.videoUrl}">
        <div class="embeddedVideo">
            <techtribesje:embedVideo url="${talk.videoUrl}" />
        </div>
        </c:if>

        <p>
            <img src="<techtribesje:flag name="${talk.country}" />" alt="${talk.country}" class="flag" />
            ${talk.type}
            at
            ${talk.eventName}
            |
            <c:if test="${not empty talk.city}">${talk.city}, </c:if>${talk.country}
            |
            <techtribesje:date date="${talk.date}" showTime="false"/>
            |
            <c:if test="${not empty talk.slidesUrl}">
                <a href="${talk.slidesUrl}" target="_blank">Slides</a> |
            </c:if>
            <c:if test="${not empty talk.videoUrl}">
                <a href="${talk.videoUrl}" target="_blank">Audio/video</a> |
            </c:if>
            <a href="${talk.url}" target="_blank">Read more...</a>
        </p>
    </div>
</div>