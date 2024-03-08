package com.mctolueneam.locationservice.helpers;

import org.junit.jupiter.api.Test;

import com.mctoluene.locationservice.helpers.LocationHelper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LocationHelperTest {

    @Test
    void validatePage_valid() {
        // given:
        int page = 1;
        int size = 1;

        boolean isPageableValid = LocationHelper.validatePage(page, size);

        assertThat(isPageableValid).isTrue();
    }

    @Test
    void validatePage_invalid_size() {
        // given:
        int page = 1;
        int size = 0;

        boolean isPageableValid = LocationHelper.validatePage(page, size);

        assertThat(isPageableValid).isFalse();
    }

    @Test
    void validatePage_invalid_page() {
        // given:
        int page = 0;
        int size = 1;

        boolean isPageableValid = LocationHelper.validatePage(page, size);

        assertThat(isPageableValid).isFalse();
    }

    @Test
    void validatePage_invalid_page_size() {
        // given:
        int page = 0;
        int size = 0;

        boolean isPageableValid = LocationHelper.validatePage(page, size);

        assertThat(isPageableValid).isFalse();
    }
}