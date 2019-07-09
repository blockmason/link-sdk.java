# Blockmason Link SDK for Java

[![CircleCI][1]][2]

Interact with your Blockmason Link projects from Java environments,
including Android.

## Installing

These instructions assume your project is using [Gradle][4]. The same
general steps also apply if your project uses [Maven][5].

### Add the Blockmason repository

Add the following snippet to your project's **build.gradle**:

```groovy
repositories {
  maven {
    url 'https://maven.blockmason.io'
  }
}
```

> 💡 **Note:** If you already have a `repositories` block, just add
> the `maven` block in the above snippet to it.

### Add the dependency

Add the following snippet to your project's **build.gradle**:

```groovy
dependencies {
  implementation 'io.blockmason:link-sdk:0.1.0'
}
```

> 💡 **Note:** If you already have a `dependencies` block (you probably
> do), just add the `implementation` line in the above snippet to it.

## Usage

First, your app should import the `io.blockmason.link.Project` class:

```
import io.blockmason.link.Project;
```

Use the `Project` class to initialize your project like this:

```
Project project = Project.at("<your-client-id>", "<your-client-secret>");
```

Use the **Client ID** and **Client Secret** provided by your Link project
to fill in the respective values above.

Then, you can use the `project` object to make requests against your
Link project.

For example, assuming your project has a **GET /echo** endpoint that
expects a `message` input and responds with a `message` output:

```
JSONObject inputs = new JSONObject();

inputs.put("message", "Hello, world!");

JSONObject outputs = project.get("/echo", inputs);

System.out.println(outputs.getString("message"));
// => "Hello, world!"
```

Another example, assuming your project has a **POST /mint** endpoint
that expects `to` and `amount` inputs:

```
JSONObject inputs = new JSONObject();

inputs.put("amount", 1000);
inputs.put("to", "0x1111222233334444555566667777888899990000");

project.post("/mint", inputs);
```

## Contributing

### Building

Clone this repo, then run `./gradlew build`. For more information about
building Gradle projects, see [Building Java Libraries with Gradle][3].

### Publishing

Set the `ORG_GRADLE_PROJECT_SIGNING_KEY` environment variable to the
ASCII-armored PGP private key, then run `./gradlew publish`. The
following one-liner will publish using the key ID `ABCD1234`:

```
ORG_GRADLE_PROJECT_SIGNING_KEY="$(gpg --export-secret-key --armor ABCD1234)" ./gradlew publish
```

[1]: https://circleci.com/gh/blockmason/link-sdk.java.svg?style=svg
[2]: https://circleci.com/gh/blockmason/link-sdk.java
[3]: https://guides.gradle.org/building-java-libraries/
[4]: https://gradle.org/
[5]: https://maven.apache.org/
