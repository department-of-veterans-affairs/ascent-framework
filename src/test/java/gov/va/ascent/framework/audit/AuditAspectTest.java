package gov.va.ascent.framework.audit;

import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.service.ServiceRequest;
import gov.va.ascent.framework.service.ServiceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by vgadda on 8/17/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class, JacksonAutoConfiguration.class})
public class AuditAspectTest {

    @Autowired
    AuditableService auditableService;

    @Test
    public void test(){
        AuditServiceRequest request = new AuditServiceRequest();
        request.setText("AuditServiceRequest");
        auditableService.annotatedMethod(request);
    }
}

@Component
class TestAuditableService implements AuditableService{

    @Auditable(event = AuditEvents.REQUEST_RESPONSE)
    public ServiceResponse annotatedMethod(ServiceRequest request){
        ServiceResponse response = new ServiceResponse();
        response.addMessage(MessageSeverity.INFO, "key", "value");
        return response;
    }
}

interface AuditableService{
    ServiceResponse annotatedMethod(ServiceRequest request);
}

class AuditServiceRequest extends ServiceRequest{
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

@Configuration
@ComponentScan("gov.va.ascent.framework.audit")
@EnableAspectJAutoProxy(proxyTargetClass = true)
class Config{

}