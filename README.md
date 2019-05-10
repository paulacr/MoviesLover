# MoviesLover
Movie DB API 

# Architecture Decisions:
- MVVM (architecture components) + DataBinding + Koin (dependency injection)
This model provides a better use of architecture components , which can easily handle the rotation
Databinding allows direct communication between viewmodel and layouts

 # Third party libraries
- Rxjava, Retrofit, Databinding, support library, Koin
The RxJava was used to perform request, transform data and move between threads.
Koin was used to apply the dependency injection, is easier to work comparing to other frameworks

 The app was built on: Android studio 3.4 and requires gradle build tools: 3.4.0