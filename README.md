# notesmagotes

The purpose of this project is to serve as a proof-of-concept.  It aims to demonstrate the working of a RESTful API from both ends.  The front-end (this) requests various CRUD requests from the node.js back-end.  This project is more of a functional one than a production one;  some crashes have been handled with where others have not and crash the app. Corners have been cut in terms of design language, where regular buttons have been used instead of navigation drawers, menus, or FABs, and toasts/log messages have been used instead of snackbars.

This code is written in java and leverages modules such as Retrofit, JSON, and Butterknife.  The app looks for a suitable back-end on the computer's localhost.

You are free to use this project as-is, or to modify as you see fit after cloning.  This front-end code must be altered (the BASE_URL to send/receive HTTP requests from) in order for it to be functional enough for minor use, if the .apk is compiled.  Owing to the nature of this project, it will undergo changes before I am done.

###To-Do:

1. ~~Authentication through JSON tokens for users.~~  Implemented.  Users now authenticated with JWT sent from node.js app.  Tokens stored in shared preferences.
2. Introduction of, and deletion of HTTP requests and routes as they arise.  **ONGOING.**
3. ~~Push-notification support.~~  Implemented.  Users now notified of new notes saved to their notes.  Ability to send notes to other users by entering their username, reminiscent of e-mail systems of old.
4. ~~Interfacing with an actual remote VPS to ensure compatibility.~~  Implemented.  Works.
5. Bug squashing.  Many bugs still remaining, many errors not caught or handled with.  The app works well if everything is errorless, otherwise some unhandled errors may crash the app.  Use at your own discretion.