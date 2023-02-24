## Kubernetes setup

### Namespace

Namespaces provide a mechanism for isolating groups of resources within a single cluster. \
Let’s create a dedicated namespace for this application.

**namespace.yaml**

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: scavenger-hunt
```

```shell
kubectl apply -f namespace.yaml
```

Now we can set it as the default namespace to avoid having to specify it every time using when using `kubectl`.

```shell
# set default namespace
kubectl config set-context $(kubectl config current-context) --namespace=scavenger-hunt
```

### Docker registry

First setup a [GitLab access token](https://gitlab.com/profile/personal_access_tokens) with read registry rights.
Then create a secret in Kubernetes, so it could access GitLab's container registry:

```shell
kubectl create secret docker-registry gitlab-registry \
    --docker-username=DOCKER_USERNAME \
    --docker-password=DOCKER_PASSWORD \
    --docker-email=DOCKER_EMAIL \
    --docker-server=DOCKER_SERVER
```

### Deployment

A Deployment provides declarative updates for [Pods](https://kubernetes.io/docs/concepts/workloads/pods/)
and [ReplicaSets](https://kubernetes.io/docs/concepts/workloads/controllers/replicaset/).

This will spin up 1 application pod instance in Kubernetes.

```shell
# if no docker images have been build via pipelines yet, then push one manually:
docker build -t registry.gitlab.com/dobicinaitis/scavenger-hunt:latest .
docker push registry.gitlab.com/dobicinaitis/scavenger-hunt:latest
```

**deployment-production.yaml**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: production-scavenger-hunt-deployment
  namespace: scavenger-hunt
  labels:
    app: scavenger-hunt
spec:
  replicas: 1
  selector:
    matchLabels:
      app: scavenger-hunt
  template:
    metadata:
      labels:
        environment: production
        app: scavenger-hunt
    spec:
      containers:
        - name: scavenger-hunt
          image: registry.gitlab.com/dobicinaitis/scavenger-hunt:latest
          imagePullPolicy: Always
      imagePullSecrets:
        - name: gitlab-registry
```

```shell
kubectl apply -f deployment-production.yaml
```

At this point, you should see a pod being spun up.

```shell
$ kubectl get pods
NAME                                               READY   STATUS              RESTARTS   AGE
production-scavenger-hunt-deployment-587d6c5944-8t4m2   0/1     ContainerCreating   0          16s

# a short time after
$ kubectl get pods
NAME                                               READY   STATUS    RESTARTS   AGE
production-scavenger-hunt-deployment-587d6c5944-8t4m2   1/1     Running   0          61s

# view logs
kubectl logs -f production-scavenger-hunt-deployment-587d6c5944-8t4m2
```

You can use port forwarding to access the app locally via http://localhost:8080.
This is useful for verifying that it's working as expected.

```shell
kubectl port-forward production-scavenger-hunt-deployment-[tab] 8080:8080
```

### Service

Service is an abstract way to expose the application running on a set of Pods as a network service.

**service-production.yaml**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: production-scavenger-hunt-service
  namespace: scavenger-hunt
spec:
  selector:
    environment: production
    app: scavenger-hunt
  ports:
    - name: http
      protocol: TCP
      port: 80
      targetPort: 8080
```

```shell
kubectl apply -f service-production.yaml
```

### Ingress

Finally, we can expose our service to the outside world. Setting up ingress will make the app accessible via 
https://your-awesome-domain.com and use a free Let's Encrypt SSL/TLS certificate that will be renewed auto-magically 
([setup guide](https://www.digitalocean.com/community/tutorials/how-to-set-up-an-nginx-ingress-with-cert-manager-on-digitalocean-kubernetes)).  
The HTTPS traffic will be forwarded to the internal HTTP port of our service.

**ingress.yaml**
> Replace FQDN with your domain name or apply `ingress.yaml` using the export, sed example below.
```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: scavenger-hunt-ingress
  namespace: scavenger-hunt
  annotations:
    kubernetes.io/ingress.class: nginx
    kubernetes.io/tls-acme: "true"
    cert-manager.io/cluster-issuer: letsencrypt-prod
    cert-manager.io/acme-challenge-type: http01
    nginx.ingress.kubernetes.io/proxy-send-timeout: "3600"
    nginx.ingress.kubernetes.io/proxy-read-timeout: "3600"
spec:
  tls:
    - hosts:
        - FQDN
      secretName: letsencrypt-prod
  backend:
    serviceName: production-scavenger-hunt-service
    servicePort: 80
  rules:
    - host: FQDN
      http:
        paths:
          - backend:
              serviceName: production-scavenger-hunt-service
              servicePort: 80
```

> Nore: A DNS "A" record forwarding the domain/subdomain to Kubernetes load balancer needs to be configured prior to executing the next step.

```shell
export FQDN=your-awesome-domain.com
sed "s/FQDN/$FQDN/g" ingress.yaml | kubectl apply -f -
```
Check status of certificate issuance:
```shell
kubectl describe certificates letsencrypt-prod
```

Cross your fingers, navigate to your domain and see if the app is loaded and a valid SSL certificate is being used.


### Integration with GitLab

See [GitLab setup](gitlab-setup.md) for details on setting up GitLab CI/CD with Kubernetes.