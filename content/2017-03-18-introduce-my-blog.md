---
title: Introduce my new blog
description: how to create this blog - write article & diliver to github pages.
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2017-03-18
date-modified: 2017-03-18
date-published: 
headline:
in-language: en
keywords: design
draft: false
---

####Introduce

This is my new blog in github pages. Actually I don't often wtrite article to record my research, The beginning 2017 I start to learn clojure, I plan to use clojure to do something - web app, game etc. My first trying is this blog - based on [perun](https://github.com/hashobject/perun). Perun is very good static html generate tool. So far my blog only has 3 main views (home page, post page, tags page). Perun provides features which as lone I write markdown file he can generate html page & collect tags based on keyword of the markdonw. I will write another post to introduce how to write simple clojure to render html.

####Logo

![alt uncle bean's blog](https://unclebean.github.io/images/ub_blog.svg)

This is my prefered logo which getting from [markmaker](http://emblemmatic.org/markmaker/#/designs). But somehow [markmaker](http://emblemmatic.org/markmaker/#/designs) generated svg always 300px width & 300px height has whitespace, so I have to change the svg size to match the logo size manually. 

####UI

So far the UI is still draft, all css define in one big less file. It's not good. I would like to try some UI good to read article .

####Comments

Right now I follow the perun way to integrate [disqus](https://disqus.com) for comments feature. Unfortunately [disqus](https://disqus.com) can't work in my country :(

####Todo List
Right now I can write markdonw to easilly create post. I will move  my notes from Notational Velocity to blog. I will continue to enhance my blog to provide more feature & try my idea... There are some idea:

1. email subscription (I like this feature I can push the good articles which I read or myself...)
2. loading link inside post (Write post always need to reference some links in the post, open the link in another tab is not convenience. So I plan to use iframe to load link inside the post)
3. share post to twitter, weibo, wechat etc...