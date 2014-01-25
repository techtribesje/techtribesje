<div class="section">
    <div class="container">
        <h1>Add a person/tribe</h1>
        <form class="form-horizontal" action="/admin/add" method="POST">

            <div class="control-group">
                <label class="control-label" for="name">Name</label>
                <div class="controls">
                    <input id="name" name="name" type="text" placeholder="Name" value="${contentSource.name}" class=" input-large" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="twitterId">Twitter ID</label>
                <div class="controls">
                    <input id="twitterId" name="twitterId" type="text" placeholder="Name" value="${contentSource.twitterId}" class=" input-medium" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="profile">Profile</label>
                <div class="controls">
                    <input id="profile" name="profile" type="text" placeholder="Name" value="${contentSource.profile}" class=" input-xxlarge" maxlength="512" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="profileImageUrl">Profile Image</label>
                <div class="controls">
                    <input id="profileImageUrl" name="profileImageUrl" type="text" placeholder="http://" value="${contentSource.profileImageUrl}" class=" input-xxlarge" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="url">URL</label>
                <div class="controls">
                    <input id="url" name="url" type="text" placeholder="http://" value="" class=" input-xxlarge" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="type">Island</label>
                <div class="controls">
                    <select id="island" name="island">
                        <option value="j">Jersey</option>
                        <option value="g">Guernsey</option>
                        <option value="n">None</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="type">Type</label>
                <div class="controls">
                    <select id="type" name="type">
                        <option value="p">Person</option>
                        <option value="c">Social Tribe</option>
                        <option value="t">Tech Tribe</option>
                        <option value="b">Business Tribe</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="feedUrl1">Feed URL</label>
                <div class="controls">
                    <input id="feedUrl1" name="feedUrl1" type="text" placeholder="Feed URL" value="" class=" input-xxlarge" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="feedUrl2">Feed URL</label>
                <div class="controls">
                    <input id="feedUrl2" name="feedUrl2" type="text" placeholder="Feed URL" value="" class=" input-xxlarge" />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="feedUrl3">Feed URL</label>
                <div class="controls">
                    <input id="feedUrl3" name="feedUrl3" type="text" placeholder="Feed URL" value="" class=" input-xxlarge" />
                </div>
            </div>

            <div class="control-group">
                <label class="checkbox">
                    <div class="controls">
                        <input type="checkbox" name="contentAggregated" value="true" checked="true" /> Content aggregated
                        </div>
                </label>
            </div>

            <p>
                <input type="submit" class="btn" value="Add" />
            </p>
        </form>
    </div>
</div>