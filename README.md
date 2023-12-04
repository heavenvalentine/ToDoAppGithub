To-DoListApp

[x] Define a local database table and DAO (data access object) based on schema in app/schemas/tasks.json. Use FilterUtils#geFilteredQuery to create a filterable query.
[] Initiate RecyclerView with TaskAdapter and update database when onCheckChange.
[] Display title in list-item based on state using TitleTextView (CustomView).
[] Show a detailed task when the list is selected and implement a delete action.
[] Create AddTaskViewModel class to insert a new "task" into the database.
[] Schedule and cancel daily reminder using WorkManager. If notification preference is on, get the earliest active task from the repository and show notification with pending intent.
[] Address the following comment from the QA team:
[] SnackBar does not show when the task completed/activated.
[] Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed.
