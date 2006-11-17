/*
 * Created on 16-Mar-2006
 */
package uk.org.ponder.springutil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import uk.org.ponder.fileutil.StalenessEntry;
import uk.org.ponder.streamutil.StreamResolver;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * A "caching" source of InputStreams that will only poll the filesystem for
 * changes after a specified lag. Currently any non-filesystem resources are
 * assumed to be ALWAYS STALE, that is, they will always have their streams
 * returned rather than the marker.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class CachingInputStreamSource implements StreamResolver {
  /**
   * If the stream is considered up to date, either through actually being up to
   * date or through having been polled within the last
   * <code>cacheSeconds</code>, this marker value is returned from
   * getInputStream. Do not attempt to use any methods of this object!
   */
  public static final InputStream UP_TO_DATE = new ByteArrayInputStream(
      new byte[0]);

  private int cachesecs;

  public static final int ALWAYS_STALE = 0;

  public static final long NEVER_STALE_MODTIME = Long.MAX_VALUE;

  /**
   * Sets the lag after which the filesystem will be checked again for change of
   * datestamp. At a value of ALWAYS_STALE (0) the resource will always be
   * reloaded.
   */

  public void setCacheSeconds(int cachesecs) {
    this.cachesecs = cachesecs;
  }

  private ResourceLoader resourceloader;

  private StreamResolver baseresolver;

  private Map stalenesses = new HashMap();

  // The first argument here is typically the ApplicationContext - note that
  // it will stubbornly interpret ALL paths as relative to the ServletContext,
  // whether they begin with slash or no - @see ServletContextResource
  public CachingInputStreamSource(ResourceLoader resourceloader, int cachesecs) {
    this.resourceloader = resourceloader;
    this.cachesecs = cachesecs;
    init();
  }

  public void init() {
    baseresolver = new SpringStreamResolver(resourceloader);
  }

  public StreamResolver getNonCachingResolver() {
    return baseresolver;
  }

  public InputStream openStream(String fullpath) {
    StalenessEntry staleness = (StalenessEntry) stalenesses.get(fullpath);
    boolean isnew = false;
    if (staleness == null) {
      isnew = true;
      staleness = new StalenessEntry();
    }
    long now = System.currentTimeMillis();
    boolean isstale = false;

    Resource res = null;
    try {
      if (staleness.modtime != NEVER_STALE_MODTIME
          && now > staleness.lastchecked + cachesecs * 1000) {
        res = resourceloader.getResource(fullpath);
        if (!res.exists())
          return null;
        try {
          if (res instanceof ClassPathResource) {
            staleness.modtime = NEVER_STALE_MODTIME;
          }
          else {
            // Logger.log.debug("Trying to load from path " + fullpath);
            File f = res.getFile(); // throws IOException
            long modtime = f.lastModified();
            if (modtime > staleness.modtime) {
              staleness.modtime = modtime;
              isstale = true;
            }
          }
          if (isnew) {
            stalenesses.put(fullpath, staleness);
          }
        }
        catch (Exception e) {
          // If it's not a file, it's always stale.
          return res.getInputStream();
        }
      }
      staleness.lastchecked = now;

      return isstale ? res.getInputStream()
          : UP_TO_DATE;
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e,
          "Error opening stream for resource " + fullpath);
    }
  }

}
