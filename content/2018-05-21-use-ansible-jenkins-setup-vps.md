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
draft: false

---

#### 使用ansible和jenkins快速搭建VPS

通常当我们想要尝试一些新技术或者实现一些自己想用的工具时，我喜欢拥有更多控制权限的[VPS](https://en.wikipedia.org/wiki/Virtual_private_server)部署应用。但是VPS我觉得并不能直接使用，因为尽管只是拿来做side project，依然需要保证基本的安全性，例如禁止root远程登录，只允许通过public key登录、端口管理、对于进程的监控、以及编程语言的各种依赖环境(当然可以直接使用docker，但是docker也是需要安装的)。

以上这些准备工作我们完全可以通过[ansible](https://github.com/ansible/ansible/)完成，为什么还需要jenkins呢？我一直使用[digitalocean](https://www.digitalocean.com), 这里请想象一下这样一个场景，我从digitalocean的网站购买了新的VPS，然后修改一下inventory文件，在本地执行ansible-playbook把需要安装的依赖全部装好就行了。没错如果只是自己使用的确已经足够了，可是每当我需要新的VPS就不得不自己执行同样的命令，并且如果需要和其他人一起使用这个VPS时，就不得不共享一些安全相关的信息，例如：密码、私钥… 所以如果可以借助jenkins来减少重复工作，并且最低限度的限制共享安全信息，我认为将会是最理想的办法。

这篇文章的重点就是放在怎样通过ansible提供的密码保护工具vault和jenkins集成，方便的创建相对安全的VPS。关于ansible的项目结构，请看这篇介绍。

[ansible vault](https://docs.ansible.com/ansible/2.4/vault.html)是ansible提供的用来管理密码和其它敏感数据的工具，通过**ansible-vault create file.yml**可以将密码或其它敏感数据保存在加密的文件里，这样就不必担心密码以明文的形式存储在配置文件里的问题了。在执行ansible-playbook时可以用**—ask-vault-pass**参数输入创建加密文件时输入的密码。但是在jenkins上执行时是没有办法输入vault密码的。虽然我们可以将vault密码保存在另一个文件中并通过**—vault-password-file**免密码输入，但是这样提交vault密码文件到github上一样等于暴露了敏感数据，因为任何人都可以通过**ansible-vault edit file.yml**输入vault密码来获取敏感数据。好在jenkins有[file operations plugin](https://plugins.jenkins.io/file-operations)可以在workplace里面创建vault password文件。这样就不需要将vault密码文件提交到branch了。

![ansible-vault gift](https://unclebean.github.io/images/ansible-vault.gif)

上面已经介绍了怎么用ansible-vault创建加密文件来保护敏感数据，在执行ansible-playbook时就可以使用之前创建的file.yml文件来传递password到ansible的任何task里。在下面的命令中**vault.txt**文件里面保存的是创建file.yml时输入的密码。**valut.txt**不可以提交到branch里面，因为那样等于暴露了加密的file.yml文件的内容。

```shell
#!/bin/bash
ansible-playbook ./playbooks/main.yml -i ./production/inventory -u root --extra-vars '@file.yml' --vault-password-file=vault.txt
```

所以在使用jenkins时需要使用到file operations，在Build Step里面创建vault.txt,并且在执行完jenkins build之后删除vault.txt。这样就可以非常安全的自动安装VPS需要的所有依赖程序，而不是每次都需要手动执行ansible命令了。

![valut file](https://unclebean.github.io/images/jenkins-file-operation.png)