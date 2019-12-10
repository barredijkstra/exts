exts
====
[![Build Status](https://api.travis-ci.org/barredijkstra/exts.png?branch=master)](https://travis-ci.org/barredijkstra/exts)
[![Download](https://api.bintray.com/packages/barredijkstra/maven/exts/images/download.svg) ](https://bintray.com/barredijkstra/maven/exts/_latestVersion)

Collection of random scala helpers that I generally use in my projects.

Feel free to use it in your own projects, but there are no real guarantees on API stability, maintenance or anything besides releases using SemVer.

Basically; this project is mainly (more solely) targeted for use by myself.

## Usage
Are you sure you want to? Are you sure? Really?

Okay, if you really really want to (though you shouldn't)...

Add a resolver for the [barredijkstra/maven bintray](https://bintray.com/barredijkstra/maven/) repo and add the library dependency to your `build.sbt`


```scala
resolvers += Resolver.bintrayRepo("barredijkstra", "maven")

libraryDependencies += "nl.salp" %% "exts" % "x.y.z"
```

Replace `x.y.z` with the [latest library version](https://bintray.com/barredijkstra/maven/exts/_latestVersion).

The project is cross-compiled against both Scala 2.11 (Java 7) and 2.12 (Java 8)

# Versions
## 0.3.0
- Removed Scala 2.11 support
- Added Scala 2.13 support and added 2.12/2.13 specific code branches for 2.13 deprecated code
- Bumped dependency versions
    - `"com.typesafe" % "config" % "1.4.+" % Provided`
    - `"io.spray" %% "spray-json" % "1.3.+" % Provided`
    - `"com.beachape" %% "enumeratum" % "1.5.+" % Provided`
- Upgraded to scalatest 3.1.x
- Cleaned up build

## 0.2.0
- ...

## Credits
- Some spray formats are based on [kebs](https://github.com/theiterators/kebs/)  
- The `tags` comes from miles sabin's awesome [shapeless](https://github.com/milessabin/shapeless) library

## License
Copyright (c) 2018 Barre Dijkstra

Published under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
