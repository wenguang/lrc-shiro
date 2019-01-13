
####来看看shiro与spring整合时要怎么写web.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

  <display-name>shiro-spring整合</display-name>

  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
      classpath:spring-beans.xml,
      classpath:spring-shiro-web.xml
    </param-value>
  </context-param>

  <!--
    contextConfigLocation这个参数，它定义在ContextLoader类，
    ContextLoaderListener就继承了这个类且实现了ServletContextListener接口,
    ContextLoader是创建、维护、销毁web上下文的一个类,
    后面的filters、servlets都会用到这个上下文,比例下面的DelegatingFilterProxy
    -->
  
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

  <!--
    与Web集成——《跟我学Shiro》 http://jinnianshilongnian.iteye.com/blog/2024723
    与Spring集成——《跟我学Shiro》 http://jinnianshilongnian.iteye.com/blog/2029717
    -->

  <!--
    这里filter-name中的shiroFilter配置在spring-shiro-web.xml中，
    DelagatingFilterProxy持有web上下文的引用，所以它能找到shiroFilter,
    和配置别的fitler不同，filter-class中的DelegatingFilterProxy不是shiroFilter的类,
    而是它代理了shiroFilter这个过滤器，而这个过滤器配置在spring-shiro-web.xml中,

    那用DelegatingFilterProxy的好处是什么呢？
    参考：spring filter的targetFilterLifecycle作用 https://blog.csdn.net/u013378306/article/details/50801001

    奥秒在targetFilterLifecycle

    按filter-class对应filter-name实现类的方式配置的话，filter是被加载到web容器中（比例tomcat），
    而spring配置中的beans是spring容器管理的，那filter想要引用spring配置中的bean就麻烦了，
    作法是让spring容器来管理filter的生命周期，这样filter想引用其它的spring bean就容易了
    targetFilterLifecycle设置为true上面就能实现这一点

    注意：
      shiroFilter对应的类是org.apache.shiro.spring.web.ShiroFilterFactoryBean,
      而不是org.apache.shiro.web.servlet.ShiroFilter
    在shiro-web模块中，用过org.apache.shiro.web.servlet.ShiroFilter对应filter的实现，它是被加载web容器中的
    而org.apache.shiro.spring.web.ShiroFilterFactoryBean是实现spring bean接口的，而它也不是单纯的过滤器
    而持有securityManager、shiro过滤器列表、loginUrl等对象，它更像是一个shiro.ini配置入口

    -->
  <filter>
    <filter-name>shiroFilter</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    <async-supported>true</async-supported>
    <init-param>
      <param-name>targetFilterLifecycle</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>shiroFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>



  <servlet>
    <servlet-name>spring-mvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
  </servlet>
  <servlet-mapping>
    <servlet-name>spring-mvc</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

</web-app>
```





//第十二章 与Spring集成——《跟我学Shiro》
http://jinnianshilongnian.iteye.com/blog/2029717

//第十四章 SSL——《跟我学Shiro》
http://jinnianshilongnian.iteye.com/blog/2036420

本模块除也与spring整合，也有ssl的内容（ssl部分还有问题）