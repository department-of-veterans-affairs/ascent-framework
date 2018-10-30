package gov.va.ascent.framework.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class WSInterceptorTestUtil {
	private static final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	public static final String getRawXML(final SoapMessage sm) {
		StringWriter sw = null;
		String result = null;

		assertNotNull("SoapMessage cannot be null.", sm);
		assertNotNull("SoapMessage.getDocument cannot be null.", sm.getDocument());

		try {
			sw = new StringWriter();

			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			transformerFactory.newTransformer().transform(new DOMSource(sm.getDocument()), new StreamResult(sw));
			result = sw.toString();

		} catch (final TransformerException e) {
			throw new RuntimeException(e);
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (final IOException e) {
					// intentional no-op
				}
			}
		}

		return result;
	}

	public static final SoapMessage createSoapMessage(final String filePath)
			throws IOException, ParserConfigurationException, SAXException {
		SoapMessage sm = null;
		InputStream is = null;

		assertTrue("filePath cannot be null or empty", filePath != null && !filePath.isEmpty());

		try {
			final SoapMessageFactory sf = new AxiomSoapMessageFactory();
			is = new FileInputStream(new File(filePath));
			sm = sf.createWebServiceMessage();
			docFactory.setNamespaceAware(true);
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(is);
			sm.setDocument(doc);
		} finally {
			if (is != null) {
				is.close();
			}
		}

		return sm;
	}

}
