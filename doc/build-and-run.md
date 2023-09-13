## Build & Run locally

### JAR

#### Build

```shell
./gradlew clean build
```

#### Run

```shell
java -jar build/libs/scavenger-hunt-*.jar
```

### Docker image

#### Build

```shell
./gradlew clean build
docker build -t scavenger-hunt:latest .

# docker buildx create --name builder --use
# docker buildx build --platform linux/amd64,linux/arm64/v8 -t registry.gitlab.com/dobicinaitis/scavenger-hunt:latest .
# docker push registry.gitlab.com/dobicinaitis/scavenger-hunt:latest
```

#### Run

```shell
docker run -it --rm -p 8080:8080 --name scavenger-hunt scavenger-hunt:latest

# docker run -it --rm --name scavenger-hunt registry.gitlab.com/dobicinaitis/scavenger-hunt:latest
```
