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

ecto是[Elixir](https://elixir-lang.org)用来与数据库交互的DSL实现，在实际使用中是用来提供数据访问层模块的。由于ecto对众多数据库的支持都很好

[一篇介绍ecto很好的文章](https://www.toptal.com/elixir/meet-ecto-database-wrapper-for-elixir)

1. 创建项目

   mix new cart —sup （sup 需要supervisor）

2. 添加依赖到mix

3. 创建repo

   mix ecto.gen.repo -r ExampleApp.Repo

   mix ecto.gen.repo Cart.Repo   

4. 修改config.exc，添加repo

   mix ecto.gen.repo Cart.Repo

5. 创建数据库

   mix ecto.create

6. 添加migration文件

   mix ecto.gen.migration create_invoices

7. 