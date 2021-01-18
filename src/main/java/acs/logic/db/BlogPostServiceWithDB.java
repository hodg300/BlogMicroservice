package acs.logic.db;

import acs.boundary.BlogPostBoundary;
import acs.dao.BlogDao;
import acs.exceptions.BadRequestException;
import acs.logic.EnhancedBlogPostService;
import acs.logic.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public BlogPostBoundary createPost(BlogPostBoundary post) {
        return this.converter.fromEntity(this.blogDao.save(converter.toEntity(post)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostBoundary> getAll(FilterTypeBlog filterType, String filterValue, int size, int page, String sortBy, SortOrder sortOrder) {

        Sort.Direction direction = sortOrder == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
            // byCreation
        if (filterType.equals(FilterTypeBlog.BY_CREATION)) {
            return this.blogDao.findAllByPostingTimeStampBetween(getFromDate(filterValue),
                    new Date(), PageRequest.of(page, size, direction, sortBy)).stream()
                    .map(this.converter::fromEntity).collect(Collectors.toList());
        }
        // byCount
        if(filterType.equals(FilterTypeBlog.BY_COUNT)){
            int count = Integer.parseInt(filterValue);
            if(count <= 0){
                throw new BadRequestException("count must be greater than zero");
            }
            return this.blogDao.findAll(PageRequest.of(page, count, direction, sortBy)).stream()
                    .map(this.converter::fromEntity).collect(Collectors.toList());
        }

        throw new BadRequestException("FilterType must be specified");
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostBoundary> getAllByUser(String email, FilterTypeUser filterTypeUser, String filterValue, int size, int page, String sortBy, SortOrder sortOrder) {
                Sort.Direction direction = sortOrder == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (filterTypeUser != null && filterValue != null) {
            // byLanguage
            if (filterTypeUser.equals(FilterTypeUser.BY_LANGUAGE)) {
                return this.blogDao.findAllByUser_Email_AndLanguage(email, filterValue,
                        PageRequest.of(page, size, direction, sortBy)).stream().map(this.converter::fromEntity).collect(Collectors.toList());
            }
            // byCreation
            if (filterTypeUser.equals(FilterTypeUser.BY_CREATION)) {
                return this.blogDao.findAllByUser_Email_AndPostingTimeStampBetween(email, getFromDate(filterValue), new Date(),
                        PageRequest.of(page, size, direction, sortBy)).stream().map(this.converter::fromEntity).collect(Collectors.toList());
            }
            // byProduct
            if (filterTypeUser.equals(FilterTypeUser.BY_PRODUCT)) {
                return this.blogDao.findAllByUser_Email_AndProduct_Id(email, filterValue,
                        PageRequest.of(page, size, direction, sortBy)).stream().map(this.converter::fromEntity).collect(Collectors.toList());
            }
        }
        return this.blogDao.findAllByUser_Email(email,
                PageRequest.of(page, size, direction, sortBy)).stream().map(this.converter::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogPostBoundary> getAllByProduct(String productId, FilterTypeProduct filterTypeProduct, String filterValue, int size, int page, String sortBy, SortOrder sortOrder) {
                Sort.Direction direction = sortOrder == SortOrder.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        if(filterTypeProduct !=null&&filterValue!=null){
            // byLanguage
            if(filterTypeProduct.equals(FilterTypeUser.BY_LANGUAGE)){
                return this.blogDao.findAllByProduct_Id_AndLanguage(productId, filterValue,
                        PageRequest.of(page, size, direction, sortBy)).stream().map(this.converter::fromEntity)
                        .collect(Collectors.toList());
            }
            // byCreation
            if(filterTypeProduct.equals((FilterTypeUser.BY_CREATION))){
                return this.blogDao.findAllByProduct_Id_AndPostingTimeStampBetween(productId, getFromDate(filterValue), new Date(),
                        PageRequest.of(page, size, direction, sortBy)).stream().map(this.converter::fromEntity)
                        .collect(Collectors.toList());
            }
        }
        return this.blogDao.findAllByProduct_Id(productId,
                PageRequest.of(page, size, direction, sortBy)).stream().map(this.converter::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteAll() {
        blogDao.deleteAll();
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
