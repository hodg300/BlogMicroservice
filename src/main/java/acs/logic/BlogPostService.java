package acs.logic;

import acs.boundary.BlogPostBoundary;
import acs.logic.utils.FilterType;
import acs.logic.utils.FilterTypePartial;
import acs.logic.utils.SortOrder;

import java.util.List;

public interface BlogPostService {
    BlogPostBoundary createPost (BlogPostBoundary post);

    List<BlogPostBoundary> getAll (FilterTypePartial filterType, String filterValue, int size, int page, String sortBy, SortOrder sortOrder);

    List<BlogPostBoundary> getAllByUser(String email, FilterType filterType, String filterValue, int size, int page, String sortBy, SortOrder sortOrder);

    List<BlogPostBoundary> getAllByProduct(String productId, FilterType filterType, String filterValue, int size, int page, String sortBy, SortOrder sortOrder);
}

