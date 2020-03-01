---
title: My side project CI/CD setup 
description: lovely CircleCI 
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2020-02-29
date-modified: 2020-02-29
date-published: 2020-02-29
headline:
in-language: en
keywords: CI/CD 
draft: false 

---

####My Side project CI/CD setup

> Few years back, I started to use Jenkins as my main CI/CD tool. My all side projects are running on it.
>
> Since I would like to try different program languages, so I need many different program languages dependencies with my jenkins server. As of now is a little difficult to maintain a jenkins server by myself. So I look for an alternative tool.

I choice CircleCI has no more reason, I don't compare with other tools. Because CircleCI can match my all requirements and it's very frindly for open source project.

So what's my requirments?

1. Can it support multi program languages?
2. Can I easily install dependencies and run unit test?
3. Can I deploy to my digital ocean server?

*So how does CircleCI to support multi program languages?*

CircleCI has a supported program languages [list](https://circleci.com/docs/2.0/demo-apps/#section=welcome). To choice the puticular program language running on CircleCI is very easy, it has a [Orbs Concept](https://circleci.com/docs/2.0/using-orbs/). For example if your project is python project then you just 

need to use python orbs.

```yaml
orbs:
  python: circleci/python@0.2.1
```

*For installing dependencies for the project*

In my current side project(a python project), I have couple of dependencies need to compile from the running environment. So I'm using docker to prepare third party libraries. In CircleCI can use [remote docker](https://circleci.com/docs/2.0/building-docker-images/) build environment to build your docker image and push the image to docker hub. Just needs to add remote docker in job steps:

```yaml
- setup_remote_docker:
    version: 18.06.0-ce
```

*After build project then how to test it?*

Since I use docker, once I build a new docker image just need to add a test step into job then run container in interactive mode to run test. **run_test** is my shell case. 

```yaml
- run:
    command: docker run -it [image:tag] run_test
    name: run test
```

*Deployment to server*

I'm using DigitalOcean droplets running my all side projects. CircleCI can maintains all sensitive data in build settings as variables so I no need to put within project (that's the main reason why I use my private jenkins). But seems CircleCI runs ssh to remote server is not quite convenience, only can execute existing shell on that server, can't like jenkins the put shell in jenkins. There is the example:

```yaml
 - run:
     name: Deploy master.
     command: if [ "${CIRCLE_BRANCH}" == "master" ]; then ssh ${user}@${server} 'cd /home/deploy/deploy.sh'; else echo "Skipped"; fi

```

So far CircleCI is good enough for me and the free plan can build 2500 times per week. I already migrate 2 projects from Jenkins to CircleCI, not sure whether has any tool can do auto migration. I have one [ansible devops project](https://github.com/unclebean/init-env) to setup linux server, when I want to create new digitalocean droplets I just need to add ip within my devops project it will auto initial all software on that server. I will migrate this project later should be interesting, if it's still working I can save 10$ per month 🥰.

