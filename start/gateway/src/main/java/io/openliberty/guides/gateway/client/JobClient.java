// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
 // end::copyright[]
package io.openliberty.guides.gateway.client;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;


import io.openliberty.guides.models.Job;
import io.openliberty.guides.models.JobResult;
import io.openliberty.guides.models.Jobs;

@RequestScoped
public class JobClient {

    @Inject
    @ConfigProperty(name = "GATEWAY_JOB_BASE_URI", defaultValue = "http://localhost:9082")
    private String baseUri;

    private WebTarget target;

    public JobClient() {
        this.target = null;
    }

    // tag::getJobsCompletionStage[]
    public CompletionStage<Jobs> getJobs() {
        return iBuilder(webTarget())
            // tag::rxGetJobsCompletionStage[]
            .rx()
            // end::rxGetJobsCompletionStage[]
            .get(Jobs.class);
    }
    // end::getJobsCompletionStage[]

    // tag::getJobCompletionStage[]
    public CompletionStage<JobResult> getJob(String jobId) {
        return iBuilder(webTarget().path(jobId))
            // tag::rxGetJobCompletionStage[]
            .rx()
            // end::rxGetJobCompletionStage[]
            .get(JobResult.class);
    }
    // end::getJobCompletionStage[]

    // tag::createJobCompletionStage[]
    public CompletionStage<Job> createJob() {
        return iBuilder(webTarget())
            // tag::rxCreateJobCompletionStage[]
            .rx()
            // end::rxCreateJobCompletionStage[]
            .post(null, Job.class);
    }
    // end::createJobCompletionStage[]

    private Invocation.Builder iBuilder(WebTarget target) {
        return target
            .request()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }

    private WebTarget webTarget() {
        if (this.target == null) {
            this.target = ClientBuilder
                .newClient()
                .target(baseUri)
                .path("/jobs");
        }

        return this.target;
    }

}