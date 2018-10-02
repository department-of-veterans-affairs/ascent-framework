package gov.va.ascent.framework.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.github.lalyos.jfiglet.FigletFont;

/**
 * Object that represents text in the form of ASCII Art.
 *
 * @author aburkholder
 */
public class AscentLogBanner {

	private static final Logger LOGGER = LoggerFactory.getLogger(AscentLogBanner.class);

	private String banner;
	private String bannerText;
	private Level level;

	/** The font file */
	protected static final String FONT_FILE = "classpath:/flf/digital.flf";

	/**
	 * Create an ASCII Art representation of some text.
	 * The static {@link #newBanner(String, Level)} convenience method does the same.
	 *
	 * @param bannerText the banner text
	 * @param level the log level reported by the banner
	 */
	public AscentLogBanner(String bannerText, Level level) {
		Level useLevel = getSafeLevel(level);
		this.bannerText = bannerText == null ? "" : bannerText;
		this.level = useLevel;
		this.banner = createBannerString(bannerText, useLevel);
	}

	/**
	 * Convenience method. Same as {@link #AscentLogBanner(String, Level)}
	 *
	 * @param bannerText the banner text
	 * @param level the log level reported by the banner
	 * @return AscentLogBanner
	 */
	public static AscentLogBanner newBanner(final String bannerText, final Level level) {
		return new AscentLogBanner(bannerText, level);
	}

	/**
	 * Get the text used in the banner.
	 *
	 * @return String
	 */
	public String getBannerText() {
		return this.bannerText;
	}

	/**
	 * Get the banner as a String.
	 *
	 * @return String
	 */
	public String getBanner() {
		return this.banner;
	}

	/**
	 * Get the banner as a String that reports the specified log level.
	 * <p>
	 * Note that this method rebuilds the banner, so should be used infrequently.
	 *
	 * @param level the log level
	 * @return String
	 */
	public String getBanner(Level level) {
		Level useLevel = getSafeLevel(level);
		if (!useLevel.equals(this.level)) {
			this.level = useLevel;
			this.banner = createBannerString(this.bannerText, useLevel);
		}
		return this.banner;
	}

	/**
	 * Get the banner's reported log level.
	 *
	 * @return Level
	 */
	public Level getLevel() {
		return this.level;
	}

	/**
	 * Set the banner's reported log level. Defaults to Level.INFO if specified level is {@code null}.
	 * <p>
	 * Note that this method rebuilds the banner, so should be used infrequently.
	 *
	 * @param level the log level (severity)
	 */
	public void setLevel(Level level) {
		this.level = getSafeLevel(level);
		this.banner = createBannerString(this.bannerText, level);
	}

	/**
	 * Creates the banner ASCII Art string.
	 *
	 * @param banner the banner
	 * @param level the level
	 * @return the string
	 */
	private String createBannerString(final String banner, final Level level) {
		String rtnBanner = banner;
		if (rtnBanner == null) {
			rtnBanner = "";
		}
		rtnBanner = (level == null ? "" : level.name() + ": ") + rtnBanner.toUpperCase();
		try {
			rtnBanner = "\n" + FigletFont.convertOneLine(FONT_FILE, rtnBanner);
		} catch (final Exception exc) {
			LOGGER.debug("Error creating ascii art.  Not a huge deal.", exc);
			rtnBanner = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + banner + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
		}
		return rtnBanner;
	}

	/**
	 * Returns a log level (defaults to INFO), even if the specified log level is {@code null}.
	 *
	 * @param level the log level
	 * @return Level a non-null log level
	 */
	private Level getSafeLevel(final Level level) {
		Level useLevel = level;
		if (level == null) {
			useLevel = Level.INFO;
		}
		return useLevel;
	}

}
