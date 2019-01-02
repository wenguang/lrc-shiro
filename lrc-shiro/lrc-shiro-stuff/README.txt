// 《跟我学shiro》—— 相关对象
http://jinnianshilongnian.iteye.com/blog/2022468

// 《跟我学shiro》——缓存
http://jinnianshilongnian.iteye.com/blog/2029217

                /**
                 * 因为测试用例的关系，需要将Ehcache的CacheManager改为使用VM单例模式：
                 * this.manager = new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream());
                 * 改为
                 * this.manager = net.sf.ehcache.CacheManager.create(getCacheManagerConfigFileInputStream());
                 *
                 * 否则会出现异常：org.apache.shiro.cache.CacheException: net.sf.ehcache.CacheException: Another CacheManager with same name 'lrc-shiro-stuff' already exists in the same VM
                 */