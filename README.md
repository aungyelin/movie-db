# **MovieDB Android App**

MovieDB is a modern Android application built natively with **Kotlin** and **Jetpack Compose**. It leverages a **Clean Architecture** approach to provide a robust, scalable, and testable codebase, allowing users to explore movies, view detailed information, and manage their personal watchlists.

## **✨ Features**

* **Browse Popular Movies:** Discover trending and popular films.  
* **Now Playing Movies:** View movies currently showing in theaters.  
* **Detailed Movie Screens:** View comprehensive details including plot, cast, ratings, release date, and genre.  
* **Responsive UI:** A beautiful and adaptive user interface built with Jetpack Compose, ensuring a great experience across various Android devices.  
* **Offline Caching:** Seamlessly access previously loaded movie data even without an internet connection. (Future plan)

## **🛠️ Technologies Used**

This project adheres to **Clean Architecture** principles, separating concerns into distinct layers: Presentation, Domain, and Data.

### **Core Technologies**

* **Kotlin:** The primary programming language for Android development.  
* **Jetpack Compose:** Android's modern toolkit for building native UI.  
* **Android Jetpack Libraries:** Leveraging various components for robust app development.

### **Architecture & Patterns**

* **Clean Architecture:** A layered architectural pattern for separation of concerns and testability.  
  * **Presentation Layer:** UI (Jetpack Compose), ViewModels (AndroidX ViewModel).  
  * **Domain Layer:** Business logic, Use Cases, Entities.  
  * **Data Layer:** Repositories, Data Sources (Remote/Local), Models.  
* **MVVM (Model-View-ViewModel):** For structuring the Presentation Layer.  
* **Coroutines & Kotlin Flow:** For asynchronous operations and reactive data streams.  
* **Dependency Injection (Hilt):** For managing dependencies across the application.

### **Libraries & Tools**

* **Retrofit:** A type-safe HTTP client for making API requests (e.g., to TMDB).  
* **Gson:** A JSON serialization/deserialization library.  
* **Room Persistence Library:** For local data caching and offline support.  
* **Navigation Compose:** For navigation within the Compose UI.  
* **Coil (or Glide/Picasso):** For asynchronous image loading and caching.  
* **Material Design 3:** For modern and accessible UI components.

### **API**

* **The Movie Database (TMDB) API:** Used as the primary data source for movie information.

## **🚀 Getting Started**

Follow these instructions to set up and run the MovieDB Android app on your local machine.

### **Prerequisites**

* **Android Studio Dolphin (or newer):** The official IDE for Android development.  
* **JDK 11 (or newer):** Required for Android Studio and Gradle.  
* An Android device or emulator running API Level **24 (Android 7.0 Nougat)** or higher.

### **Installation Steps**

1. **Clone the repository:**  
   git clone https://github.com/aungyelin/movie-db.git  
   cd movie-db  
2. Open in Android Studio:  
   Open the cloned project in Android Studio. Android Studio will automatically set up the project and sync Gradle dependencies.  
3. **Obtain TMDB API Key:**  
   * Go to [The Movie Database (TMDB) website](https://www.themoviedb.org/) and sign up for an account.  
   * Navigate to your [API Settings](https://www.themoviedb.org/settings/api) and request an API key (developer type).  
4. Configure API Key:  
   Create a local.properties file in the root of your project (movie-db/local.properties) and add your TMDB API key:  
   TMDB\_API\_KEY=YOUR\_TMDB\_API\_KEY\_HERE  
   Replace YOUR\_TMDB\_API\_KEY\_HERE with the API key you obtained from TMDB. This file is excluded by .gitignore to prevent committing your API key to version control.

### **Running the Application**

* Connect an Android device to your computer or launch an Android emulator.  
* In Android Studio, click the Run 'app' button (the green triangle icon) in the toolbar.  
* The application will be built and installed on your selected device/emulator.

## **🤝 Contributing**

Contributions are highly encouraged\! Whether it's a bug report, a feature request, or a pull request, your involvement helps make this project better.

To contribute:

1. **Fork** the repository.  
2. Create your feature branch: git checkout \-b feature/your-awesome-feature  
3. Commit your changes following a clear and concise message convention: git commit \-m 'feat: Add a new movie detail screen'  
4. Push to the branch: git push origin feature/your-awesome-feature  
5. Open a **Pull Request** and describe your changes.

Please ensure your code adheres to the project's architectural guidelines and coding conventions.

## **📄 License**

This project is open-source and distributed under the **MIT License**. See the LICENSE file in the root of the repository for full details.