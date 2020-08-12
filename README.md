# ReviewPromptManager

ReviewPromptManager supports to show review prompt
(use [Google Play In-App Review API](https://developer.android.com/guide/playcore/in-app-review)).

## How To Get Started

### Installation with Gradle

Add dependency to build.gradle (module).

```gradle
implementation 'jp.hitting.review-manager:review-manager:0.0.1'
```

### Manual Installation

- Download source code.
- Add ReviewPromptManager.kt to your project.
- Add dependency to build.gradle (module)
  - `implementation 'com.google.android.play:core:1.8.+'`
  - or
  - `implementation 'com.google.android.play:core-ktx:1.8.+'`

## Requirements

Android 5.0 or higher.

## Usage

### Set up

Set up `daysUntilPrompt` and `usesUntilPrompt` at Application class.

- `daysUntilPrompt`: Days until showing review prompt
- `usesUntilPrompt`: Uses until showing review prompt
- `IS_DEV`: Use in development because Google Play In-App Review API requires your app to be published in Play Store.
  - Please see [Test in-app reviews](https://developer.android.com/guide/playcore/in-app-review/test)
- `DEBUG`: Use for debug. If true, review prompt is always showed.

```kotlin
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        ReviewPromptManager.daysUntilPrompt = 1
        ReviewPromptManager.usesUntilPrompt = 10
//        ReviewPromptManager.IS_DEV = true
//        ReviewPromptManager.DEBUG = true
    }
}
```

### Record Launched

Call `launched(Context)` from first activity.
Record the number of launched app and first launched date.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ReviewPromptManager.launched(this)
}
```

### Show Review Prompt

Call `showReviewPrompt(Activity, PromptListener?)` to show review prompt when you want to show it.

Show review prompt if the following conditions are satisfied:

- the number of launched app >= usesUntilPrompt
- the number of days elapsed >= daysUntilPrompt

## Example

See the sample module.

## Credits

ReviewPromptManager was created by [IOKA Masakazu](https://www.hitting.jp).

## License

MIT License

Copyright (c) 2020 IOKA

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
