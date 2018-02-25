# DebugAlter
Alter Android app behavior without rebuild when debugging.

You can change method without build when debugging in your app.
And this changes does not affected to production code.

**So you can implement debug menu safely!**

## Usage

![Platform](http://img.shields.io/badge/platform-android-green.svg?style=flat)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)
 [ ![Download](https://api.bintray.com/packages/takahirom/maven/debugalter-plugin/images/download.svg) ](https://bintray.com/takahirom/maven/debugalter-plugin/_latestVersion)


![read](https://user-images.githubusercontent.com/1386930/33054217-3d185c9e-cebb-11e7-9e09-91c30c30eafc.gif)


This is image for implementation.

![image](https://user-images.githubusercontent.com/1386930/33059689-bcc3a62a-ced8-11e7-89c8-f6ef5e9c0f0d.png)


### Production code

You can use the `@ DebugReturn` annotation for the method you want to change the return value.
(You can use this library in Kotlin and Java!)

app/src/**main**/java/com/../MainActivity.kt

```kotlin
    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val fab = findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener { view ->
                if (isSnackbarShowTiming()) {
                    Snackbar.make(view, getSnackbarText(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                }
            }
        }

        @DebugReturn
        fun isSnackbarShowTiming(): Boolean {
            return false
        }

        @DebugReturn
        fun getSnackbarText(): String {
            return "bad"
        }
```

### Debug code

Create DebugApplication class for setting `@DebugReturn` methods.

app/src/**debug**/AndroidManifest.xml

```xml
    <application
        android:name=".DebugApp"
        tools:replace="android:name"
        />
```


Write bindings between shared preference and method in your DebugApp.

app/src/**debug**/java/.../DebugApp.kt

```kotlin
// Extends your main Application classs
class DebugApp : App() {

    override fun onCreate() {
        super.onCreate()

        val preference = PreferenceManager.getDefaultSharedPreferences(this)
        val items = arrayListOf<DebugAlterItem<*>>(
                object : DebugAlterItem<String>("getSnackbarText") {
                    override fun isAlter(): Boolean = preference.contains(key)
                    override fun get(): String? = preference.getString(key, null)
                },
                object : DebugAlterItem<Boolean>("isSnackbarShowTiming") {
                    override fun isAlter(): Boolean = preference.contains(key)
                    override fun get(): Boolean? = preference.getBoolean(key, false)
                })

        DebugAlter.getInstance().setItems(items)

```

You can confirm Notification and PreferenceFragment implementation in app/ folder.


### Advanced usage
You can use the alter key instead of method name for identifying alter item

```
@DebugReturn("tutorial")
```


```kotlin
val items = arrayListOf<DebugAlterItem<*>>(
                object : DebugAlterItem<String>("tutorial") {
                    override fun isAlter(): Boolean = preference.contains(key)
                    override fun get(): String? = preference.getString(key, null)
                }
                ...
```


### Setup

latest version : [ ![Download](https://api.bintray.com/packages/takahirom/maven/debugalter-plugin/images/download.svg) ](https://bintray.com/takahirom/maven/debugalter-plugin/_latestVersion)

```gradle
{
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath "com.github.takahirom.debug.alter:plugin:**write latest version here**"
    }
}
```

```gradle
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'com.github.takahirom.debug.alter' // add this plugin
```
