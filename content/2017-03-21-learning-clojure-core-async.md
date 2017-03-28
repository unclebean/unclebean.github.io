---
title: Handle multiple http requests with core.async part-1
description: larning clojure core.async library to understand clojure way handle concurrency.
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2017-03-21
date-modified: 2017-03-21
date-published: 
headline:
in-language: en
keywords: clojure
draft: false
---

#### Handle multiple http requests with core.async part-1

I'm starting to learn core.async library. It's very powerful asynchronized library for clojure. I can find a lot of very good articles for it. So my post only wnat to talk about a scenario:
>read URL list file, one by one to request each URL, once the request complete we put the response in the channel for next step processing.

My doubt is we should use only one channel for all response or we should create multi channels for each response. Acutally I also read [Seven Concurrency Models in Seven Weeks](https://pragprog.com/book/pb7con/seven-concurrency-models-in-seven-weeks) The question from this book, the author mentioned a function to handle wikipedia xml dump. 

To answer myself I implemented the ways & mintor performance from [VirtualVM](https://visualvm.java.net) 

>I will use some basic functions from core.async to implement feature.

>**(go)** for creation block to handle parking "read" & "write" message in channel.

>**(go-loop)** each go block only can handle one times read or write. so core.async provides go-loop marco to run a loop for multi times message reading or writing.

>**<!** read message from channel, it will not block running thread.

>**>!** write message to channel with non blocking.

>**put!** write message to channel, put! function no need work in go block.

**single channel version**

```
(ns playasync.core
  (:require [clojure.core.async :as async
             :refer [>! <! >!! <!! go chan buffer close! alts!! go-loop merge]]
            [playasync.http-call :as http-call]
            [clojure.java.io :as io]
            )
  (:gen-class))

(defn -main
  [feeds-path & args]
  (with-open [feeds-io (io/reader feeds-path)]
    (let [ch (chan)
          feeds (line-seq feeds-io)]
      (doall
        (map (fn [url] (http-call/get-rss-v2 ch url)) feeds))
      (go-loop []
        (<! ch)
        (recur)))
))
```
This implementation pass a channel to **http-call/get-rss-v2** function, map function will call this function for each URL, once the request complete will use this channel and put response in it then **go-loop** will read response from the channel.

**Multi channels version**

```
(ns playasync.core
  (:require [clojure.core.async :as async
             :refer [>! <! >!! <!! go chan buffer close! alts!! go-loop merge]]
            [playasync.http-call :as http-call]
            [clojure.java.io :as io]
            )
  (:gen-class))

(defn -main
  [feeds-path & args]
  (with-open [feeds-io (io/reader feeds-path)]
    (let [feeds (line-seq feeds-io)]
      (doall
        (for [feed feeds]
          (go (<! (http-call/get-rss feed)))
          )
        )
    )
  ))
```

**http-call/get-rss** function will create individual channel for each http request then return the channel for caller, once the request complete will send message to the channel.

Let's compare two version execution time:

**version 1 single channel** 

![single channel](https://unclebean.github.io/images/clojure-async-single-channel.png)

**version 2 multiple channels**

![multiple channels](https://unclebean.github.io/images/clojure-async-single-channel.png)

For now we can see seems we should use **single channel** version for this function, but I will continue to explore the right way...





