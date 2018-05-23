---
title: 使用ansible和jenkins快速搭建VPS
description: 使用ansible和jenkins快速搭建VPS
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2018-05-21
date-modified: 2018-05-21
date-published: 2018-05-21
headline:
in-language: zh-cn
keywords: jenkins devops
draft: true

---

#### 使用ansible和jenkins快速搭建VPS

通常当我们想要尝试一些新技术或者实现一些自己想用的工具时，都会需要用到[VPS](https://en.wikipedia.org/wiki/Virtual_private_server)。但是VPS我觉得并不能直接使用，因为尽管只是拿来做side project，依然需要保证基本的安全性，例如禁止root远程登录，只允许通过public key登录、端口管理、对于进程的监控、以及编程语言的各种依赖环境(当然可以直接使用docker，但是docker也是需要安装的)。

以上这也准备工作我们完全可以通过[ansible](https://github.com/ansible/ansible/)完成，为什么还需要jenkins呢？我自己一直使用[digitalocean](https://www.digitalocean.com), 这里请想象一下这样一个场景，我从digitalocean的网站购买了新的VPS，然后修改一下inventory文件，在本地执行ansible-playbook把需要安装的依赖全部装好就行了。没错如果只是自己使用的确已经足够了，可是每当我需要新的VPS就不得不自己执行同样的命令，并且如果需要和其他人一起使用这个VPS时，就不得不共享一些安全相关的信息，例如：密码、私钥… 所以如果可以借助jenkins来减少重复工作，并且最低限度的限制共享安全信息，我认为将会是最理想的办法。

这篇文章的重点就是放在怎样通过ansible提供的密码保护工具vault和jenkins集成，方便的创建相对安全的VPS。

##### agenda

1. Ansible project structure 
2. Manage sensitive info using vault
3. Jenkins in docker
4. SSH agent with ansible in Jenkins 
5. Create vault file in Jenkins 
