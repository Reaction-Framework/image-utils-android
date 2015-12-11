# rction-image-android

## Getting started

`npm install https://github.com/Reaction-Framework/rction-image-android.git --save`

## Android setup 

* Add to your `settings.gradle`:
```
include ':io.reactionframework.android.image'
project(':io.reactionframework.android.image').projectDir = new File(settingsDir, '../node_modules/image-utils-android')
```
* Add to your `app/build.gradle`:
```
dependencies {
	...
	compile project(':io.reactionframework.android.image')
}
```
