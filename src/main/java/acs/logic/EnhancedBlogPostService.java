package acs.logic;

import reactor.core.publisher.Mono;

public interface EnhancedBlogPostService extends BlogPostService{
    Mono<Void> deleteAll();
}
