<%@ include file="/WEB-INF/fragments/person-profile.jspf" %>

<hr />

<form action="/user/profile" method="POST" class="form-horizontal">

    <div class="control-group">
        <label class="control-label" for="gitHubId">GitHub ID</label>
        <div class="controls">
            <input id="gitHubId" name="gitHubId" type="text" placeholder="ID" value="${person.gitHubId}" class=" input-medium" maxlength="40" />
        </div>
    </div>

    <div class="row">

        <div class="span6">
            <h3>You're a member</h3>
            <h4>Community tribes</h4>
            <c:forEach var="tribe" items="${memberOfSocial}">
                <img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImageSmall" /> ${tribe.name}
            </c:forEach>

            <h4>Tech tribes</h4>
            <c:forEach var="tribe" items="${memberOfTech}">
                    <img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImageSmall" /> ${tribe.name}
            </c:forEach>

            <h4>Business tribes</h4>
            <c:forEach var="tribe" items="${memberOfBusiness}">
                <img src="${tribe.profileImageUrl}" alt="Profile image" class="profileImageSmall" /> ${tribe.name}
            </c:forEach>
        </div>

        <div class="span6">
            <h3>You're not a member</h3>

            <h4>Tech tribes</h4>
            <p>
                Membership of tech tribes is based upon the content that you publish. If you want to join a specific tech tribe, you'll need to write some related content!
            </p>

            <h4>Business tribes</h4>
            <p>
                If you work for or help a business tribe, they will need to add you ... just ask the tribal leader to sign-in with the tribe's Twitter ID to manage its profile.
            </p>
        </div>

    </div>

    <p>
        <input type="submit" class="btn" value="Update" />
    </p>
</form>