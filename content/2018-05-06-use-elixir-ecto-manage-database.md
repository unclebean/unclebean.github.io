---
title: 使用Elixir ecto管理数据库
description: 使用Elixir ecto管理数据库, unquote splicing.
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2018-05-06
date-modified: 2018-05-06
date-published: 2018-05-06
headline:
in-language: zh-cn
keywords: elixir ecto 
draft: true


---

#### 使用Elixir ecto管理数据库

ecto是[Elixir](https://elixir-lang.org)用来与数据库交互的DSL实现，在实际使用中是用来提供数据访问层模块的。由于ecto对众多数据库的支持都很好，

1. 创建repo

   mix ecto.gen.repo -r ExampleApp.Repo

2. ​