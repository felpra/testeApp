# testeApp

The chosen architecture technology was MVVM. MVVM is a modern architecture and was chosen with the aim of extracting the maximum benefits from Android's
native components. It is possible to use MVVM with great robustness by combining it with Jetpack components (StateFlow, ViewModel, Hilt, etc). 
The viewModel layer serves as a presentation layer for the Activity, which is the visual layer. The activity layer was created to be as dumb as possible, 
being only a layer that observes the screen state. The viewModel contains the application state in StateFlow, which is observed by the activity. 
Unit tests were added for the viewModel layer. The code also follows some basic Clean concepts, such as a separation between the data layer (repositories) 
and Domain/Use cases. The robust business logic is present in the Domain layer, represented by Managers, while the viewModel only contains small and light 
logic. As it is not possible to use POST, PUT, or DELETE properly in the provided API, some decisions were made to work around this. 
The app has a local cache, which in addition to being able to populate the screen more quickly, also serves as a data source 
when the application is offline. Favorites are saved in the cache, and there is a check every time the data is retrieved from the API. 
If an element exists in both lists, it receives the favorites value from the same element that is in the cache, thus we can ensure that favorites are 
saved even without being able to do so in the API. For the recyclerview, the implementation was done with DiffUtils, 
with the purpose of bringing more performance to the application. All the main requirements of the application were fulfilled. 
There are some caveats in the Missing Points section at the end of this README.

How to use: There is a button in the right corner of each post that can be used to favorite or unfavorite a post. 
Favorite posts are always shown first on the list. If you press and hold a post for a few seconds, it is deleted. 
If we click on the FAB button in the bottom right corner of the screen, all posts will be deleted except for favorites.

Missing Points / Possible Improvements and Additions:
Most of the following points were not covered due to redundancy (something very similar or the same has already been implemented in the application,
so it is already possible to evaluate the use of it), or simply due to lack of time (I had only a few hours on the weekend to work on the test,
so I focused on what I considered most important and non-trivial)

1 - The app can enter the details and comments screen even in Offline mode, but it only displays the intrinsic details of the post object. 
It would be possible and recommended to create a table of comments and Users and associate them through Room with the post table,
so that this page could also be 100% loaded through the cache. This was not done as it would be redundant work, as the purpose of this test 
is evaluative and there is already a satisfactorily constructed local cache table in the application.

2 - Strings should have been placed in a resource file (strings.xml), but due to lack of time, they ended up being hardcoded.

3 - Unit tests should have been focused on the classes in the Domain layer (manager), but I thought that those demonstrated for the viewModel would 
provide more satisfactory content for evaluation purposes.

4 - The delete and delete all except favorites operations only work for visualization purposes. If you open the application again, 
the posts will be fully reloaded. A similar logic could be used as in the favorites (given the absence of an effective post function in the API), 
but it was not done due to redundancy issues.
