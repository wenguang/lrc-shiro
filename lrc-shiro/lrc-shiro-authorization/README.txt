//《跟我学shiro》授权，这篇写得比较难懂
http://jinnianshilongnian.iteye.com/blog/2020017

//调试发现自定义Realm的doGetAuthorizationInfo不被调用，自主调用后hasRole也得不到期待的结果

// 关于何时执行shiro AuthorizingRealm 里的 doGetAuthenticationInfo与doGetAuthorizationInfo
http://www.voidcn.com/article/p-xxsxsixp-ym.html

// Shiro 如何主动调用doGetAuthorizationInfo方法
http://www.voidcn.com/article/p-bkdcqhoy-yb.html

// When does the method doGetAuthorizationInfo get called in Shiro?
https://stackoverflow.com/questions/34691141/when-does-the-method-dogetauthorizationinfo-get-called-in-shiro