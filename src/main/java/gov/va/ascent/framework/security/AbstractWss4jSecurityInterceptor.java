package gov.va.ascent.framework.security;

import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

/**
 * An abstract implementation of spring's Wss4jSecurityInterceptor that adds
 * common encryption properties for encryption related implementations.
 */
public abstract class AbstractWss4jSecurityInterceptor extends Wss4jSecurityInterceptor {
	/**
	 * Retrieves properties to set to create a crypto file
	 *
	 * @return
	 */
	public abstract CryptoProperties retrieveCryptoProps();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		//
	}

}
