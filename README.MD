# Commons
###### Contains various codes that can easily be reused

[![Generic badge](https://img.shields.io/badge/Java-17-white.svg)]()
[![Generic badge](https://img.shields.io/nexus/r/de.hypercdn.commons/Commons?color=white&label=Latest%20Version&nexusVersion=3&server=https%3A%2F%2Fcd.voigt.app%2F)]()

## Installation
Grab the latest version from our [Nexus-Proxy](https://nexus.hypercdn.de/) using Gradle or Maven.

### Gradle

```groovy
repositories {
    maven {
        name 'maven-hypercdn'
        url 'https://nexus.hypercdn.de/repository/maven-hypercdn'
    }
}
```

```groovy
dependencies {
    implementation group: 'de.hypercdn.commons', name: 'Commons', version: 'REPLACE WITH LATEST VERSION'
}
```

### Maven

```xml

<repositories>
    <repository>
        <id>maven-hypercdn</id>
        <name>maven-hypercdn</name>
        <url>https://nexus.hypercdn.de/repository/maven-hypercdn</url>
    </repository>
</repositories>
```

```xml

<dependencies>
    <dependency>
        <groupId>de.hypercdn.commons</groupId>
        <artifactId>Commons</artifactId>
        <version>REPLACE WITH LATEST VERSION</version>
    </dependency>
</dependencies>

```
