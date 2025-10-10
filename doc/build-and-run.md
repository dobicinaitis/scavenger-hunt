# Build & Run

## JAR

```shell
./gradlew clean build
java -jar build/libs/scavenger-hunt-*.jar
```

To run the app with a custom config, create a `./config/quest.yml` file
and populate it with your [quest](../src/main/resources/quest.yml) details.

## Docker

```shell
docker build -t scavenger-hunt:local .
docker run -it --rm -p 8080:80 scavenger-hunt:local

# run with a custom config file
docker run -it --rm -p 8080:80 \
  -v ./config/quest.yml:/app/config/quest.yml \
  scavenger-hunt:local
```

Push an image to GitLab registry:

```bash
docker buildx create --name builder --use
docker buildx build --push --platform linux/amd64,linux/arm64/v8 -t registry.gitlab.com/dobicinaitis/scavenger-hunt:latest-dev .

docker run -it --rm  -p 8080:80 registry.gitlab.com/dobicinaitis/scavenger-hunt
```

## Kubernetes

### Local chart

```bash
# push the container image to the local registry
docker tag scavenger-hunt:local localhost:32000/scavenger-hunt:local
docker push localhost:32000/scavenger-hunt:local

# deploy using Helm
helm upgrade --install scavenger-hunt chart \
    --set image.repository=localhost:32000/scavenger-hunt \
    --set image.tag=local \
    --set ingress.enabled=true

# uninstall
helm uninstall scavenger-hunt
```

Head over to http://scavenger-hunt.localhost to access the web interface.

### Chart from GitLab registry

**Log `helm` into the registry**

```bash
helm registry login -u <gitlab-username> -p "<access-token>" registry.gitlab.com
```

**Deploy**

```bash
helm upgrade --install scavenger-hunt \
    --set ingress.enabled=true \
    --set imagePullSecrets[0].name=gitlab-registry-secret \
    --set imagePullSecrets[0].registry=registry.gitlab.com \
    --set imagePullSecrets[0].username=<username> \
    --set imagePullSecrets[0].password=<password> \
    --set imagePullSecrets[0].email=<email> \
    oci://registry.gitlab.com/dobicinaitis/scavenger-hunt/charts/scavenger-hunt --version <version>
```

Head over to http://scavenger-hunt.localhost to access the web interface.