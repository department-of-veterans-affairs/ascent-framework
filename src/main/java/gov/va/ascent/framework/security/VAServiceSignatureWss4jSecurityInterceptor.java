package gov.va.ascent.framework.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.ws.security.SOAPConstants;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSEncryptionPart;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.apache.ws.security.util.WSSecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A Wss4j2 Security Interceptor to digitally sign a soap message.
 */
public class VAServiceSignatureWss4jSecurityInterceptor extends AbstractEncryptionWss4jSecurityInterceptor {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(VAServiceSignatureWss4jSecurityInterceptor.class);

	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#secureMessage(org.springframework.ws.soap .SoapMessage,
	 * org.springframework.ws.context.MessageContext)
	 */
	@Override
	protected final void secureMessage(final SoapMessage soapMessage, final MessageContext messageContext) {

		super.secureMessage(soapMessage, messageContext);

		WSSConfig.init();

		try {

			if (getCrypto() == null) {
				LOGGER.debug("Initializing crypto properties...");
				setCrypto(CryptoFactory.getInstance(retrieveCryptoProps()));
			}

			final WSSecSignature sign = new WSSecSignature();
			sign.setUserInfo(getKeyAlias(), getKeyPassword());
			sign.setKeyIdentifierType(WSConstants.ISSUER_SERIAL);

			final Document doc = soapMessage.getDocument();
			final WSSecHeader secHeader = new WSSecHeader();
			secHeader.insertSecurityHeader(doc);

			final List<WSEncryptionPart> parts = getEncryptionPartsList(doc.getDocumentElement());

			sign.setParts(parts);
			sign.prepare(doc, getCrypto(), secHeader);

			final List<javax.xml.crypto.dsig.Reference> referenceList = sign.addReferencesToSign(parts, secHeader);
			sign.computeSignature(referenceList, false, null);

			soapMessage.setDocument(doc);

		} catch (final WSSecurityException e) {
			LOGGER.error("failed encription ", e);
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
