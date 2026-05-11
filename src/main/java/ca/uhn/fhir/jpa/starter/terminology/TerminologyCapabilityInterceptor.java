package ca.uhn.fhir.jpa.starter.terminology;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.TerminologyCapabilities;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;

@Interceptor
public class TerminologyCapabilityInterceptor {
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public void responseHandling(ResponseDetails theResponseDetails, RequestDetails theRequestDetails) {
		var mode = theRequestDetails.getParameters().get("mode");
		if (mode != null && Arrays.asList(mode).contains("terminology") && theResponseDetails.getResponseResource() instanceof CapabilityStatement) {
			var capabilities = new TerminologyCapabilities();
			capabilities.setVersion("1.0.0");
			capabilities.setName("TerminologyCapabilities");
			capabilities.setTitle("Terminology capabilities");
			capabilities.setStatus(Enumerations.PublicationStatus.ACTIVE);
			capabilities.setDate(Date.from(Instant.parse("2026-05-11T07:30:00Z")));
			capabilities.setExpansion(new TerminologyCapabilities.TerminologyCapabilitiesExpansionComponent()
				.addParameter(new TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent().setName("count"))
				.addParameter(new TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent().setName("displayLanguage"))
				.addParameter(new TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent().setName("excludeNested"))
				.addParameter(new TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent().setName("offset"))
				.addParameter(new TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent().setName("tx-resource"))
				// activeOnly and includeDesignations are accepted by the server
				// but are not applied (they do not have any effect currently).
				// Therefore, they are excluded here.
				// .addParameter(new TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent().setName("activeOnly"))
				// .addParameter(new TerminologyCapabilities.TerminologyCapabilitiesExpansionParameterComponent().setName("includeDesignations"))
			);
			theResponseDetails.setResponseResource(capabilities);
		}
	}
}
