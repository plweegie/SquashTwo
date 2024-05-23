# squash

This app consumes the GitHub API to display information and recent commit history for user's favorite repositories. It uses **Retrofit** for HTTP requests and **Gson** for JSON deserialization.
The favorite repos are stored in a **Room** database. This database is accessed directly for building new commit notifications and
through **StateFlow** and **ViewModel** for displaying the favorites list in UI.
