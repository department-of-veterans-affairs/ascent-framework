package gov.va.ascent.framework.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ws.security.SOAPConstants;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSEncryptionPart;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.apache.ws.security.util.WSSecurityUtil;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.log.AscentLogger;
import gov.va.ascent.framework.log.AscentLoggerFactory;

/**
 * A Wss4j2 Security Interceptor to digitally sign a soap message.
 * <p>
 * Instantiation of this class requires a {@link #retrieveCryptoProps()} method that returns a {@link CryptoProperties} implementation,
 * as declared in {@link AbstractWss4jSecurityInterceptor#retrieveCryptoProps()}.
 * <p>
 * A complete example can be found in the spring beans of vetservices-partner-efolder EFolderWsClientConfig.java.
 * <p>
 * <b>NOTE:</b> VBMS uses the same cert for ssl AND signing AND key time stamp AND decryption. If future implementations require
 * separate certificates, this code, and possibly the {@link CryptoProperties} interface and certainly its implementations, will need
 * to be modified to provide the additional alias(es).
 * <p>
 * Calling code would typically provide the method in-line during construction, for example:
 *
 * <pre>
 * new VAServiceSignatureWss4jSecurityInterceptor() {
 * 	&#64;Override
 * 	public CryptoProperties retrieveCryptoProps() {
 * 		return new VAServiceSignatureWss4jSecurityInterceptor interceptor = cryptoProps.retrieveCryptoProperties();
 * 	}
 * };
 * </pre>
 */
public abstract class VAServiceSignatureWss4jSecurityInterceptor extends AbstractWss4jSecurityInterceptor {

	/** The Constant LOGGER. */
	private static final AscentLogger LOGGER = AscentLoggerFactory.getLogger(VAServiceSignatureWss4jSecurityInterceptor.class);

	/**
	 * Create a message that is encrypted and signed. See {@link VAServiceSignatureWss4jSecurityInterceptor} javadoc for implementation
	 * details.
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#secureMessage(org.springframework.ws.soap .SoapMessage,
	 *      org.springframework.ws.context.MessageContext)
	 */
	@Override
	protected final void secureMessage(final SoapMessage soapMessage, final MessageContext messageContext) {

		super.secureMessage(soapMessage, messageContext);

		WSSConfig.init();

		try {

			CryptoProperties props = retrieveCryptoProps();

			final WSSecSignature sign = new WSSecSignature();
			LOGGER.info("UserInfo alias {} " + props.getCryptoDefaultAlias());
			sign.setUserInfo(props.getCryptoDefaultAlias(), props.getCryptoKeystorePw());
			sign.setKeyIdentifierType(WSConstants.ISSUER_SERIAL);

			final Document doc = soapMessage.getDocument();
			final WSSecHeader secHeader = new WSSecHeader();
			secHeader.insertSecurityHeader(doc);

			final List<WSEncryptionPart> parts = getEncryptionPartsList(doc.getDocumentElement());

			sign.setParts(parts);
			LOGGER.info("crypto {} " + ReflectionToStringBuilder.reflectionToString(props));
			LOGGER.info("creating crypto instance... ");

			Crypto instance = CryptoFactory.getInstance(props);

			LOGGER.info("crypto instance created {} " + ReflectionToStringBuilder.reflectionToString(instance));

			sign.prepare(doc, instance, secHeader);

			LOGGER.info("Document prepared for signature, doc {} " + ReflectionToStringBuilder.reflectionToString(doc) + " sign {} "
					+ ReflectionToStringBuilder.reflectionToString(sign) + " secHeader {} "
					+ ReflectionToStringBuilder.reflectionToString(secHeader));

			final List<javax.xml.crypto.dsig.Reference> referenceList = sign.addReferencesToSign(parts, secHeader);

			LOGGER.info("References added for signature parts {}" + ReflectionToStringBuilder.reflectionToString(parts) + " sign {} "
					+ ReflectionToStringBuilder.reflectionToString(sign) + " secHeader {} "
					+ ReflectionToStringBuilder.reflectionToString(secHeader));

			sign.computeSignature(referenceList, false, null);

			LOGGER.info("References added for signature referenceList {}" + ReflectionToStringBuilder.reflectionToString(referenceList)
					+ " sign {} " + ReflectionToStringBuilder.reflectionToString(sign));

			soapMessage.setDocument(doc);

			LOGGER.info("Document set in the SOAP request {}" + ReflectionToStringBuilder.reflectionToString(doc) + " soapMessage {} "
					+ ReflectionToStringBuilder.reflectionToString(soapMessage));

		} catch (final WSSecurityException e) {
			LOGGER.error("failed encryption ", e);
			throw new AscentRuntimeException(e);
		}
	}

	/**
	 * Gets the encryption parts list.
	 *
	 * @param soapMessage the soap message
	 * @return the encryption parts list
	 */
	private List<WSEncryptionPart> getEncryptionPartsList(final Element soapMessage) {

		final List<WSEncryptionPart> retVal = new ArrayList<>();
		WSEncryptionPart encPart = null;

		final Element timestamp = WSSecurityUtil.findElement(soapMessage, WSConstants.TIMESTAMP_TOKEN_LN, WSConstants.WSU_NS);

		if (timestamp != null) {
			encPart = new WSEncryptionPart(WSConstants.TIMESTAMP_TOKEN_LN, WSConstants.WSU_NS, "");
			retVal.add(encPart);
		}

		final SOAPConstants soapConstants = WSSecurityUtil.getSOAPConstants(soapMessage);
		encPart = new WSEncryptionPart(soapConstants.getBodyQName().getLocalPart(), soapConstants.getEnvelopeURI(), "");
		retVal.add(encPart);

		return retVal;
	}

}
