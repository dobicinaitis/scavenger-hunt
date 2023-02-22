### API requests

Test requests via [Swagger UI](http://localhost:8080/api) or using `curl`.

#### Validate code

Validate the submitted code and get the next hint if code is correct.

**OK**

```shell
curl -X 'POST' \
  'http://localhost:8080/api/code/validate' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "code": "code5"
}'
```

```json
{
  "message": "Hint No 6",
  "progress": {
    "visual": "5/10",
    "numerical": 0.5
  },
  "isFinal": false
}
```

**NOK**

```shell
curl -X 'POST' \
  'http://localhost:8080/api/code/validate' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "code": "wrong"
}'
```

```json
{
  "timestamp": "2023-02-22T21:56:24.437+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Nope, try again 😋",
  "path": "/api/code/validate"
}
```

**Final**

```shell
curl -X 'POST' \
  'http://localhost:8080/api/code/validate' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "code": "code10"
}'
```

```json
{
  "message": "Congrats, all sweets have been found! 🎉",
  "progress": {
    "visual": "10/10",
    "numerical": 1
  },
  "isFinal": true
}
```

#### Generate codes

Generate a number of random alphanumeric codes that can be used to
configure [quest.yml](../src/main/resources/quest.yml) file.

```shell
curl http://localhost:8080/api/code/generate?count=10&length=5
```

```text
FETRS
2U2HF
MO3AB
AGXB4
1BGPT
IAMWR
DMSYL
IZVT3
3BTP3
D5AXG
```