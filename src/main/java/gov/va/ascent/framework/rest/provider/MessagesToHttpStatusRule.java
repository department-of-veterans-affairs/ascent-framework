package gov.va.ascent.framework.rest.provider;

import gov.va.ascent.framework.messages.Message;
import org.springframework.http.HttpStatus;

import java.util.Set;

/**
 * The Interface MessagesToHttpStatusRule is the rule interface used in the MessagesToHttpStatusRulesEngine.
 *
 * @author jshrader
 */
@FunctionalInterface
public interface MessagesToHttpStatusRule {

	/**
	 * Eval.
	 *
	 * @param messagesToEval the messages to eval
	 * @return the HttpStatus
	 */
	HttpStatus eval(Set<Message> messagesToEval);
}
