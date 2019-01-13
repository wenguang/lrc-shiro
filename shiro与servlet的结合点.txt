




在web.xml中定义的context-param、listener、filter、servlet的加载顺序：
1、servlet容器（如tomcat）读web.xml中的context-param和listener
2、servlet容器为应用创建servletContext
3、servlet容器把context-param的值传给servletContext
4、servlet容器创建listener实例，监听
5、在listener的监听方法中，通过ServletContextEvent.getServletContext()得到上下文servletContext从而得到context-param定义的参数
6、利用context-param的值进行处理

在shiro应用中
web.xml有名为shiroEnvironmentClass、shiroConfigLocations的context-param参数，名为EnvironmentLoaderListener的监听器
shiroEnvironmentClass、shiroConfigLocations定义在EnvironmentLoader类中

EnvironmentLoaderListener的监听器继承了EnvironmentLoader，且实现ServletContextListener接口
EnvironmentLoaderListener在contextInitailized事件触发时，给EnvironmentLoader传入shiroEnvironmentClass、shiroConfigLocations的值，
它用shiroEnvironmentClass指定的类去创建webEnvironment实例，如IniWebEnvironment

那IniWebEnvironment有啥用？
它用从EnvironmentLoader传的shiroConfigLocations指定的shiro的ini配置文件创建WebSecurityManager
同时，它还维护着FilterChainResolver



【疑问】
一运行（未登录），就创建了3个session


【参考】

//web.xml的加载过程是context-param >> listener >> fileter >> servlet
https://www.cnblogs.com/yaoyiyao/p/7198076.html

//<context-param>与<init-param>的区别与作用
https://blog.csdn.net/m13666368773/article/details/7929934

//源码分析——Shiro 之 入口：EnvironmentLoaderListener
https://my.oschina.net/huangyong/blog/209339

//源码分析——Shiro 之 Filter（上）：ShiroFilter
https://my.oschina.net/huangyong/blog/210438