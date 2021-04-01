# Weather app

Was created in case of learning modern android development. 

![home](https://user-images.githubusercontent.com/8080919/113286629-262c1c80-92f5-11eb-9d08-6d64e7f9a5ea.png) ![favorites](https://user-images.githubusercontent.com/8080919/113286637-27f5e000-92f5-11eb-8051-f14d9946431d.png) ![settings](https://user-images.githubusercontent.com/8080919/113286644-29270d00-92f5-11eb-9af9-16fbfef0c082.png)

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
