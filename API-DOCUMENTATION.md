# Site Package API Documentation

**API Root URL: http://localhost:8080/sp/api**

## Auth
### Users
Path: `root/auth`

#### Create User (Register)

`/register`

| Header | Type | Required |
| ------ | ---- | -------- |
| `name` | String | yes |
| `username` | String | yes |
| `password` | String | yes |
| `roleName` | String | yes |
| `isSiteAdmin` | Boolean | yes |
### Roles

## REST