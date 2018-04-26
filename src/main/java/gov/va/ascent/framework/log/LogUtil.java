package gov.va.ascent.framework.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lalyos.jfiglet.FigletFont;

/**
 * The Class LogUtil will contain various log utility methods 
 *
 */
public class LogUtil {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    /** The font file */
    protected static final String FONT_FILE = "classpath:/flf/digital.flf";

    /**
     * The Enum Level.
     */
    protected enum Level {
        /** The debug. */
        DEBUG,
        /** The info. */
        INFO,
        /** The warn. */
        WARN,
        /** The error. */
        ERROR
    }
    
    /**
     * Log debug with a clear banner so it stands out.
     *
     * @param logger the logger
     * @param banner the banner
     * @param message the message
     */
    public static void logDebugWithBanner(final Logger logger, final String banner, final String message) {
        if(logger.isDebugEnabled()){
            logger.debug(createBannerString(banner, Level.DEBUG));
            logger.debug(message);
        }
    }

    /**
     * Log info with a clear banner so it stands out.
     *
     * @param logger the logger
     * @param banner the banner
     * @param message the message
     */
    public static void logInfoWithBanner(final Logger logger, final String banner, final String message) {
        if(logger.isInfoEnabled()){    
            logger.info(createBannerString(banner, Level.INFO));
            logger.info(message);
        }
    }

    /**
     * Log warning with a clear banner so it stands out.
     *
     * @param logger the logger
     * @param banner the banner
     * @param message the message
     */
    public static void logWarningWithBanner(final Logger logger, final String banner, final String message) {
        if(logger.isWarnEnabled()){    
            logger.warn(createBannerString(banner, Level.WARN));
            logger.warn(message);
        }
    }

    /**
     * Log error with a clear banner so it stands out.
     *
     * @param logger the logger
     * @param banner the banner
     * @param message the message
     */
    public static void logErrorWithBanner(final Logger logger, final String banner, final String message) {
        if(logger.isErrorEnabled()){    
            logger.error(createBannerString(banner, Level.ERROR));
            logger.error(message);
        }
    }

    /**
     * Creates the banner string.
     *
     * @param banner the banner
     * @param level the level
     * @return the string
     */
    private static String createBannerString(final String banner, final Level level) {
        String rtnBanner = banner;
        if (rtnBanner == null) {
            rtnBanner = "";
        }
        rtnBanner = level.name() + ": " + rtnBanner.toUpperCase();
        try {
            rtnBanner = "\n" + FigletFont.convertOneLine(FONT_FILE, rtnBanner);
        } catch (final Exception exc) {
            LOGGER.debug("Error creating ascii art.  Not a huge deal.", exc);
            rtnBanner = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + banner + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
        }
        return rtnBanner;
    }

}