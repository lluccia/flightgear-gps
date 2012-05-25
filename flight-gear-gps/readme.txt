To build this project the mapgforge library needs to be installed in your local Maven repository.

To accomplish that, having SVN and Maven installed, execute the following commands:

svn checkout http://mapsforge.googlecode.com/svn/tags/0.3.0
mvn clean install

configure -Dandroid.sdk.path=... or set environment variable ANDROID_HOME to your SDK location