# Scavenger Hunt

Send your friends and family on small scavenger hunts to locate sweets or other goodies.

### How it works

Hide items with some secret codes written/printed on them, define hints to their locations in
[quest.yml](src/main/resources/quest.yml) file and deploy the app somewhere reachable by the
participants.

Let participants stumble upon the first item or give them a link containing the first code to start the hunt.

Codes can be entered manually in the UI or using links, e.g. `https://find.me/{code}`.

### Screenshots

#### Default theme

![UI screenshot](doc/images/ui-example-1.png)

![UI screenshot](doc/images/ui-example-2.png)

#### Halloween theme

![UI screenshot](doc/images/ui-example-3.png)

### Quickstart

Use the prebuilt container image:

```bash
docker run --rm -p 8080:8080 registry.gitlab.com/dobicinaitis/scavenger-hunt
```

Supply your own [quest.yml](src/main/resources/quest.yml) configuration file by mounting it into the container:

```bash
docker run --rm -p 8080:8080 \
  -v ./quest.yml:/app/config/quest.yml \
  registry.gitlab.com/dobicinaitis/scavenger-hunt
```

### Local setup

* Install Java 24 or later
* Clone this repository
* Run the app `./gradlew bootRun`
* Check out the page at http://localhost:8080

### Useful:

* [Build & Run](doc/build-and-run.md)
* [API examples](doc/api-call-examples.md)

### Library docs:

* [Materialize](https://materializecss.com)
* [ProgressBar.js](https://kimmobrunfeldt.github.io/progressbar.js)
* [TypewriterJS](https://safi.me.uk/typewriterjs)
