(defproject hive "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :username [:env/DATOMIC_USERNAME]
                                   :password [:env/DATOMIC_PASSWORD]}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.7.1"]
                 [buddy/buddy-hashers "1.2.0"]
                 [buddy/buddy-sign "1.5.0"]
                 [com.amazonaws/aws-java-sdk-dynamodb "1.11.158"]
                 [io.pedestal/pedestal.service "0.5.2"]
                 [com.google.firebase/firebase-admin "5.3.0"]
                 [com.datomic/datomic-pro "0.9.5561.50"]
                 [expectations "2.2.0-beta1"]

                 ;; Remove this line and uncomment one of the next lines to
                 ;; use Immutant or Tomcat instead of Jetty:
                 [io.pedestal/pedestal.jetty "0.5.2"]
                 [io.pedestal/pedestal.log "0.5.1"]
                 ;; [io.pedestal/pedestal.immutant "0.5.2"]
                 ;; [io.pedestal/pedestal.tomcat "0.5.2"]
                 [com.walmartlabs/lacinia "0.18.0"]

                 [ch.qos.logback/logback-classic "1.1.8" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.22"]
                 [org.slf4j/jcl-over-slf4j "1.7.22"]
                 [org.slf4j/log4j-over-slf4j "1.7.22"]
                 [org.clojure/tools.logging "0.4.0"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; If you use HTTP/2 or ALPN, use the java-agent to pull in the correct alpn-boot dependency
  ;:java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.5"]]
  :datomic {:schemas ["resources" ["schema.edn"]]
            :config "resources/dynamo-transactor.properties"
            :db-uri "datomic:ddb://ap-northeast-1/hive-core/hive-core"}
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "hive.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.2"]]
                   :datomic {:config "resources/dev-transactor.properties"
                             :db-uri "datomic:dev://localhost:4334/hive-core"}}
             :uberjar {:aot [hive.server]}}
  :main ^{:skip-aot true} hive.server)