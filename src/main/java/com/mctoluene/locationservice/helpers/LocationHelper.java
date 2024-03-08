package com.mctoluene.locationservice.helpers;

import com.mctoluene.locationservice.domains.dtos.Filtering;
import com.mctoluene.locationservice.domains.enums.EnumResponseAdapter;
import com.mctoluene.locationservice.domains.enums.Sorting;
import com.mctoluene.locationservice.domains.enums.Status;
import com.mctoluene.locationservice.exceptions.PageableException;
import com.mctolueneam.commons.exceptions.NotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class LocationHelper {

    private LocationHelper() {
    }

    public static boolean validatePage(int page, int size) {
        return page >= 1 && size > 0;
    }

    public static String getEnabledDisabled(String statusString) {
        Optional<EnumResponseAdapter> status = EnumResponseAdapter.getStatus(statusString);
        String enabledDisabled = null;
        if (status.isPresent()) {
            enabledDisabled = status.get().toEnabledDisabled();
        }
        return enabledDisabled;
    }

    public static Mono<Filtering> filteringCheck(Integer page, Integer size, String status, String name, String sort,
            String sortKey, String messageError) {
        if (!validatePage(page, size))
            return Mono.error(new PageableException(messageError));

        if (!status.isEmpty()) {
            status = status.toUpperCase().trim();
            var optionalStatus = Status.getStatus(status);
            if (optionalStatus.isEmpty())
                return Mono.error(new NotFoundException("status does not exist"));
        }

        if (!sort.isEmpty()) {
            sort = sort.toUpperCase();
            var optionalStatus = Sorting.getSort(sort);
            if (optionalStatus.isEmpty())
                return Mono.error(new NotFoundException("sort params does not exist"));
        } else
            sort = "DESC";

        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.toUpperCase()), sortKey);
        PageRequest request = PageRequest.of(page - 1, size, Sort.by(order));
        return Mono.just(Filtering.builder()
                .pageable(request)
                .name(name.trim())
                .status(status)
                .build());
    }
}
