package gov.va.ascent.framework.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.wss4j.dom.WSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ResourceUtils;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import gov.va.ascent.framework.constants.AnnotationConstants;
import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.exception.InterceptingExceptionTranslator;
import gov.va.ascent.framework.log.PerformanceLogMethodInterceptor;
import gov.va.ascent.framework.security.VAServiceWss4jSecurityInterceptor;
import gov.va.ascent.framework.util.Defense;
import gov.va.ascent.framework.ws.client.remote.RemoteServiceCallInterceptor;

/**
 * Base WebService Client configuration, consolidates core/common web service configuration operations used across the applications.
 *
 * @author jshrader
 */
@Configuration
public class BaseWsClientConfig {

	/**
	 * The Constant LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseWsClientConfig.class);

	/**
	 * The Constant JAVA_IO_TMPDIR.
	 */
	protected static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

	/**
	 * The Constant PACKAGE_ASCENT_FOUNDATION_EXCEPTION.
	 */
	public static final String PACKAGE_ASCENT_FRAMEWORK_EXCEPTION = "gov.va.ascent.framework.exception";

	/**
	 * Creates the default web service template using the default audit request/response interceptors and no web service interceptors.
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final WebServiceTemplate createDefaultWebServiceTemplate(final String endpoint, final int readTimeout,
			final int connectionTimeout, final Marshaller marshaller, final Unmarshaller unmarshaller) {
		return this.createDefaultWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller,
				new HttpRequestInterceptor[] { null },
				new HttpResponseInterceptor[] { null }, null);
	}

	/**
	 * Creates the default web service template using the default audit request/response interceptors and the provided web service
	 * interceptors
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param wsInterceptors the ws interceptors
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final WebServiceTemplate createDefaultWebServiceTemplate(final String endpoint, final int readTimeout,
			final int connectionTimeout, final Marshaller marshaller, final Unmarshaller unmarshaller,
			final ClientInterceptor[] wsInterceptors) {
		return this.createDefaultWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller,
				new HttpRequestInterceptor[] { null },
				new HttpResponseInterceptor[] { null }, wsInterceptors);
	}

	/**
	 * Creates the default web service template using the supplied http request/response interceptors and the provided web service
	 * interceptors with axiom message factory
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param httpRequestInterceptors the http request interceptors
	 * @param httpResponseInterceptors the http response interceptors
	 * @param wsInterceptors the ws interceptors
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final WebServiceTemplate createDefaultWebServiceTemplate( // NOSONAR do NOT encapsulate params just to reduce the number
			final String endpoint, // NOSONAR do NOT encapsulate params just to reduce the number
			final int readTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final int connectionTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final Marshaller marshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final Unmarshaller unmarshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final HttpRequestInterceptor[] httpRequestInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final HttpResponseInterceptor[] httpResponseInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final ClientInterceptor[] wsInterceptors) { // NOSONAR do NOT encapsulate params just to reduce the number

		// create axiom message factory
		final AxiomSoapMessageFactory axiomSoapMessageFactory = new AxiomSoapMessageFactory();
		axiomSoapMessageFactory.setAttachmentCacheDir(new File(System.getProperty(BaseWsClientConfig.JAVA_IO_TMPDIR)));

		return this
				.createWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller, httpRequestInterceptors,
						httpResponseInterceptors, wsInterceptors, axiomSoapMessageFactory,
						null, null, null, null);
	}

	/**
	 * Creates the web service template using the the default audit request/response interceptors and the provided web service
	 * interceptors with saaj message factory.
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param wsInterceptors the ws interceptors
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SOAPException error creating message factory
	 */
	protected final WebServiceTemplate createSaajWebServiceTemplate(final String endpoint, final int readTimeout,
			final int connectionTimeout, final Marshaller marshaller, final Unmarshaller unmarshaller,
			final ClientInterceptor[] wsInterceptors) throws SOAPException {
		return this.createWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller,
				new HttpRequestInterceptor[] { null },
				new HttpResponseInterceptor[] { null }, wsInterceptors,
				new SaajSoapMessageFactory(MessageFactory.newInstance()),
				null, null, null, null);
	}

	/**
	 * Creates the ssl web service template using the default audit request/response interceptors and no web service interceptors.
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param keystore the path to the client ssl keystore
	 * @param keystorePass the password for the client ssl keystore
	 * @param truststore the path to the client ssl truststore
	 * @param truststorePass the password for the client ssl truststore
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final WebServiceTemplate createSslWebServiceTemplate( // NOSONAR do NOT encapsulate params just to reduce the number
			final String endpoint, // NOSONAR do NOT encapsulate params just to reduce the number
			final int readTimeout,// NOSONAR do NOT encapsulate params just to reduce the number
			final int connectionTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final Marshaller marshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final Unmarshaller unmarshaller,// NOSONAR do NOT encapsulate params just to reduce the number
			final Resource keystore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String keystorePass, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource truststore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String truststorePass) { // NOSONAR do NOT encapsulate params just to reduce the number
		return this.createSslWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller,
				new HttpRequestInterceptor[] { null },
				new HttpResponseInterceptor[] { null }, null, keystore, keystorePass, truststore, truststorePass);
	}

	/**
	 * Creates the ssl web service template using the default audit request/response interceptors and the provided web service
	 * interceptors
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param wsInterceptors the ws interceptors
	 * @param keystore the path to the client ssl keystore
	 * @param keystorePass the password for the client ssl keystore
	 * @param truststore the path to the client ssl truststore
	 * @param truststorePass the password for the client ssl truststore
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final WebServiceTemplate createSslWebServiceTemplate( // NOSONAR do NOT encapsulate params just to reduce the number
			final String endpoint, // NOSONAR do NOT encapsulate params just to reduce the number
			final int readTimeout,// NOSONAR do NOT encapsulate params just to reduce the number
			final int connectionTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final Marshaller marshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final Unmarshaller unmarshaller,// NOSONAR do NOT encapsulate params just to reduce the number
			final ClientInterceptor[] wsInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource keystore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String keystorePass, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource truststore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String truststorePass) { // NOSONAR do NOT encapsulate params just to reduce the number
		return this.createSslWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller,
				new HttpRequestInterceptor[] { null },
				new HttpResponseInterceptor[] { null }, wsInterceptors, keystore, keystorePass, truststore, truststorePass);
	}

	/**
	 * Creates the ssl web service template using the supplied http request/response interceptors and the provided web service
	 * interceptors with axiom message factory
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param httpRequestInterceptors the http request interceptors
	 * @param httpResponseInterceptors the http response interceptors
	 * @param wsInterceptors the ws interceptors
	 * @param keystore the path to the client ssl keystore
	 * @param keystorePass the password for the client ssl keystore
	 * @param truststore the path to the client ssl truststore
	 * @param truststorePass the password for the client ssl truststore
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final WebServiceTemplate createSslWebServiceTemplate( // NOSONAR do NOT encapsulate params just to reduce the number
			final String endpoint, // NOSONAR do NOT encapsulate params just to reduce the number
			final int readTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final int connectionTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final Marshaller marshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final Unmarshaller unmarshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final HttpRequestInterceptor[] httpRequestInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final HttpResponseInterceptor[] httpResponseInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final ClientInterceptor[] wsInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource keystore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String keystorePass, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource truststore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String truststorePass) { // NOSONAR do NOT encapsulate params just to reduce the number

		// create axiom message factory
		final AxiomSoapMessageFactory axiomSoapMessageFactory = new AxiomSoapMessageFactory();
		axiomSoapMessageFactory.setAttachmentCacheDir(new File(System.getProperty(BaseWsClientConfig.JAVA_IO_TMPDIR)));

		return this
				.createWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller, httpRequestInterceptors,
						httpResponseInterceptors, wsInterceptors, axiomSoapMessageFactory, keystore, keystorePass, truststore,
						truststorePass);
	}

	/**
	 * Creates the SAAJ SSL web service template using the the default audit request/response interceptors and the provided web service
	 * interceptors with saaj message factory.
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param wsInterceptors the ws interceptors
	 * @param keystore the path to the client ssl keystore
	 * @param keystorePass the password for the client ssl keystore
	 * @param truststore the path to the client ssl truststore
	 * @param truststorePass the password for the client ssl truststore
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SOAPException error creating message factory
	 */
	protected final WebServiceTemplate createSaajSslWebServiceTemplate( // NOSONAR do NOT encapsulate params just to reduce the number
			final String endpoint,  // NOSONAR do NOT encapsulate params just to reduce the number
			final int readTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final int connectionTimeout,  // NOSONAR do NOT encapsulate params just to reduce the number
			final Marshaller marshaller,  // NOSONAR do NOT encapsulate params just to reduce the number
			final Unmarshaller unmarshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final ClientInterceptor[] wsInterceptors,  // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource keystore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String keystorePass, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource truststore,  // NOSONAR do NOT encapsulate params just to reduce the number
			final String truststorePass) throws SOAPException { // NOSONAR do NOT encapsulate params just to reduce the number
		return this.createWebServiceTemplate(endpoint, readTimeout, connectionTimeout, marshaller, unmarshaller,
				new HttpRequestInterceptor[] { null },
				new HttpResponseInterceptor[] { null }, wsInterceptors,
				new SaajSoapMessageFactory(MessageFactory.newInstance()),
				keystore, keystorePass, truststore, truststorePass);
	}

	/**
	 * Creates web service template using the supplied http request/response interceptors and the provided web service
	 * interceptors and message factory - if web service clients wish to configure their own message factory.
	 *
	 * @param endpoint the endpoint
	 * @param readTimeout the read timeout
	 * @param connectionTimeout the connection timeout
	 * @param marshaller the marshaller
	 * @param unmarshaller the unmarshaller
	 * @param httpRequestInterceptors the http request interceptors
	 * @param httpResponseInterceptors the http response interceptors
	 * @param wsInterceptors the ws interceptors
	 * @param messageFactory webservice message factory
	 * @param truststore the path to the client ssl truststore
	 * @param truststorePass the password for the client ssl truststore
	 * @param keystore the path to the client ssl keystore
	 * @param keystorePass the password for the client ssl keystore
	 * @return the web service template
	 * @throws KeyManagementException the key management exception
	 * @throws UnrecoverableKeyException the unrecoverable key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws KeyStoreException the key store exception
	 * @throws CertificateException the certificate exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final WebServiceTemplate createWebServiceTemplate( // NOSONAR do NOT encapsulate params just to reduce the number
			final String endpoint, // NOSONAR do NOT encapsulate params just to reduce the number
			final int readTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final int connectionTimeout, // NOSONAR do NOT encapsulate params just to reduce the number
			final Marshaller marshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final Unmarshaller unmarshaller, // NOSONAR do NOT encapsulate params just to reduce the number
			final HttpRequestInterceptor[] httpRequestInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final HttpResponseInterceptor[] httpResponseInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final ClientInterceptor[] wsInterceptors, // NOSONAR do NOT encapsulate params just to reduce the number
			final WebServiceMessageFactory messageFactory, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource keystore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String keystorePass, // NOSONAR do NOT encapsulate params just to reduce the number
			final Resource truststore, // NOSONAR do NOT encapsulate params just to reduce the number
			final String truststorePass) { // NOSONAR do NOT encapsulate params just to reduce the number
		// configure the message sender
		final HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
		messageSender.setReadTimeout(readTimeout);
		messageSender.setConnectionTimeout(connectionTimeout);

		final HttpClientBuilder httpClient = HttpClients.custom();

		if (httpRequestInterceptors != null) {
			for (final HttpRequestInterceptor httpRequestInterceptor : httpRequestInterceptors) {
				httpClient.addInterceptorFirst(httpRequestInterceptor);
			}
		}
		if (httpResponseInterceptors != null) {
			for (final HttpResponseInterceptor httpResponseInterceptor : httpResponseInterceptors) {
				httpClient.addInterceptorLast(httpResponseInterceptor);
			}
		}

		addSslContext(httpClient, keystore, keystorePass, truststore, truststorePass);

		LOGGER.debug("HttpClient Object : %s% {}", ReflectionToStringBuilder.toString(httpClient));
		LOGGER.debug("Default Uri : %s% {}", endpoint);

		messageSender.setHttpClient(httpClient.build());

		// set the message factory & configure and return the template
		final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setMessageFactory(messageFactory);
		webServiceTemplate.setMessageSender(messageSender);
		webServiceTemplate.setDefaultUri(endpoint);
		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(unmarshaller);
		webServiceTemplate.setInterceptors(wsInterceptors);
		return webServiceTemplate;
	}

	/**
	 * If keystore and truststore are not null, SSL context is added to the httpClient.
	 *
	 * @param httpClient
	 * @param keystore
	 * @param keystorePass
	 * @param truststore
	 * @param truststorePass
	 */
	protected void addSslContext(final HttpClientBuilder httpClient,
			final Resource keystore, final String keystorePass, final Resource truststore, final String truststorePass) {

		if (keystore != null && truststore != null) {
			// Add SSL
			try {
				KeyStore keystoreFile = this.keyStore(keystore.getFile().getPath(), keystorePass.toCharArray());

				SSLContext sslContext =
						SSLContextBuilder.create()
								.loadKeyMaterial(keystoreFile, keystorePass.toCharArray())
								.loadTrustMaterial(truststore.getFile(), truststorePass.toCharArray()).build();
				// use NoopHostnameVerifier to turn off host name verification
				SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
				httpClient.setSSLSocketFactory(csf);

			} catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | IOException
					| UnrecoverableKeyException e) {
				String msg = "Could not establish SSL context due to " + e.getClass().getSimpleName() + ": " + e.getMessage();
				LOGGER.error(msg, e);
				throw new AscentRuntimeException(msg, e);
			}
		}
	}

	/**
	 * Produce a KeyStore object for a given JKS file and its password.
	 *
	 * @param filePath the JKS file path
	 * @param pass the password
	 * @return KeyStore
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	private KeyStore keyStore(String filePath, char[] pass)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		File key = ResourceUtils.getFile(filePath);

		try (InputStream in = new FileInputStream(key)) {
			keyStore.load(in, pass);
		}
		return keyStore;
	}

	/**
	 * Gets the bean name auto proxy creator.
	 *
	 * @param beanNames the bean names
	 * @param interceptorNames the interceptor names
	 * @return the bean name auto proxy creator
	 */
	public final BeanNameAutoProxyCreator getBeanNameAutoProxyCreator(final String[] beanNames, final String[] interceptorNames) {
		final BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
		creator.setBeanNames(beanNames);
		creator.setInterceptorNames(interceptorNames);
		return creator;
	}

	/**
	 * Gets the intercepting exception translator.
	 *
	 * @param defaultExceptionClass the default exception class
	 * @param exceptionPackagesToExclude the exception packages to exclude
	 * @return the intercepting exception translator
	 * @throws ClassNotFoundException the class not found exception
	 */
	@SuppressWarnings(AnnotationConstants.UNCHECKED)
	public final InterceptingExceptionTranslator getInterceptingExceptionTranslator(final String defaultExceptionClass,
			final String exceptionPackagesToExclude) throws ClassNotFoundException {
		// RR: second param should be made a Set

		Defense.notNull(defaultExceptionClass);
		Defense.notNull(exceptionPackagesToExclude);

		final InterceptingExceptionTranslator interceptingExceptionTranslator = new InterceptingExceptionTranslator();

		// set the default type of exception that should be returned when this interceptor runs
		interceptingExceptionTranslator
				.setDefaultExceptionType((Class<? extends RuntimeException>) Class.forName(defaultExceptionClass));

		// define packages that contain "our exceptions" that we want to propagate through
		// without again logging and/or wrapping
		final Set<String> exclusionSet = new HashSet<>();
		exclusionSet.add(PACKAGE_ASCENT_FRAMEWORK_EXCEPTION);
		interceptingExceptionTranslator.setExclusionSet(exclusionSet);

		return interceptingExceptionTranslator;
	}

	/**
	 * Gets the marshaller.
	 *
	 * @param transferPackage the transfer package
	 * @param schemaLocations the schema locations
	 * @param isLogValidationErrors the is log validation errors
	 * @return the marshaller
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings("REC_CATCH_EXCEPTION")
	public final Jaxb2Marshaller getMarshaller(final String transferPackage, final Resource[] schemaLocations,
			final boolean isLogValidationErrors) {
		Defense.notNull(transferPackage);

		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setValidationEventHandler(new JaxbLogAndEatValidationEventHandler(isLogValidationErrors));
		marshaller.setContextPath(transferPackage);
		if (schemaLocations != null) {
			marshaller.setSchemas(schemaLocations);
		}
		try {
			marshaller.afterPropertiesSet();
			// jluck - We want to catch exception here as that
			// is the exception type declared by the afterPropertiesSet() method
			// CHECKSTYLE:OFF
		} catch (final Exception ex) {
			// CHECKSTYLE:ON
			throw new IllegalArgumentException("Error configuring JAXB marshaller", ex);
		}
		return marshaller;
	}

	/**
	 * Gets the performance interceptor.
	 *
	 * @param methodWarningThreshhold the method warning threshhold
	 * @return the performance interceptor
	 */
	public final PerformanceLogMethodInterceptor getPerformanceLogMethodInterceptor(final Integer methodWarningThreshhold) {
		final PerformanceLogMethodInterceptor performanceLogMethodInteceptor = new PerformanceLogMethodInterceptor();
		performanceLogMethodInteceptor.setWarningThreshhold(methodWarningThreshhold);
		return performanceLogMethodInteceptor;
	}
	
	/**
	 * Gets the RemoteServiceCallInterceptor interceptor.
	 *
	 * @return the performance interceptor
	 */
	public final RemoteServiceCallInterceptor getRemoteServiceCallInterceptor() {
		final RemoteServiceCallInterceptor remoteServiceCallInterceptor = new RemoteServiceCallInterceptor();
		return remoteServiceCallInterceptor;
	}


	/**
	 * Gets the security interceptor.
	 *
	 * @param username the username
	 * @param password the password
	 * @param vaApplicationName the va application name
	 * @param stationId the stationd id
	 * @return the security interceptor
	 */
	protected final VAServiceWss4jSecurityInterceptor getVAServiceWss4jSecurityInterceptor(final String username,
			final String password, final String vaApplicationName, final String stationId) {
		final VAServiceWss4jSecurityInterceptor interceptor = new VAServiceWss4jSecurityInterceptor();
		interceptor.setSecurementActions(WSConstants.USERNAME_TOKEN_LN);
		interceptor.setSecurementUsername(username);
		interceptor.setSecurementPassword(password);
		interceptor.setSecurementPasswordType(WSConstants.PW_TEXT);
		interceptor.setVaApplicationName(vaApplicationName);
		interceptor.setSecurementMustUnderstand(false);
		if (!StringUtils.isEmpty(stationId)) {
			interceptor.setStationId(stationId);
		}
		return interceptor;
	}

}