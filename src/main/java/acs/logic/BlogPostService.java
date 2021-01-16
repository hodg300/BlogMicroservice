package acs.logic;

import acs.boundary.BlogPostBoundary;
import acs.logic.utils.FilterType;
import acs.logic.utils.FilterTypePartial;
import acs.logic.utils.SortOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlogPostService {
    Mono<BlogPostBoundary> createPost (BlogPostBoundary post);

    Flux<BlogPostBoundary> getAll (FilterTypePartial filterType, String filterValue, String sortBy, SortOrder sortOrder);

    Flux<BlogPostBoundary> getAllByUser(String email, FilterType filterType, String filterValue, String sortBy, SortOrder sortOrder);

    Flux<BlogPostBoundary> getAllByProduct(String productId, FilterType filterType, String filterValue, String sortBy, SortOrder sortOrder);
}

