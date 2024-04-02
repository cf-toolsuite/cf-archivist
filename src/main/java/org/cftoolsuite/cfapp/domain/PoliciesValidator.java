package org.cftoolsuite.cfapp.domain;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class PoliciesValidator {

    private static final String REQUIRED_PROPERTIES_REJECTED_MESSAGE = "-- {} was rejected because required properties failed validation.";
    private static final String QUERY_REJECTED_MESSAGE = "-- {} was rejected because either name or sql was blank or sql did not start with SELECT.";
    private static final String EMAIL_NOTIFICATION_TEMPLATE_REJECTED_MESSAGE = "-- {} was rejected because either the email template did not contain valid email addresses for from/to or the subject/body was blank.";


    public boolean validate(QueryPolicy policy) {
        boolean hasId = Optional.ofNullable(policy.getId()).isPresent();
        boolean hasQueries = Optional.ofNullable(policy.getQueries()).isPresent();
        boolean hasEmailNotificationTemplate = Optional.ofNullable(policy.getEmailNotificationTemplate()).isPresent();
        boolean valid = !hasId && hasQueries && hasEmailNotificationTemplate;
        if (hasQueries) {
            if (ObjectUtils.isEmpty(policy.getQueries())) {
                valid = false;
            } else {
                for (Query q: policy.getQueries()) {
                    if (!q.isValid()) {
                        valid = false;
                        log.warn(QUERY_REJECTED_MESSAGE, policy.toString());
                        break;
                    }
                }
            }
        }
        if (hasEmailNotificationTemplate) {
            if (!policy.getEmailNotificationTemplate().isValid()) {
                valid = false;
                log.warn(EMAIL_NOTIFICATION_TEMPLATE_REJECTED_MESSAGE, policy.toString());
            }
        }
        if (valid == false) {
            log.warn(REQUIRED_PROPERTIES_REJECTED_MESSAGE, policy.toString());
        }
        return valid;
    }

}
