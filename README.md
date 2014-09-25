
GoogleImageSearch Android App
=============================

It's an app which allows a user to select search filters and paginate results infinitely.

User Stories covered
--------------------

- [x] User can enter a search query that will display a grid of image results from the Google Image API.
- [x] User can click on "settings" which allows selection of advanced search options to filter results
- [x] User can configure advanced search filters such as:
	    * Size (small, medium, large, extra-large)
		* Color filter (black, blue, brown, gray, green, etc...)
		* Type (faces, photo, clip art, line art)
		* Site (espn.com)
- [x] Subsequent searches will have any filters applied to the search results
- [x] User can tap on any image in results to see the image full-screen
- [x] User can scroll down “infinitely” to continue loading more image results (up to 8 pages)

Advanced User Stories covered
-----------------------------

- [x] Robust error handling, check if internet is available, handle error cases, network failures
- [x] Use the ActionBar SearchView or custom layout as the query box instead of an EditText
- [x] User can share an image to their friends or email it to themselves
- [x] Replace Filter Settings Activity with a lightweight modal overlay
- [x] Improve the user interface and experiment with image assets and/or styling and coloring

Bonus
-----

- [x] Use the StaggeredGridView to display improve the grid of image results
- [x] User can zoom or pan images displayed in full-screen detail view
- [x] Implemented *Voice* search feature using android's default configuration
- [x] Displaying the Image Title as a Action Bar Title

TODO
----

- Use Parceble instead of Serializable
- Implement View Pager to allow users swipe through images once in the Image view Activity

Time Spent : 30 hours

Screenshots
-----------

Please click on the image below to see the demo!!!

[![ScreenShot](https://github.com/yatrikp/GoogleImageSearch/blob/master/Image-Search-1.JPG?raw=true)](http://player.vimeo.com/video/107114776)

 