# GitLab setup

## Kubernetes integration

Connect a Kubernetes cluster to GitLab to deploy the app via CI/CD pipelines.

### Create an agent configuration file

Create a `config.yaml` file under `.gitlab/agents/gitlab-agent/`

```shell
mkdir -p .gitlab/agents/gitlab-agent/
```

Contents:

```shell
vim .gitlab/agents/gitlab-agent/config.yaml
```

```yaml
ci_access:
  projects:
    - id: dobicinaitis/scavenger-hunt
      default-namespace: scavenger-hunt
```

Push it upstream

```shell
git add .gitlab/agents/gitlab-agent/config.yaml
git commit -m "Added gitlab-agent config"
git push
```

### Setup integration

Navigate to `Infrastructure > Kubernetes clusters` in GitLab, then press `Connect a cluster (agent)`.

Pick `gitlab-agent` and get the installation instructions. Change the namespace to the one used by the app and execute
the helm install.
> Change namespace from GitLab prompt to `scavenger-hunt`.

```shell
helm repo add gitlab https://charts.gitlab.io
helm repo update
helm upgrade --install gitlab-agent gitlab/gitlab-agent \
    --namespace scavenger-hunt \
    --create-namespace \
    --set image.tag=v15.2.0 \
    --set config.token=TOKEN_GOES_HERE \
    --set config.kasAddress=wss://kas.gitlab.com
```

A gitlab-agent pod should appear

```shell
kubectl get pods
```

## CI/CD pipelines

Commits to `main` will trigger a pipeline that builds a docker image with tag `latest` and deploys it to the Kubernetes
cluster.

Check out the [.gitlab-ci.yml](../.gitlab-ci.yml) file for details.
