package acs.logic.db;

import acs.boundary.BlogPostBoundary;
import acs.dao.BlogDao;
import acs.exceptions.BadRequestException;
import acs.logic.EnhancedBlogPostService;
import acs.logic.utils.*;
import acs.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class BlogPostServiceWithDB implements EnhancedBlogPostService {
    private BlogDao blogDao;
    private BlogPostConverter converter;

    @Autowired
    public BlogPostServiceWithDB(BlogDao blogDao, BlogPostConverter converter) {
        super();
        this.converter = converter;
        this.blogDao = blogDao;
    }

    @Override
    public Mono<BlogPostBoundary> createPost(BlogPostBoundary post) {
        return this.blogDao.save(converter.toEntity(post)).map(this.converter::fromEntity);
    }

    @Override
    public Flux<BlogPostBoundary> getAll(FilterTypePartial filterType, String filterValue, String sortBy, SortOrder sortOrder) {

        Sort.Direction direction = sortOrder == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;

        if(filterType!=null &&filterValue!=null) {
            // byCreation
            if (filterType.equals(FilterTypePartial.BY_CREATION)) {
                return this.blogDao.findAllByPostingTimeStampBetween(Sort.by(direction,
                        sortBy), getFromDate(filterValue), new Date()).map(this.converter::fromEntity);
            }
            // byCount
            if(filterType.equals(FilterTypePartial.BY_COUNT)){
                long count = Long.parseLong(filterValue);
                if(count <= 0){
                    throw new BadRequestException("count must be greater than zero");
                }
                return this.blogDao.findAll(Sort.by(Sort.Direction.DESC,
                        Constants.POSTING_TIME_STAMP)).limitRequest(count).map(this.converter::fromEntity);
            }
        }
        return this.blogDao.findAll(Sort.by(direction, sortBy))
                .map(this.converter::fromEntity);
    }

    @Override
    public Flux<BlogPostBoundary> getAllByUser(String email, FilterType filterType, String filterValue, String sortBy, SortOrder sortOrder) {
        Sort.Direction direction = sortOrder == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (filterType != null && filterValue != null) {
            // byLanguage
            if (filterType.equals(FilterType.BY_LANGUAGE)) {
                return this.blogDao.findAllByUser_Email_AndLanguage(Sort.by(
                        direction,
                        sortBy), email, filterValue).map(this.converter::fromEntity);
            }
            // byCreation
            if (filterType.equals(FilterType.BY_CREATION)) {
                return this.blogDao.findAllByUser_Email_AndPostingTimeStampBetween(Sort.by(direction,
                        sortBy), email, getFromDate(filterValue), new Date()).map(this.converter::fromEntity);
            }
            // byProduct
            if (filterType.equals(FilterType.BY_PRODUCT)) {
                return this.blogDao.findAllByUser_Email_AndProduct_Id(Sort.by(direction,
                        sortBy), email, filterValue).map(this.converter::fromEntity);
            }
        }
        return this.blogDao.findAllByUser_Email(Sort.by(direction,
                sortBy), email).map(this.converter::fromEntity);
    }

    @Override
    public Flux<BlogPostBoundary> getAllByProduct(String productId, FilterType filterType, String filterValue, String sortBy, SortOrder sortOrder) {
        Sort.Direction direction = sortOrder == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        if(filterType!=null&&filterValue!=null){
            // byLanguage
            if(filterType.equals(FilterType.BY_LANGUAGE)){
                return this.blogDao.findAllByProduct_Id_AndLanguage(Sort.by(direction,
                        sortBy), productId, filterValue).map(this.converter::fromEntity);
            }
            // byCreation
            if(filterType.equals((FilterType.BY_CREATION))){
                return this.blogDao.findAllByProduct_Id_AndPostingTimeStampBetween(Sort.by(direction,
                        sortBy), productId, getFromDate(filterValue), new Date()).map(this.converter::fromEntity);
            }
        }
        return this.blogDao.findAllByProduct_Id(Sort.by(direction,
                sortBy), productId).map(this.converter::fromEntity);
    }

    @Override
    public Mono<Void> deleteAll() {
        return blogDao.deleteAll();
    }

    private Date getFromDate(String timeEnum){
        LocalDateTime localDateTime = LocalDateTime.now();
        if(timeEnum.equals(TimeEnum.LAST_DAY.toString())){
            localDateTime = localDateTime.minusDays(1);
        }
        else if(timeEnum.equals(TimeEnum.LAST_WEEK.toString())){
            localDateTime = localDateTime.minusDays(7);
        }
        else if(timeEnum.equals(TimeEnum.LAST_MONTH.toString())){
            localDateTime = localDateTime.minusDays(30);
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }
}
