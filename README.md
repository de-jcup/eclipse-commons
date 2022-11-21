# de-jcup-eclipse-commons
[![Java CI with Maven](https://github.com/de-jcup/eclipse-commons/actions/workflows/maven.yml/badge.svg)](https://github.com/de-jcup/eclipse-commons/actions/workflows/maven.yml)

*You will find the sources under https://github.com/de-jcup/eclipse-commons*

A library containing reuseable eclipse solutions. This is a simple approach to resolve the
problem of too much copy & waste in de-jcup plugins. Some bigger and trick parts are bundled 
here as a simple library which can be used by plugins.

## Howto develop/test
For testing and development you can start it as a normal eclipse plugin inside your PDT

## Howto use/integrate
For integration you should use the library simply as a normal maven or gradle dependency.


### Maven

```xml
<!-- https://mvnrepository.com/artifact/de.jcup.eclipse.commons/eclipse-commons -->
<dependency>
    <groupId>de.jcup.eclipse.commons</groupId>
    <artifactId>eclipse-commons</artifactId>
    <version>1.1.1</version>
</dependency>


```

### Gradle
```gradle
// https://mvnrepository.com/artifact/de.jcup.eclipse.commons/eclipse-commons
implementation("de.jcup.eclipse.commons:eclipse-commons:1.1.1")

```

## Howto build with maven

```
./mvn clean verify
```

