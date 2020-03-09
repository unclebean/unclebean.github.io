---
title: Airflow beyond crontab 
description: schedule workflow tool 
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2020-03-08
date-modified: 2020-03-08
date-published: 2020-03-08
headline:
in-language: en
keywords: tool 
draft: false 
share-image: /images/airflow.png

---

#### Airflow beyond crontab

> Do you want to have a tool which can aggregate news or data then email to you or your friends?
>
> Are you working on data driven project, once get new data need to process step by step?
>
> If you have same problem, airflow maybe a good candidate can solve the problem for you.

**What's airflow**

[Apache Airflow (or simply Airflow) is a platform to programmatically author, schedule, and monitor workflows.](https://github.com/apache/airflow/blob/master/README.md) It has two main parts - webserver & scheduler.

* webserver

  Webserver command will launch airflow dashboard to manange all schedule workflow via UI.

* scheduler

  This is a background process  uses to start scheduler and load all DAGs.

**Basic & important concept of DAG and Task operator**

* DAG - directed acyclic graphs

  Use to define a workflow which running in airflow plantform, we can think it's a collection of tasks and running task by task with a crontab expression. 

  There is an example, means that I define a DAG will run every 30 mins, if DAG failed will delay 10 mins to trigger next job and only try once. **Two things** should be **careful**.

  First - start date must be earlier than current date otherwise DAG will not start once you start airflow scheduler.

  Second - Be careful **timezone**! Not only setting in DAG defination also need to make sure your system timezone is same as your DAG start date timezone, if it's different your DAG will run in the UTC time.

  ```python
  import airflow
  import pendulum
  from airflow import DAG
  import datetime
  from airflow.operators.python_operator import PythonOperator
  
  utc_date = airflow.utils.dates.days_ago(1)
  local_tz = pendulum.timezone("Asia/Singapore")
  
  
  default_args = {
      "owner": "Sanji",
      "depends_on_past": False,
      "start_date": datetime.datetime(
          utc_date.year, utc_date.month, utc_date.day, tzinfo=local_tz
      ),
      "retries": 1,
      "retry_delay": datetime.timedelta(minutes=10),
  }
  
  example_dag = DAG("example", default_args=default_args, schedule_interval="*/30 * * * *")
  
  
  ```

* Task Operator

  Airflow provides a lot of different operators:

  - [`BashOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/bash_operator/index.html#airflow.operators.bash_operator.BashOperator) - executes a bash command
  - [`PythonOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/python_operator/index.html#airflow.operators.python_operator.PythonOperator) - calls an arbitrary Python function
  - [`EmailOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/email_operator/index.html#airflow.operators.email_operator.EmailOperator) - sends an email
  - [`SimpleHttpOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/http_operator/index.html#airflow.operators.http_operator.SimpleHttpOperator) - sends an HTTP request
  - [`MySqlOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/mysql_operator/index.html#airflow.operators.mysql_operator.MySqlOperator), [`SqliteOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/sqlite_operator/index.html#airflow.operators.sqlite_operator.SqliteOperator), [`PostgresOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/postgres_operator/index.html#airflow.operators.postgres_operator.PostgresOperator), [`MsSqlOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/mssql_operator/index.html#airflow.operators.mssql_operator.MsSqlOperator), [`OracleOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/oracle_operator/index.html#airflow.operators.oracle_operator.OracleOperator), [`JdbcOperator`](https://airflow.apache.org/docs/stable/_api/airflow/operators/jdbc_operator/index.html#airflow.operators.jdbc_operator.JdbcOperator), etc. - executes a SQL command

  Current I'm only using python operator for aggregating data and do further analysis. There is a very interesting question how to define the *task flow* ?

  In Airflow we can use downstream/upsteam to define task flow and Airflow also provides bit shift operator. 

   ```python
  task1 >> task2 >> task3 # that means run task1 first then task2 then task3
   ```

**Why Airflow**

* No outage 

  Airflow is a python project whenever I change my DAG after that I no need to restart my scheduler service, the DAG will be reloaded automatically.

* Strategy

  For my scenario I need to run multiple strategies once data loaded, so I need clear data process flow. Airflow is really good for that case.

* Visualization monitoring task status

  I used to build dashboard for spring-boot scheduler project, it uses to manage & monitor all scheduler jobs. I can't say it's not good, but as a good scheduler tool visualization & mannually trigger job is very important. So Airflow is really realy good, see below screenshot, it's from my airflow server. It's very clear to monitor every task status.


  ![task status](https://unclebean.github.io/images/airflow_status.png) 



