package org.apache.shiro.cache.ehcache;

import net.sf.ehcache.Ehcache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class SingleEhCacheManager implements CacheManager, Initializable, Destroyable {
    private static final Logger log = LoggerFactory.getLogger(EhCacheManager.class);
    protected net.sf.ehcache.CacheManager manager;
    private boolean cacheManagerImplicitlyCreated = false;
    private String cacheManagerConfigFile = "classpath:org/apache/shiro/cache/ehcache/ehcache.xml";

    public SingleEhCacheManager() {
    }

    public net.sf.ehcache.CacheManager getCacheManager() {
        return this.manager;
    }

    public void setCacheManager(net.sf.ehcache.CacheManager manager) {
        this.manager = manager;
    }

    public String getCacheManagerConfigFile() {
        return this.cacheManagerConfigFile;
    }

    public void setCacheManagerConfigFile(String classpathLocation) {
        this.cacheManagerConfigFile = classpathLocation;
    }

    protected InputStream getCacheManagerConfigFileInputStream() {
        String configFile = this.getCacheManagerConfigFile();

        try {
            return ResourceUtils.getInputStreamForPath(configFile);
        } catch (IOException var3) {
            throw new ConfigurationException("Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", var3);
        }
    }

    public final <K, V> Cache<K, V> getCache(String name) throws CacheException {
        if (log.isTraceEnabled()) {
            log.trace("Acquiring EhCache instance named [" + name + "]");
        }

        try {
            Ehcache cache = this.ensureCacheManager().getEhcache(name);
            if (cache == null) {
                if (log.isInfoEnabled()) {
                    log.info("Cache with name '{}' does not yet exist.  Creating now.", name);
                }

                this.manager.addCache(name);
                cache = this.manager.getCache(name);
                if (log.isInfoEnabled()) {
                    log.info("Added EhCache named [" + name + "]");
                }
            } else if (log.isInfoEnabled()) {
                log.info("Using existing EHCache named [" + ((Ehcache)cache).getName() + "]");
            }

            return new EhCache((Ehcache)cache);
        } catch (net.sf.ehcache.CacheException var3) {
            throw new CacheException(var3);
        }
    }

    public final void init() throws CacheException {
        this.ensureCacheManager();
    }

    private net.sf.ehcache.CacheManager ensureCacheManager() {
        try {
            if (this.manager == null) {
                if (log.isDebugEnabled()) {
                    log.debug("cacheManager property not set.  Constructing CacheManager instance... ");
                }

                /**
                 * 因为测试用例的关系，需要将Ehcache的CacheManager改为使用VM单例模式：
                 * this.manager = new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream());
                 * 改为
                 * this.manager = net.sf.ehcache.CacheManager.create(getCacheManagerConfigFileInputStream());
                 *
                 * 否则会出现异常：org.apache.shiro.cache.CacheException: net.sf.ehcache.CacheException: Another CacheManager with same name 'lrc-shiro-stuff' already exists in the same VM
                 */
                //this.manager = new net.sf.ehcache.CacheManager(this.getCacheManagerConfigFileInputStream());
                this.manager = net.sf.ehcache.CacheManager.create(this.getCacheManagerConfigFileInputStream());

                if (log.isTraceEnabled()) {
                    log.trace("instantiated Ehcache CacheManager instance.");
                }

                this.cacheManagerImplicitlyCreated = true;
                if (log.isDebugEnabled()) {
                    log.debug("implicit cacheManager created successfully.");
                }
            }

            return this.manager;
        } catch (Exception var2) {
            throw new CacheException(var2);
        }
    }

    public void destroy() {
        if (this.cacheManagerImplicitlyCreated) {
            try {
                net.sf.ehcache.CacheManager cacheMgr = this.getCacheManager();
                cacheMgr.shutdown();
            } catch (Throwable var5) {
                if (log.isWarnEnabled()) {
                    log.warn("Unable to cleanly shutdown implicitly created CacheManager instance.  Ignoring (shutting down)...", var5);
                }
            } finally {
                this.manager = null;
                this.cacheManagerImplicitlyCreated = false;
            }
        }

    }
}

