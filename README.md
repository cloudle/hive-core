# hive

FIXME

## Getting Started

1. Start the application: `lein run`
2. Go to [localhost:8080](http://localhost:8080/) to see: `Hello World!`
3. Read your app's source code at src/hive/service.clj. Explore the docs of functions
   that define routes and responses.
4. Run your app's tests with `lein test`. Read the tests at test/hive/service_test.clj.
5. Learn more! See the [Links section below](#links).


## REPL
1. open Proto-REPL on Atom
2. run `(def dev-serv (hive.server/run-dev))`
3. Command + Shift + Alt + F to re-eval current file.
4. Server is hot updated! try it.

## Configuration

https://stackoverflow.com/questions/26619362/lein-install-datomic-peer-library-on-mac
To configure logging see config/logback.xml. By default, the app logs to stdout and logs/.
To learn more about configuring Logback, read its [documentation](http://logback.qos.ch/documentation.html).


## Developing your service

1. Start a new REPL: `lein repl`
2. Start your service in dev-mode: `(def dev-serv (run-dev))`
3. Connect your editor to the running REPL session.
   Re-evaluated code will be seen immediately in the service.

### [Docker](https://www.docker.com/) container support

1. Build an uberjar of your service: `lein uberjar`
2. Build a Docker image: `sudo docker build -t hive .`
3. Run your Docker image: `docker run -p 8080:8080 hive`

### [OSv](http://osv.io/) unikernel support with [Capstan](http://osv.io/capstan/)

1. Build and run your image: `capstan run -f "8080:8080"`

Once the image it built, it's cached.  To delete the image and build a new one:

1. `capstan rmi hive; capstan build`


## Links
* [Other examples](https://github.com/pedestal/samples)
