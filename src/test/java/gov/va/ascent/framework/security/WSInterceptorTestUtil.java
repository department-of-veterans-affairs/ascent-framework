package gov.va.ascent.framework.security;

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
		final SoapMessageFactory sf = new AxiomSoapMessageFactory();
		final InputStream is = new FileInputStream(new File(filePath));
		final SoapMessage sm = sf.createWebServiceMessage();
		docFactory.setNamespaceAware(true);
		final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		final Document doc = docBuilder.parse(is);
		sm.setDocument(doc);
		is.close();
		return sm;
	}

}
