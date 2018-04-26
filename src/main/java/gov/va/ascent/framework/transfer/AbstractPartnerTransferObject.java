package gov.va.ascent.framework.transfer;

/**
 * To be extended by any data transfer objects that will be generated in a partner client jar project.
 *
 * @author aburkholder
 */
/*
 * Sonar refuses to recognize testing on this class,
 * but recognizes 100% coverage on the exact same test
 * for AbstractServiceTransferObjectTest. So, the only
 * viable option seems to be: suppress coverage issue.
 */
@java.lang.SuppressWarnings("common-java:InsufficientLineCoverage")
@edu.umd.cs.findbugs.annotations.SuppressWarnings("all")
public abstract class AbstractPartnerTransferObject extends AbstractTransferObject {
	private static final long serialVersionUID = 6730544865283424537L;
}
