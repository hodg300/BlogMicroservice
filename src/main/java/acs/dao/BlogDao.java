package acs.dao;

import acs.data.BlogPostEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.util.Date;

public interface BlogDao extends ReactiveSortingRepository<BlogPostEntity, String> {
    // find all
    Flux<BlogPostEntity> findAllByPostingTimeStampBetween(Sort sort, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
    // find all by user
    Flux<BlogPostEntity> findAllByUser_Email(Sort sort, @Param("email") String email);
    Flux<BlogPostEntity> findAllByUser_Email_AndPostingTimeStampBetween(Sort sort, @Param("email") String email, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
    Flux<BlogPostEntity> findAllByUser_Email_AndLanguage(Sort sort, @Param("email") String email, @Param("language") String language);
    Flux<BlogPostEntity> findAllByUser_Email_AndProduct_Id(Sort sort, @Param("email") String email, @Param("productId") String productId);

    // find all by product
    Flux<BlogPostEntity> findAllByProduct_Id(Sort sort, @Param("productId") String productId);
    Flux<BlogPostEntity> findAllByProduct_Id_AndPostingTimeStampBetween(Sort sort, @Param("productId") String productId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
    Flux<BlogPostEntity> findAllByProduct_Id_AndLanguage(Sort sort, @Param("productId") String productId, @Param("language") String language);

}
