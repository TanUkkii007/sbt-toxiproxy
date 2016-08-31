# sbt-toxiproxy

toxiproxy server plugin for sbt project

## Introduction

[Toxiproxy](https://github.com/Shopify/toxiproxy) is a framework for simulating network conditions.
Toxiproxy can be used for testing your application's resilience against network failure and component failure.
sbt-toxiproxy provides features to run Toxiproxy server via sbt.
Toxiproxy Java client is available [here](https://github.com/trekawek/toxiproxy-java).

## Commands
- startToxiproxy: start Toxiproxy server
- stopToxiproxy: stop Toxiproxy server

## Settings

To enable the plugin, following line should be added to `build.sbt`

```scala
ToxiproxyPlugin.settings
```

Following settings are provided by defaults.

```scala

toxiproxyVersion := "2.0.0"

toxiproxyHost := Some("localhost")

toxiproxyPort := Some(8474)

toxiproxyDownloadDirectory := file("toxiproxy")

```

## Thanks

Most of sbt-toxiproxy's implementation is based on [sbt-dynamodb](https://github.com/grahamar/sbt-dynamodb)