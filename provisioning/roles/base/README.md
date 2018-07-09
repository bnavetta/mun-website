# Base

This role does basic configuration needed by most other roles:

* Creates the `mun-net` Docker network. All of our containers use this, and not the default Docker bridge network,
  because containers on user-defined bridge networks can automatically communicate with each other and are
  isolated from the outside world. Check out Docker's
  [bridge network documentation](https://docs.docker.com/network/bridge/)
* Configures the [`docker-credential-gcr`](https://github.com/GoogleCloudPlatform/docker-credential-gcr) credential
  helper so that Docker can download images from Google Cloud Registry