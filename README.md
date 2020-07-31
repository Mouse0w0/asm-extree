# asm-extree
[![](https://jitpack.io/v/Mouse0w0/asm-extree.svg)](https://jitpack.io/#Mouse0w0/asm-extree)

A new tree implementation for asm (A Java bytecode manipulation and analysis framework).

## How to use it
### Maven
Step 1. Add the JitPack repository to your build file
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Step 2. Add the dependency
```xml
	<dependency>
	    <groupId>com.github.Mouse0w0</groupId>
	    <artifactId>asm-extree</artifactId>
	    <version>8.0.0</version>
	</dependency>
```
### Gradle
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```xml
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```xml
	dependencies {
	        implementation 'com.github.Mouse0w0:asm-extree:8.0.0'
	}
```
