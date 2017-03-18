---
title: Using Monit with Spring boot embedded tomcat
description: monitor uber jar running health.
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2016-03-27
date-modified: 2016-03-27
date-published: 2016-03-27
headline:
in-language: en
keywords: devops
---

[Monit](https://mmonit.com/monit/) is an open source process monitor & error recovery tool. I have a small VPS on DigitalOcean only has 512MB memory, my spring-boot app often due to out of memory to stop. So I plan to use Monit to monitor spring-boot process and restart it. I found a good [article](http://www.tecmint.com/how-to-install-and-setup-monit-linux-process-and-services-monitoring-program/) regarding how to install/using Monit. I learn how to use Monit to monitor a service such as “Nginx & Mysql”, but I still need to monitor my embedded tomcat WAR file meanwhile start program from my user.

So I create a shell script to handle startup my WAR file then record PID into pidfile so that I stop process with the pidfile.

Shell script template from [Stack Overflow](https://stackoverflow.com/questions/23454344/use-monit-monitor-a-python-program/25170143#25170143)

```bash
    #!/bin/bash
    PIDFILE=app.pid
    case $1 in
    start)
    # Launch your program as a detached process
    java -Xmx280m -Xss80m -jar app.war -server.port=8080 — server.tomcat.max-threads=20 > startup.log 2>&1 &
    # Get its PID and store it
    echo $! > ${PIDFILE}
    ;;
    stop)
    kill -15 `cat ${PIDFILE}`
    # Now that it’s killed, don’t forget to remove the PID file
    rm ${PIDFILE}
    ;;
```

The script is working very well when I execute it individually. But I still can’t through Monit to start my app. The root cause is that Monit uses “root” to execute script but my WAR file does’t belong to “root”. For fixing that I need to edit **“/etc/monit/monitrc”** configuration file like this:

```
    check process app with pidfile /home/dev/app/app.pid
    start program = “/bin/su — dev -c ‘cd /home/dev/app; ./startup.sh start’” timeout 100 seconds
    stop program = “/bin/su — dev -c ‘cd /home/dev/app; ./startup.sh stop’”
    if failed host 127.0.0.1 port 8080 then restart
    if 5 restarts within 5 cycles then timeout
```

> /bin/su — dev will use dev to execute my shell.