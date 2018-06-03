---
title: Troubleshooting for using logstash feeds geolocation data to Elasticsearch 
description: Elasticsearch PostgreSQL integration
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2018-06-02
date-modified: 2018-06-02
date-published: 2018-06-02
headline:
in-language: en
keywords: Elasticsearch 
draft: false

---

####Troubleshooting for using logstash feeds geolocation data to Elasticsearch

Around 4 years ago I started to use mongodb for my place search side project, actually I prefer to 2d geolocation search feature of mongodb and mongodb is very good, but when I want to provide fulltext search as well, seems mongo is not good at fulltext search especially multiple language supporting. So I was thought migrate to elasticsearch solution...emm... yes my another tech debt.

The idea is quite simple which is store all places info as meta data in postgresql and then through logstash synchronize to elasticsearch. Elasticsearch will be readonly datasource and be provided geolocation & fulltext search. 

[Logstash](https://www.elastic.co/products/logstash) is really powerful & easy tool to synchronize data to elasticsearch. We only need to define a configuration file to tell logstash where is input & output then it can work alone. Basically logstash will be mapping all database table fields to elasticsearch.  But since I alreay have migrated data from mongodb to postgresql so lantitude & longitude are normal columns in my database, it's not correct geo_point type in elasticsearch. Fortunately logstash has **filter** feature can be defined with input&output in configuration file. 

The basic feature of **filter** is for converting field data format, creating new field and remove unnecessary fields. For my case I need to convert latitude & longitude columns to geo_point type of elasticsearch and remove location_lat & location_lng fields. Acutally I was stuck on one issue which is before feeding data to elasticsearch we have to create index first otherwise elasticsearch will not handle location as geo_pooint.

create index payload data sample:

```json
{
    "settings":{
        "number_of_shards":1
    },
    "mappings":{
        "test":{
            "properties":{             
                "location":{
                    "type":"geo_point"
                }
            }
        }
    }
}
```

logstash filter sample:

```conf
filter {
    mutate {
        add_field => { "[location][lat]" => "%{location_lat}" }
        add_field => { "[location][lon]" => "%{location_lng}" }
        remove_field => ["location_lat", "location_lng"]
    }
    mutate {
        convert => {
            "[location][lat]" => "float"
            "[location][lon]" => "float"
        }
    }
}
```

 geo_point data strucutre:

```json
{
    "location": {
        "lat": 42.286681,
        "lon": 119.000181
    }
}
```

Elasticsearch allows latitude between -90 ~ 90, longitude between -180 ~ 180 see ([Geohash](https://en.wikipedia.org/wiki/Geohash)) definition. So if you have geolocation data is not in this range should think about your data maybe is wrong. I was stuck in this issue with lots of error like `java.lang.IllegalArgumentException: illegal latitude value [-95.66912769999999]` . 

Finally I can query geolocation:

```curl
curl -X GET "localhost:9200/places/_search" -H 'Content-Type: application/json' -d'
{
    "query": {
        "bool" : {
            "must" : {
                "match_all" : {}
            },
            "filter" : {
                "geo_distance" : {
                    "distance" : "2km",
                    "location" : {
                        "lat" : 42.285559,
                        "lon" : 119.004589
                    }
                }
            }
        }
    }
}
'
```

My last quirement from elasticsearch is fulltext search at least supporting english and chinese. I googled a lots of articals how to support multiple anaylzer for different langugage in one field but seems those articles are not work for **6.2.4** version. So I want to record instruction hope can help people who wants to do the same thing.

For supporting chinese I follow the suggestion to use [IK Analysis](https://github.com/medcl/elasticsearch-analysis-ik) . To install IK analysis plugin we can use `./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.2.4/elasticsearch-analysis-ik-6.2.4.zip` command to install in elasticsearch. After that we need to create index to support multiple analyzer. E.g for city field we want to chinese uses ik analyzer and english uses english analyzer.

```
{
    "settings":{
        "number_of_shards":1
    },
    "mappings":{
        "place":{
            "properties":{
                "city":{
                    "type":"text",
                    "fields":{
                        "cn":{
                            "type":"text",
                            "analyzer":"ik_smart"
                        },
                        "en":{
                            "type":"text",
                            "analyzer":"english"
                        }
                    }
                }
            }
        }
    }
}
```

Right now we have created index for one field supporting multiple analyzer so we can use **multi_match** search feature to verify whether it's working.

```
curl -X POST "localhost:9200/places/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "multi_match": {
      "type":     "most_fields", 
      "query":    "天津大连",
      "fields": [ "city" ]
    }
  }
}
'
```

We also can use **_analyze** API to verify whether the analyzer is working.

~~~
curl -X GET "localhost:9200/places/_analyze" -H 'Content-Type: application/json' -d'
{
  "tokenizer": "ik_smart",
  "text":     "天津大连"
}
'
~~~

Conclusion:

This is a very nice weekend for me because I pay back my another tech debt :fist_oncoming: , moving forward still need to think how to put everthing to jenkins maybe use docker. Hope it's not another debt :joy: 