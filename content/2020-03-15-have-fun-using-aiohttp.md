---
title: Have fun using AIOHTTP
description: Async HTTP client/server for asyncio and Python
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2020-03-15
date-modified: 2020-03-15
date-published: 2020-03-15
headline:
in-language: en
keywords: python
draft: false 
share-image: /images/aiohttp.png

---

#### Have fun using AIOHTTP

> I need a simple web server framework to build a small app, I used to use django, it has everything even something I no need... I'm not blaming django, acutally django is a mature web framework. But I want some fun.

I used to use java: [Struts 1](https://en.wikipedia.org/wiki/Apache_Struts_1), [JSF](https://en.wikipedia.org/wiki/JavaServer_Faces), [Spring MVC](https://en.wikipedia.org/wiki/Spring_Framework), node: [express](https://github.com/expressjs/express), [koa](https://koajs.com), clojure: [compojure](https://github.com/weavejester/compojure), elixir: [phoenix](https://www.phoenixframework.org), python: [django](https://www.djangoproject.com) to build web application. Actually every framework tries to resolve the basic problem - handle request/response, manage context.

**In Request side** needs to think about how to handle request parameter & request body.

**In Response side** needs to think about the return types, html, json, xml, binary...etc

**For context** the problem becomes user's session and how to do access control via authorization & authentication.

On top of the basic problems the benifit and main reason why you decide to choice one fromwork rather than others, I think it should be whether this framework can support new concept or popular features e.g graphQL, webScoket, reactive. Support not meanning the framework must have everything, whenever I need that feature I can easily to integrate or install the library for that framework. So the framework must be small & flexible to start.

AIOHTTP is the one who is small and has plugable library on demand. 

To describe AIOHTTP for short is that it's an [Async HTTP client/server for asyncio and Python](https://docs.aiohttp.org/en/stable/)

> Since I only use AIOHTTP server part, so this post only focuse on server part.

**What's asyncio(coroutine)**

asyncio is a builtin library for python to implement coroutine. AIOHTTP is based on this core library that means every request handler function in AIOHTTP is a async fuction and we can put the task as a couroutine task in an eventloop inside the handler function. So how the request handler looks like?

```python
@subscription_routes.view("/subscribeList")
@aiohttp_jinja2.template("subscription.html")
async def subscription_list(request):
    web_session = await get_session(request)
    user = web_session.get("user", {})
    email = user.get("email", "")
    db_session = apply_session()
    subs = []
    for sub in db_session.query(Subscription).filter(Subscription.email == email).all():
        try:
            subs.append(await get_subscribed_item(sub.symbol))
        except:
            continue
    return {"subs": subs}

```

The every first two lines define request URL and response(for this example is html page).

The function body is that I get user from http session and then query user subscribed items from database after that return all subscribed items to jinja2 template for rendering html.

The important part is:

```python
await get_subscribed_item(sub.symbol)
```

**get_subscribed_item** function relies on anohter web service so I have to wait for that API call to get full subscribed item. Since this function always take long time so I create an event loop to handle coroutine task  that request will not be block each other.

There is the get_subscribed_item function:

```python
async def get_subscribed_item(symbol):
    loop = asyncio.get_event_loop()
    score, _, close_price, result = await loop.run_in_executor(None, xg_predict, symbol)
    stock = await loop.run_in_executor(None, yf.Ticker, symbol)
    stock_data = stock.history(period="1d")
    return {
        "symbol": symbol,
        "predictScore": score,
        "currentPrice": stock_data["Close"].values[0],
        "limit_up_down": round(100 * (result / close_price), 2),
    }
```

**Request and Response that's all**

As a small web framework, AIOHTTP the basic library only handle request and response. If you need response to handle html template you need to install [ aiohttp-jinja2](https://github.com/aio-libs/aiohttp-jinja2) separately. Of course only request & response can't create a web application, at least we need session and maybe DB related feature and more benefits. AIOHTTP has them but thery are all as plugin to present in AIOHTTP.

**For HTTP Session & Security?**

if you need to have session & security handler then you need to install [aiohttp-session](https://github.com/aio-libs/aiohttp-session) & [aiohttp-security](https://github.com/aio-libs/aiohttp-security)

**For DB related CRUD/schema migration**

AIOHTTP officially provide one async library for [postgresQL](https://github.com/aio-libs/aiopg) and [MySQL](https://github.com/aio-libs/aiomysql), but you still can use [SQLAlchemy](https://www.sqlalchemy.org) with any your favourite database. Acutally I use sqlalchemy with sqlite for fun 😊. For schema migration we also can use [Alembic](https://alembic.sqlalchemy.org/en/latest/). I have to say SQLAlchemy + Alembic is really powerful, no matter you use python or not.

**Testing**

We still can use [pytest](https://docs.pytest.org/en/latest/) to do unit test, and AIOHTTP also provides an integration testing plugin of pytest for API testing [pytest-aiohttp](https://docs.aiohttp.org/en/stable/testing.html).



**Simple made easy (Summary)**

Sorry to borrow [Rich Hickey](https://www.infoq.com/presentations/Simple-Made-Easy/)‘s saying for python AIOHTTP.

If I go though all AIOHTTP details, I don't think one post can finish it. I may create anohter post for AIOHTTP details such as how to orgnize a project and how to build RESTful API ... 

For me I really like AIOHTTP because it has clear goal and whenever I need dependency I can find out it. Another fun is AIOHTTP has a boilerplate project call **[create-aio-app](https://github.com/aio-libs/create-aio-app)** like reactjs has create react app. If you need to build a python based web app may think about it.


