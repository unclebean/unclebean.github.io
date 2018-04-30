---
title: Using jupyter & Plotly to create benchmark visualisation
description: Using jupyter & Plotly to create benchmark visualization, unquote splicing.
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2018-04-29
date-modified: 2018-04-30
date-published: 2018-04-29
headline:
in-language: en
keywords: jupyter plotly 
draft: false

---

####Using jupyter & Plotly to create benchmark visualisation

[中文](https://unclebean.github.io/performance-chart.html) [EN](https://unclebean.github.io/performance-chart_en.html)

Performance is important or readable & maintainable important, I think different devotee has different answer. But benchmark is an insteresting topic for every developer, because benchmark can help us getting high performance and readable either.

What's benchmark, there is explanation from [wikipedia](https://en.wikipedia.org/wiki/Benchmark_(computing)):

> In [computing](https://en.wikipedia.org/wiki/Computing), a **benchmark** is the act of running a [computer program](https://en.wikipedia.org/wiki/Computer_program), a set of programs, or other operations, in order to assess the relative **performance** of an object, normally by running a number of standard tests and trials against it.[[1\]](https://en.wikipedia.org/wiki/Benchmark_(computing)#cite_note-1) The term 'benchmark' is also mostly utilized for the purposes of elaborately designed benchmarking programs themselves.

Whatever it's for function benchmark or http server benchmark. We are looking forward to get better solution to resolve the problem. I use a bunch of benchmark tools, I'm going to write a series blog for each tool. For now I just recommand some useful tools:

- [jmh](http://openjdk.java.net/projects/code-tools/jmh/) java official benchmark tool.
- [Benchmark.js](https://benchmarkjs.com/) javascript/nodejs benchmark tool。
- [benchpress](https://github.com/angular/angular/tree/master/packages/benchpress) angular official e2e performance tool, this is my fevourite frontend performance tool, because it's not only for angular app but also can be used for other frontend app.
- [perf-tools](https://github.com/brendangregg/perf-tools) linux benchmark tool, basically for every programming language.
- [ab](https://httpd.apache.org/docs/2.4/programs/ab.html) apache http server benchmark tool。
- [hey](https://github.com/rakyll/hey) go-lang implemented http server benchmark tool。

Beside **benchpress**, all are command line tools, so we have to find a way to make benchmark data visualisation. I found out [Plotly.js](https://plot.ly/javascript/), it's a very good javascript library to generate chart. it also has a lot API to support other programming languages, such as scala, python, R…see [there](https://plot.ly/api/). I like python API, because can use with jupyter.

[installation](https://plot.ly/python/getting-started/): (assuming you already have [jupyter](http://jupyter.org))

```shell
$ pip install plotly 
```

After that you need to create a Plotly [free account](https://plot.ly/feed/), free account can store 25 charts and then you need initialize your Plotly account in jupyter with your account name and api_key(your api key is in [settings](https://plot.ly/settings/api))

```python
import plotly
plotly.tools.set_credentials_file(username='account', api_key='api_key')
```

Now we can make benchmark visualisation chart, I use an reactjs benchmark [project](https://github.com/aickin/react-16-ssr-perf/tree/new-prod-mode-15) for that showcase.

Actually [react-16-ssr-perf](https://github.com/aickin/react-16-ssr-perf/tree/new-prod-mode-15) uses different benchmark chart, it also looks very nice. But I don't know whether it's convenience as python plotly :)

```python
import plotly.plotly as py
import plotly.graph_objs as go
from plotly.graph_objs import *
'''
I only need to provide different benchmark categories, Plotly can help to generate a beauty chart for me!
'''
react15Raw = go.Bar(
    x=['NodeJS4.83', 'NodeJS6.11', 'NodeJS8.2', 'NodeJS8.3'],
    y=[241, 200, 152, 140],
    name='React 15.6.1(raw)'
)
react15Compiled = go.Bar(
    x=['NodeJS4.83', 'NodeJS6.11', 'NodeJS8.2', 'NodeJS8.3'],
    y=[125, 82, 55, 49],
    name='React 15.6.1(compiled)'
)
react16 = go.Bar(
    x=['NodeJS4.83', 'NodeJS6.11', 'NodeJS8.2', 'NodeJS8.3'],
    y=[60, 28, 17, 15],
    name='React 16'
)
data = [react15Raw, react15Compiled, react16]
layout = go.Layout(
    barmode='group'
)
fig = go.Figure(data=data, layout=layout)
py.iplot(fig, filename='grouped-bar')
```

![plot](https://unclebean.github.io/images/newplot.png)