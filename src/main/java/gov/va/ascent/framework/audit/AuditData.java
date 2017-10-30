package gov.va.ascent.framework.audit;

/**
 *
 * Used as a data transfer object from the Auditable annotation for writing to the audit log.
 *
 * @author npaulus
 */
public final class AuditData {

    /**
     * The event type.
     */
    private final AuditEvents event;

    /**
     * The activity (or method name).
     */
    private final String activity;

    /**
     * The class being audited.
     */
    private final String auditClass;

    /**
     * Constructs a new AuditData object.
     *
     * @param event the event type.
     * @param activity the activity or method name.
     * @param auditClass the class name for class under audit.
     */
    public AuditData(final AuditEvents event, final String activity, final String auditClass){
        this.event = event;
        this.activity = activity;
        this.auditClass = auditClass;
    }

    /**
     * Gets the event.
     *
     * @return the event.
     */
    public AuditEvents getEvent() {
        return event;
    }

    /**
     * Gets the activity.
     *
     * @return the activity
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Gets the audited class name.
     *
     * @return the audited class name.
     */
    public String getAuditClass() {
        return auditClass;
    }
}
