package org.cftoolsuite.cfapp.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "query-policies" })
public class Policies {

    @JsonProperty("query-policies")
    private List<QueryPolicy> queryPolicies;

    @JsonCreator
    Policies(
            @JsonProperty("query-policies") List<QueryPolicy> queryPolicies) {
        this.queryPolicies = queryPolicies;
    }

    public List<QueryPolicy> getQueryPolicies() {
        return queryPolicies != null ? queryPolicies: Collections.emptyList();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return getQueryPolicies().isEmpty();
    }

    public List<Policy> all() {
        List<Policy> policies = new ArrayList<>();
        policies.addAll(getQueryPolicies());
        return policies;
    }

    public Policy getById(String policyId) {
        return all()
                .stream()
                .filter(policy -> policy.getId().equals(policyId))
                .findFirst()
                .orElse(null);
    }

}
