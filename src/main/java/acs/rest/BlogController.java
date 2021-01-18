package acs.rest;

import acs.boundary.BlogPostBoundary;
import acs.logic.BlogPostService;
import acs.logic.utils.FilterTypeProduct;
import acs.logic.utils.FilterTypeUser;
import acs.logic.utils.FilterTypeBlog;
import acs.logic.utils.SortOrder;
import acs.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class BlogController {
    private BlogPostService blogService;

    @Autowired
    public void setBlogPostService(BlogPostService blogService) {
        this.blogService=blogService;
    }

    // POST /blog get boundary retruns Mono
    @RequestMapping(path = "/blog",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BlogPostBoundary createPost(@RequestBody BlogPostBoundary blogPostBoundary) {
        return this.blogService.createPost(blogPostBoundary);
    }

    // GET /blog/byUser/{email}?sortBy={sortArrt}&sortOrder={order}
    // GET /blog/byUser/{email}?filterType=byLanguage&filterValue={language}&sortBy={sortArrt}&sortOrder={order}
    // GET /blog/byUser/{email}?filterType=byCreation&filterValue={timeEnum}&sortBy={sortArrt}&sortOrder={order}
    // GET /blog/byUser/{email}?filterType=byProduct&filterValue={productId}&sortBy={sortArrt}&sortOrder={order}
    @RequestMapping(path="/blog/byUser/{email}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BlogPostBoundary[] getAllByUser (
            @PathVariable("email") String email,
            @RequestParam(name = "filterType", required = false) FilterTypeUser filterTypeUser,
            @RequestParam(name = "filterValue", required = false) String filterValue,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "sortBy", required = false, defaultValue = Constants.POSTING_TIME_STAMP) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") SortOrder sortOrder) {
        return this.blogService.getAllByUser(email, filterTypeUser, filterValue, size, page, sortBy, sortOrder).
                toArray(new BlogPostBoundary[0]);
    }


    // GET /blog/byProduct/{productId}?sortBy={sortArrt}&sortOrder={order}
    // GET /blog/byProduct/{productId}?filterType=byLanguage&filterValue={language}&sortBy={sortArrt}&sortOrder={order}
    // GET /blog/byProduct/{productId}?filterType=byCreation&filterValue={timeEnum}&sortBy={sortArrt}&sortOrder={order}
    @RequestMapping(path="/blog/byProduct/{productId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BlogPostBoundary[] getAllByProduct (
            @PathVariable("productId") String productId,
            @RequestParam(name = "filterType", required = false) FilterTypeProduct filterTypeProduct,
            @RequestParam(name = "filterValue", required = false) String filterValue,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "sortBy", required = false, defaultValue = Constants.POSTING_TIME_STAMP) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") SortOrder sortOrder) {

        return this.blogService.getAllByProduct(productId, filterTypeProduct, filterValue, size, page, sortBy, sortOrder)
                .toArray(new BlogPostBoundary[0]);
    }

    // GET /blog?filterType=byCreation&filterValue={timeEnum}&sortBy={sortArrt}&sortOrder={order}
    // GET /blog?filterType=byCount&filterValue={postsCount}
    @RequestMapping(path="/blog",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BlogPostBoundary[] getAll (
            @RequestParam(name = "filterType", required = true) FilterTypeBlog filterTypeBlog,
            @RequestParam(name = "filterValue", required = true) String filterValue,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "sortBy", required = false, defaultValue = Constants.POSTING_TIME_STAMP) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") SortOrder sortOrder) {

        return this.blogService.getAll(filterTypeBlog, filterValue, size, page, sortBy, sortOrder)
                .toArray(new BlogPostBoundary[0]);
    }
}

