package org.garden.items.adapter.out.rest_client;

import jakarta.ws.rs.WebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Optional;
import java.util.function.Predicate;

class Is404Exception implements Predicate<Throwable> {

    static final Is404Exception IS_404 = new Is404Exception();

    private Is404Exception() {}

    @Override
    public boolean test(Throwable throwable) {
        return Optional.ofNullable(throwable)
                .filter(WebApplicationException.class::isInstance)
                .map(WebApplicationException.class::cast)
                .map(WebApplicationException::getResponse)
                .filter(response -> response.getStatus() == RestResponse.Status.NOT_FOUND.getStatusCode()).isPresent();
    }
}
