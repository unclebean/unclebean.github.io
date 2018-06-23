---
title: Learning Elixir
description: learning elixir, unquote splicing.
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2017-04-01
date-modified: 2018-04-01
date-published: 2018-04-01
headline:
in-language: zh-cn
keywords: elixir
draft: true
---

#### Elixir in practice

  I used long weekend to write a tiny elixir project to parse log doing analytics job. I remembered that I wrote same using java, golang, python, js to do the same job. I have to say elixir is comfortably. I really enjoy to use elixir. 

  There are some features I really like from elixir

    1. case pattern match
    2. Enum functions e.g map, reduce, filter
    3. function pipeline.
    4. write unit test is easy.
    5. gen server & supervisor 
    6. marco 

#### Create elixir project

  mix new project-name

  mix archive.uninstall phx_new

  mix archive.install https://github.com/phoenixframework/archives/raw/master/phx_new.ez

  mix phoenix.new rumbl

  mix phoenix.server

  iex -S mix

  mix test

  iex> recompile()

  mix deps.get && mix deps.compile

  mix ecto.gen.repo

### Useful link

[10 Killer Elixir Tips](https://medium.com/blackode/10-killer-elixir-tips-2-c5f87f8a70c8)

###Exercism

1. nucleotide-count

   This question want to tell us how to use **Enum.count** & how to put item into a amp, especially when we need to iterator a list and put every item of list to a map we should use **Enum.reduce** to put every item to a **%{}**, the reason is that Map is immutable data in elixir, when we use **Map.put** value to a map will create a new map.  

2. secret-handshake

   The question recommands using [Bitwise](https://hexdocs.pm/elixir/Bitwise.html) to check whether input code matching binary key. I jumped into a misunderstanding which is I think **&&&** left and right should pass binary value... acutally we should provide dicemal value to verify input code. Another thing I was learned from the question is we should not change variable inside **case** expression

3. rotational-cipher

   This is very interesting question, I learnt a lot from this question. **Char** in Elixir is ?Char e.g: ?A means ***char A***. Any String can be canverted to char list through **String.to_charlist**. We can initial Number list using range e.g: ***Enum.to_list(1..10)***. If we can visit item inside list we can use ***Enum.at([], index)***

4. strain

   Hight Order function pass should with "&", e.g:**&is_even?/1** and calling should be **fun.()**.