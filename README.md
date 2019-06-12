# Blockmason Link SDK for Java

[![CircleCI][1]][2]

Interact with your Blockmason Link projects from Java environments,
including Android.

## Installing

To add this library to your app, do one of the following:

> **TODO:** List the different ways of installing the package.

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

Clone this repo, then run `make` to install dependencies, compile the
sources and tests, run the tests, and create a package archive for
distribution or inclusion in another project.

[1]: https://circleci.com/gh/blockmason/link-sdk.java.svg?style=svg
[2]: https://circleci.com/gh/blockmason/link-sdk.java
