package uk.gov.hmcts.cp.bulkresulting.openapi;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.cp.bulkresulting.openapi.api.ConfigApi;
import uk.gov.hmcts.cp.bulkresulting.openapi.api.HearingsApi;
import uk.gov.hmcts.cp.bulkresulting.openapi.api.RootApi;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.BulkResultingConfig;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.BusinessType;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.CourtSession;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.EligibleHearing;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.HearingCase;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.HearingDefendant;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.HearingGroup;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.HearingOffence;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.HearingSearchResponse;
import uk.gov.hmcts.cp.bulkresulting.openapi.model.Problem;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OpenApiObjectsTest {

    @Test
    void generated_apis_should_have_expected_methods() {
        assertThat(RootApi.class).hasDeclaredMethods("getRoot");
        assertThat(HearingsApi.class).hasDeclaredMethods("searchHearings");
        assertThat(ConfigApi.class).hasDeclaredMethods("getConfig");
    }

    @Test
    void search_response_should_have_expected_fields() {
        assertThat(HearingSearchResponse.class)
                .hasDeclaredFields("page", "pageSize", "totalPages", "totalMatched", "displayedCount",
                        "inFlightCount", "groups");
    }

    @Test
    void hearing_group_should_have_expected_fields() {
        assertThat(HearingGroup.class).hasDeclaredFields("hearingDate", "courtRoomId", "courtRoomName", "hearings");
    }

    @Test
    void eligible_hearing_should_have_expected_fields() {
        assertThat(EligibleHearing.class)
                .hasDeclaredFields("hearingId", "hearingType", "failedToShare", "defendantName", "firstName",
                        "lastName", "organisationName", "dateOfBirth", "accountNumber", "urn", "offenceTitles",
                        "cases");
    }

    @Test
    void hearing_case_should_have_expected_fields() {
        assertThat(HearingCase.class).hasDeclaredFields("caseId", "urn", "accountNumber", "defendants");
    }

    @Test
    void hearing_defendant_should_have_expected_fields() throws NoSuchFieldException {
        assertThat(HearingDefendant.class)
                .hasDeclaredFields("defendantId", "firstName", "lastName", "organisationName", "dateOfBirth", "offences");
        assertThat(HearingDefendant.class.getDeclaredField("dateOfBirth").getType()).isEqualTo(LocalDate.class);
    }

    @Test
    void hearing_offence_should_have_expected_fields() {
        assertThat(HearingOffence.class).hasDeclaredFields("offenceId", "offenceCode", "offenceTitle");
    }

    @Test
    void config_should_have_expected_fields() {
        assertThat(BulkResultingConfig.class).hasDeclaredFields("maxBatchSize", "searchWindowDays");
    }

    @Test
    void problem_should_have_expected_fields() {
        assertThat(Problem.class).hasDeclaredFields("type", "title", "status", "detail", "instance", "code");
    }

    @Test
    void court_session_should_have_expected_values() {
        assertThat(CourtSession.values())
                .extracting(CourtSession::getValue)
                .containsExactly("ANY", "AM", "PM", "AD");
    }

    @Test
    void business_type_should_have_expected_values() {
        assertThat(BusinessType.values())
                .extracting(BusinessType::getValue)
                .containsExactly("ENF", "ENF_AUTO");
        assertThat(BusinessType.fromValue("ENF_AUTO")).isEqualTo(BusinessType.ENF_AUTO);
        assertThatThrownBy(() -> BusinessType.fromValue("CRIME")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void search_response_builder_should_nest_groups_cases_defendants_and_offences() {
        final UUID hearingId = UUID.randomUUID();
        final HearingOffence offence = HearingOffence.builder()
                .offenceId(UUID.randomUUID()).offenceCode("CODE01").offenceTitle("Offence title").build();
        final HearingDefendant defendant = HearingDefendant.builder()
                .defendantId(UUID.randomUUID()).firstName("Ann").lastName("Example")
                .dateOfBirth(LocalDate.of(1990, 1, 1)).offences(List.of(offence)).build();
        final HearingCase hearingCase = HearingCase.builder()
                .caseId(UUID.randomUUID()).urn("URN0001").accountNumber("ACC0001")
                .defendants(List.of(defendant)).build();
        final EligibleHearing hearing = EligibleHearing.builder()
                .hearingId(hearingId).hearingType("Enforcement").failedToShare(false)
                .defendantName("Ann Example").firstName("Ann").lastName("Example")
                .urn("URN0001").accountNumber("ACC0001").offenceTitles(List.of("Offence title"))
                .cases(List.of(hearingCase)).build();
        final HearingGroup group = HearingGroup.builder()
                .hearingDate(LocalDate.of(2026, 12, 1)).courtRoomId(UUID.randomUUID()).courtRoomName("Courtroom 01")
                .hearings(List.of(hearing)).build();

        final HearingSearchResponse response = HearingSearchResponse.builder()
                .page(1).pageSize(100).totalPages(1).totalMatched(1).displayedCount(1).inFlightCount(0)
                .groups(List.of(group)).build();

        assertThat(response.getGroups().getFirst().getHearings()).singleElement()
                .satisfies(h -> {
                    assertThat(h.getHearingId()).isEqualTo(hearingId);
                    assertThat(h.getCases().getFirst().getDefendants().getFirst().getOffences())
                            .containsExactly(offence);
                });
    }
}
