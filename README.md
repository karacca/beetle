<h1><img src="docs/images/badge.png" alt="Beetle" width="32"/> Beetle [WIP ⚙️]</h1> 

Collect feedback & bug reports on your Android apps into your GitHub Issues.

## Screenshots

<img src="docs/images/feedback.png" alt="Feedback" width="270"/> <img src="docs/images/edit.png" alt="Edit" width="270"/>

## Download

Beetle is available on mavenCentral.
```kotlin
implementation("com.karacca:beetle:2.0.0")
```

## Quick Start

1. Install [Beetle GitHub App](https://github.com/marketplace/beetle-app) to your repository so Beetle can take action on your behalf and create Issues
2. Initialize Beetle inside your Application
```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Beetle.init(this, "username", "repository")
    }
}
```
3. [Optional] Make configurations and add additional data
```kotlin
Beetle.configure {
    enableAssignees = true
    enableLabels = true
    key("version", BuildConfig.VERSION_CODE)
    key("user_id", user.id)
}
```

## License

    Copyright 2022 Omer Karaca

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
