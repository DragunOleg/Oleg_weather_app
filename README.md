# Weather app

Was created in case of learning modern android development. 

![home](https://user-images.githubusercontent.com/8080919/112809797-12797f80-9083-11eb-9366-cde6d9a5fa33.png)  ![favorites](https://user-images.githubusercontent.com/8080919/112809801-13aaac80-9083-11eb-921b-1394489a2530.png)  ![settings](https://user-images.githubusercontent.com/8080919/112809807-14dbd980-9083-11eb-8753-24603a147bce.png)

## features

1. Offline caching
1. Home forecast based on location
1. Сustomizable background update
1. Standard/metric/imperial scales of measure

## key libraries and dependencies

1. API by [openweathermap](https://openweathermap.org/api) *put your key in private const val APP_ID = "_YOUR KEY_"*
1. [Retrofit](https://square.github.io/retrofit/) to make http connection
1. [Glide](https://github.com/bumptech/glide) to load images
1. [gson](https://github.com/google/gson) as JSON converter
1. [Timber](https://github.com/JakeWharton/timber) for logging
1. [Jetpack parts](https://developer.android.com/jetpack)
   1. [WorkManager](https://developer.android.com/jetpack/androidx/releases/work) for background tasks
   1. [Room](https://developer.android.com/jetpack/androidx/releases/room) to store data offline
   1. [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview) for lists
   1. [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) for navigation
   1. [Databinding](https://developer.android.com/jetpack/androidx/releases/databinding) for faster UI development

### Others:

Architecture: mvvm. 

Network model = domain model so it might cause problems when API gonna change.

Model is not nullable, so NPE is possible based on server response.