package net.nhs.esb.mdtreport.route;

import net.nhs.esb.mdtreport.model.MDTReportComposition;
import net.nhs.esb.openehr.route.CompositionSearchParameters;
import net.nhs.esb.util.DefaultAggregationStrategy;
import net.nhs.esb.util.EmptyList;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class FindMDTReportRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CompositionSearchParameters compositionParameters;

    @Override
    public void configure() throws Exception {

        //@formatter:off
        from("direct:findMDTReports").routeId("FindMDTReports")
            .to("direct:setHeaders")
            .to("direct:createSession")
            .to("direct:getEhrId")
            .setExchangePattern(ExchangePattern.InOut)
            .setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, constant(Boolean.FALSE))
            .setHeader(CxfConstants.OPERATION_NAME, constant("query"))
            .setBody(simple(buildQuery()))
            .to("cxfrs:bean:rsOpenEhr")
            .choice()
                .when(body().isNotNull())
                    .split(simple("${body.resultSet}"), new DefaultAggregationStrategy<MDTReportComposition>())
                        .setHeader("Camel.compositionId", simple("${body[uid]}"))
                        .bean(compositionParameters)
                        .setHeader(CxfConstants.OPERATION_NAME, constant("findComposition"))
                        .to("cxfrs:bean:rsOpenEhr")
                        .convertBodyTo(MDTReportComposition.class)
                    .end()
                .endChoice()
                .otherwise()
                    .removeHeader("CamelHttpResponseCode")
                    .setBody(new EmptyList())
        .end();
        //@formatter:on
    }

    private String buildQuery() {
        return "select a/uid/value as uid " +
                "from EHR e[ehr_id/value='${header.Camel.ehrId}'] " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.report.v1] " +
                "where a/name/value='MDT Output Report'";
    }
}
