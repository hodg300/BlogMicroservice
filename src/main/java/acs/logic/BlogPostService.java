package acs.logic;

import acs.boundary.BlogPostBoundary;
import acs.logic.utils.FilterTypeProduct;
import acs.logic.utils.FilterTypeUser;
import acs.logic.utils.FilterTypeBlog;
import acs.logic.utils.SortOrder;

import java.util.List;

public interface BlogPostService {
    BlogPostBoundary createPost (BlogPostBoundary post);

    List<BlogPostBoundary> getAll (FilterTypeBlog filterType, String filterValue, int size, int page, String sortBy, SortOrder sortOrder);

    List<BlogPostBoundary> getAllByUser(String email, FilterTypeUser filterTypeUser, String filterValue, int size, int page, String sortBy, SortOrder sortOrder);

    List<BlogPostBoundary> getAllByProduct(String productId, FilterTypeProduct filterTypeProduct, String filterValue, int size, int page, String sortBy, SortOrder sortOrder);
}

