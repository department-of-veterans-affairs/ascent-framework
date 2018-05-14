package gov.va.ascent.framework.security;

import gov.va.ascent.framework.security.AbstractEncryptionWss4jSecurityInterceptor;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecEncrypt;
import org.apache.ws.security.message.WSSecHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * The Class VAServiceEncryptionWss4jSecurityInterceptor.
 */
public class VAServiceEncryptionWss4jSecurityInterceptor extends AbstractEncryptionWss4jSecurityInterceptor {
	/**
	 * message encoding.
	 */
	private static final String UTF_8 = "utf-8";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(VAServiceEncryptionWss4jSecurityInterceptor.class);

	/** The Constant REQUEST_CONTENT. */
	private static final String REQUEST_CONTENT = "content";

	/** Turns on/off auditing. */
	@Value("${wss-framework-audit.enabled:true}")
	private boolean auditEnabled;

	/** Turns on/off auditing of the full payload. */
	@Value("${wss-framework-audit.auditFullMessageContent:true}")
	private boolean auditFullMessageContentsEnabled;

	/** The audit service. */
	@Autowired
	private Audit auditService;

	/**
	 * Audit service information.
	 * 
	 * @param soapMessage the soap message
	 * @param code the code
	 */
	private void auditServiceInformation(final SoapMessage soapMessage, final AuditEventTypeCode code) {
		if (auditEnabled && auditFullMessageContentsEnabled) {

			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				soapMessage.writeTo(baos);

				final LogEventRequest.AttributesBuilder attributes = new LogEventRequest.AttributesBuilder();
				attributes.put(REQUEST_CONTENT, baos.toString(UTF_8));
				final LogEventRequest logEventRequest =
						new LogEventRequest(new Date(), code.getCode(), "Request call from our web service client", attributes);
				auditService.logEvent(logEventRequest);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("value of " + code.getDescription() + " \n" + baos.toString(UTF_8));
				}
			} catch (final IOException e) {
				LOGGER.error("i/o exception logging payload " + code.getDescription() + " ", e);
			}
		}
	}

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
				LOGGER.error("Initializing crypto properties..." + getCryptoFile() + "end");
				setCrypto(CryptoFactory.getInstance(getCryptoFile()));
			}

			LOGGER.debug("Encrypting outgoing message...");

			final WSSecEncrypt encrypt = new WSSecEncrypt();
			encrypt.setUserInfo(getKeyAlias());

			final Document doc = soapMessage.getDocument();
			final WSSecHeader secHeader = new WSSecHeader();
			secHeader.insertSecurityHeader(doc);

			auditServiceInformation(soapMessage, AuditEventTypeCode.WS_REQUEST_UNENCRYPTED_AUDIT);
			encrypt.setDocument(doc);

			encrypt.build(doc, getCrypto(), secHeader);

			soapMessage.setDocument(doc);
		} catch (final WSSecurityException e) {
			LOGGER.error("failed to encrypt ", e);
		}
	}

	/**
	 * Sets the audit enabled.
	 * 
	 * @param auditEnabled the new audit enabled
	 */
	public final void setAuditEnabled(final boolean auditEnabled) {
		this.auditEnabled = auditEnabled;
	}

	/**
	 * Sets the audit full message contents enabled.
	 * 
	 * @param auditFullMessageContentsEnabled the new audit full message contents enabled
	 */
	public final void setAuditFullMessageContentsEnabled(final boolean auditFullMessageContentsEnabled) {
		this.auditFullMessageContentsEnabled = auditFullMessageContentsEnabled;
	}

	/**
	 * Sets the audit service.
	 * 
	 * @param auditService the new audit service
	 */
	public final void setAuditService(final AuditService auditService) {
		this.auditService = auditService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor#validateMessage(org.springframework.ws.soap.SoapMessage,
	 * org.springframework.ws.context.MessageContext)
	 */
	@Override
	protected final void validateMessage(final SoapMessage soapMessage, final MessageContext messageContext) {
		super.validateMessage(soapMessage, messageContext);
		auditServiceInformation(soapMessage, AuditEventTypeCode.WS_RESPONSE_UNENCRYPTED_AUDIT);
	}
}
