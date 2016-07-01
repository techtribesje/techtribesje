This section provides information about the desired quality attributes (non-functional requirements) of the techtribes.je website.

## Performance

All pages on techtribes.je should load and render in under five seconds, for fifty concurrent users.

## Scalability

The techtribes.je website should be able to scale to ten times the current data volumes, as follows:

 - 1000 people and tribes
 - 500,000 tweets
 - 10,000 news/blog posts 
 
## Security

Although most of the techtribes.je website can be viewed by anonymous users, it must provide role-based access to allow people/tribes to log-in and manage their profile. In order to reduce the operational support overhead associated with managing user credentials, all authentication must be done via a third-party mechanism such as Twitter, Facebook, Google, OpenID, etc.

## Availability

Since techtribes.je is a not a mission critical system and [has a limited budget](#Constraints), there are no strict availability targets.

## Internationalisation

All user interface text will be presented in English only.

## Localisation

All information will be formatted for the British English locale only.

## Browser compatibility

The techtribes.je website should work consistently across the following browsers:

 - Safari
 - Firefox
 - Chrome
 - Internet Explorer 8 (and above)