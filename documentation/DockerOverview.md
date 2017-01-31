# Docker 
---
Docker is an open platform for developing, shipping, and running applications. Docker enables you to separate your applications from your infrastructure. Also, it provides the ability to package and run an application in a loosely isolated environment called a container.

#### Docker Engine
Docker Engine is a client-server application with these major components:
 - A server which is a type of long-running program called a daemon process.
 - A REST API which specifies interfaces that programs can use to talk to the daemon and instruct it what to do.
 - A command line interface (CLI) client.
 
Docker can streamline the development lifecycle by allowing developers to work in standardized environments using local containers which provide your applications and services. Docker containers can run on a developer’s local host, on physical or virtual machines in a data center, in the Cloud, or in a mixture of environments.

#### Docker’s architecture
Docker uses a client-server architecture. The Docker client talks to the Docker daemon, which does the heavy lifting of building, running, and distributing your Docker containers. The Docker client and daemon can run on the same system, or you can connect a Docker client to a remote Docker daemon. The Docker client and daemon communicate using a REST API, over UNIX sockets or a network interface.

#### Install
To install Docker, you need the 64-bit version of one of these Ubuntu versions:
- Yakkety 16.10
- Xenial 16.04 (LTS)
- Trusty 14.04 (LTS)

```
$ sudo apt-get -y install docker-engine
```
Use following command to start Docker daemon:
```
$ sudo service docker start
```
Use following command to stop Docker daemon:
```
$ sudo service docker stop
```
Use following command to view Docker's status :
```
$ sudo service docker status
```
#### The Docker daemon
The background service running on the host that manages building, running and distributing Docker containers. The daemon is the process that runs in the operation system to which clients talk to.

##### Docker daemon configuration
The Docker daemon can listen for Docker Remote API requests via three different types of Socket: unix, tcp, and fd.
By default, a unix domain socket (or IPC socket) is created at /var/run/docker.sock, requiring either root permission, or docker group membership.

If you need to access the Docker daemon remotely, you need to enable the tcp Socket. Beware that the default setup provides un-encrypted and un-authenticated direct access to the Docker daemon - and it should be secured either using the built-in HTTPS encrypted socket, or by putting a secure web proxy in front of it. You can listen on port 2375 on all network interfaces with -H tcp://0.0.0.0:2375, or on a particular network interface using its IP address: -H tcp://192.168.59.103:2375. It is conventional to use port 2375 for un-encrypted, and port 2376 for encrypted communication with the daemon. 

One approach to enable docker daemon remote access is to change content of file /etc/default/docker, by adding/uncommenting DOCKER_OPTS="-H tcp://127.0.0.1:2375 -H unix:///var/run/docker.sock". 


#### The Docker client
The Docker client, in the form of the docker binary, is the primary user interface to Docker. It accepts commands and configuration flags from the user and communicates with a Docker daemon. One client can even communicate with multiple unrelated daemons.
There are several docker clients written in Java. One of them is Spotify.
##### Spotify

  `Download:` 
  ```sh
<dependency>
    <groupId>com.spotify</groupId>
    <artifactId>docker-client</artifactId>
    <version>LATEST-VERSION</version>
</dependency>
```

#### Docker images
A Docker image is a read-only template with instructions for creating a Docker container. 
You can build or update images from scratch or download and use images created by others. An image can be based on, or may extend, one or more other images. A docker image is described in text file called a Dockerfile, which has a simple, well-defined syntax.

Use following command to create docker image based on specific Dockerfile :

```
$ sudo docker build -t nameOfImage .
```

Use following command to remove one or more images:
```
$ sudo docker rmi IMAGE [IMAGE...]
```
The docker build command builds an image from a Dockerfile and a context. The build’s context is the files at a specified location PATH or URL. The PATH is a directory on your local filesystem.

#### Dockerfile
A Dockerfile is a simple text-file that contains a list of commands that the Docker client calls while creating an image. Commands written in a Dockerfile are almost identical to their equivalent Linux commands.
Before the Docker daemon runs the instructions in the Dockerfile, it performs a preliminary validation of the Dockerfile and returns an error if the syntax is incorrect.
The Docker daemon runs the instructions in the Dockerfile one-by-one, committing the result of each instruction to a new image if necessary, before finally outputting the ID of your new image.

##### Format
Here is the format of the Dockerfile:
```
# Comment
INSTRUCTION arguments
```

The instruction is not case-sensitive. However, convention is for them to be UPPERCASE to distinguish them from arguments more easily.
Docker runs instructions in a Dockerfile in order. The first instruction must be `FROM` in order to specify the Base Image from which you are building.
Docker treats lines that begin with # as a comment, unless the line is a valid parser directive. 

#### Docker containers
A Docker container is a runnable instance of a Docker image. You can run, start, stop, move, or delete a container using Docker API or CLI commands. When you run a container, you can provide configuration metadata such as networking information or environment variables. Each container is an isolated and secure application platform, but can be given access to resources running in a different host or container, as well as persistent storage or databases.

Use following command to run a Docker container based on specific image:
```
$ sudo docker run nameOfImage
```
Use following command to view all currently running containers:
```
$ sudo docker ps
```
Use following command to view all containers:
```
$ sudo docker ps -a
```
Use following command to remove one or more containers:
```
$ sudo docker rm CONTAINER [CONTAINER...]
```
Use following command to stop one or more running containers:
```
$ sudo docker stop CONTAINER [CONTAINER...]
```
Use following command to return low-level information on a container, image or task.
```
$ sudo docker inspect  CONTAINER|IMAGE|TASK [CONTAINER|IMAGE|TASK...]
```

---
#### MySQL container - creating custom database from Docker MySQL image
In order to create MYSQL container with default database from specific .sql file, following Dockerfile can be used:

```
FROM mysql:5.6

ENV MYSQL_ROOT_PASSWORD=root

ADD initDatabase.sql /docker-entrypoint-initdb.d
```
The FROM keyword tells Docker which image your image is based on.
The ENV keyword sets the environment variables.
MYSQL_ROOT_PASSWORD is mandatory variable and specifies the password that will be set for the MySQL root superuser account.
ADD command to adds your schema file to the /docker-entrypoint-initdb.d directory in the Docker container. The `docker-entrypoint.sh` file will run any files in this directory ending with ".sql" against the MySQL database.
In this particular example, initDatabase.sql must be placed in the same folder as Dockerfile.

Reference documentation:
- [Docker overview](https://docs.docker.com/engine/understanding-docker)
- [Understand images, containers, and storage drivers](https://docs.docker.com/engine/userguide/storagedriver/imagesandcontainers/)
- [Docker daemon](https://docs.docker.com/engine/reference/commandline/dockerd/)
- [Docker for beginners](https://prakhar.me/docker-curriculum/)
- [Docker basics](https://severalnines.com/blog/mysql-docker-containers-understanding-basics)




#### Integration testing with Docker
Using docker with integration testing gives us ability to work in completely isolated environment, and to achieve high portability.
Unlike virtual machines, Docker containers do not require an entire operating system, all required libraries and the actual application binaries. The same Linux kernel and libraries can be shared between multiple containers running on the host. Docker makes it easy to package Linux software in self-contained images, where all software dependencies are bundled and deployed in a repeatable manner. An image will have exactly the same software installed, whether we run it on a laptop or on a server. The key benefit of Docker is that it allows users to package an application with all of its dependencies into a standardized unit (container). As you can see Docker also saves us a lot of time with its ability to create and destroy containers, without having to mess with whole OS.

Now that we have custom MySQL container, we can do integration testing.
Each test should connect to a fresh container, with the DB in the same state. It should be able to read and write, but all changes should be lost when the test ends and the container is destroyed. We get a completely clean MySQL server for every test. This completely isolates our test cases so that no tests can influence each other. We test against the same version we have in production

 `Example using Spotify:`
 
 *Before Test*
 ```sh
   DockerClient docker = DefaultDockerClient.builder().uri("http://127.0.0.1:2375").build();
   ContainerConfig containerConfig = ContainerConfig.builder().image("persons").build();
   ContainerCreation container = docker.createContainer(containerConfig);
   String containerId = container.id();
   docker.startContainer(containerId);
 ```
 *After Test*
 ```
   dockerClient.stopContainer(containerId, 0);
   dockerClient.removeContainer(containerId);
   dockerClient.close();
```

