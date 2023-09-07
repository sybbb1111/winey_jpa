package com.green.winey_final.recommend;

import com.green.winey_final.common.config.security.AuthenticationFacade;
import com.green.winey_final.common.entity.*;
import com.green.winey_final.recommend.model.RecommendVo;
import com.green.winey_final.recommend2.model.RecommendRes2;
import com.green.winey_final.recommend2.model.UserinfoDto2;
import com.green.winey_final.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {

    private final UserInfoEntityRepository wineRepository;
    private final AuthenticationFacade facade;
    private final UserCategoryRepository userCategory;
    private final UserCountryRepository userCountry;
    private final UserPriceRangeRepository userPrice;
    private final UserSmallCategoryRepository userSmall;
    private final UserAromaRepository userAroma;
    private final RecommendMapper mapper;


    public List<Long> selRecommend(RecommendRes2 res) {
        Long user = facade.getLoginUserPk();
        List<Long> result = mapper.selRecommend(res);
        UserinfoDto2 dto = new UserinfoDto2();
        dto.setUserId(user);
        dto.setProductId(result);


        List<UserCategoryEntity> categoryList = new ArrayList<>();
        if (res.getCategoryId() != null) {
            for (Long categoryId : res.getCategoryId()) {
                UserCategoryEntity category = new UserCategoryEntity();
                category.setUserEntity(UserEntity.builder()
                        .userId(user)
                        .build());
                category.setCategoryId(categoryId);
                categoryList.add(category);
            }
        } else {
            UserCategoryEntity category = new UserCategoryEntity();
            category.setUserEntity(UserEntity.builder()
                    .userId(user)
                    .build());
            userCategory.save(category);
        }
        userCategory.saveAll(categoryList);


        List<UserCountryEntity> countryList = new ArrayList<>();
        if (res.getCountryId() != null) {
            for (Long countryId : res.getCountryId()) {
                UserCountryEntity country = new UserCountryEntity();
                country.setUserEntity(UserEntity.builder()
                        .userId(user)
                        .build());
                country.setCountryId(countryId);
                countryList.add(country);
            }
        } else {
            UserCountryEntity country = new UserCountryEntity();
            country.setUserEntity(UserEntity.builder()
                    .userId(user)
                    .build());
            userCountry.save(country);
        }
        userCountry.saveAll(countryList);


        List<UserPriceRangeEntity> priceRangeList = new ArrayList<>();
        if (res.getPriceRange() != null) {
            for (Long priceR : res.getPriceRange()) {
                UserPriceRangeEntity priceRange =new UserPriceRangeEntity();
                priceRange.setUserEntity(UserEntity.builder()
                        .userId(user)
                        .build());
                priceRange.setPriceRange(priceR);
                priceRangeList.add(priceRange);
            }
        } else {
            UserPriceRangeEntity priceRange =new UserPriceRangeEntity();
            priceRange.setUserEntity(UserEntity.builder()
                    .userId(user)
                    .build());
            userPrice.save(priceRange);
        }
        userPrice.saveAll(priceRangeList);


        List<UserSmallCategoryEntity> userSmallList = new ArrayList<>();
        if (res.getSmallCategoryId() != null) {
            for (Long smallId : res.getSmallCategoryId()) {
                UserSmallCategoryEntity smallCategory=new UserSmallCategoryEntity();
                smallCategory.setUserEntity(UserEntity.builder()
                        .userId(user)
                        .build());
                smallCategory.setSmallCategoryId(smallId);
                userSmallList.add(smallCategory);
            }
        } else {
            UserSmallCategoryEntity smallCategory=new UserSmallCategoryEntity();
            smallCategory.setUserEntity(UserEntity.builder()
                    .userId(user)
                    .build());
            userSmall.save(smallCategory);
        }
        userSmall.saveAll(userSmallList);


        List<UserAromaEntity> aromaList = new ArrayList<>();
        if (res.getAromaCategoryId() != null) {
            for (Long aromaId : res.getAromaCategoryId()) {
                UserAromaEntity aroma = new UserAromaEntity();
                aroma.setUserEntity(UserEntity.builder()
                        .userId(user)
                        .build());
                aroma.setAromaCategoryId(aromaId);
                aromaList.add(aroma);
            }
        } else {
            UserAromaEntity aroma = new UserAromaEntity();
            aroma.setUserEntity(UserEntity.builder()
                    .userId(user)
                    .build());
            userAroma.save(aroma);
        }
        userAroma.saveAll(aromaList);

        mapper.insUserinfo(dto);
        return result;
    }

    public List<Long> selUserinfo() {
        Long userPk = facade.getLoginUserPk();
        UserEntity entity = new UserEntity();
        entity.setUserId(userPk);
        List<UserInfoEntity> list = wineRepository.findByUserEntity(entity);
        List<Long> productPK = new ArrayList<>();
        for (UserInfoEntity userInfoEntity : list) {
            productPK.add(userInfoEntity.getProductEntity().getProductId());
        }
        return productPK;
    }

    public RecommendVo selUserRecommend() {
        Long userPk = facade.getLoginUserPk();
        UserEntity entity = new UserEntity();
        entity.setUserId(userPk);

        List<Long> catelist = userCategory.findCategoryIdByUserEntity(entity).stream()
                .map(UserCategoryEntity::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> coulist = userCountry.findCountryIdByUserEntity(entity).stream()
                .map(UserCountryEntity::getCountryId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> smallCatelist = userSmall.findSmallCategoryIdByUserEntity(entity).stream()
                .map(UserSmallCategoryEntity::getSmallCategoryId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> priceRalist = userPrice.findPriceRangeByUserEntity(entity).stream()
                .map(UserPriceRangeEntity::getPriceRange)
                .distinct()
                .collect(Collectors.toList());

        List<Long> aromalist = userAroma.findAromaCategoryIdByUserEntity(entity).stream()
                .map(UserAromaEntity::getAromaCategoryId)
                .distinct()
                .collect(Collectors.toList());

        return RecommendVo.builder()
                .categoryId(catelist)
                .countryId(coulist)
                .smallCategoryId(smallCatelist)
                .priceRange(priceRalist)
                .aromaCategoryId(aromalist)
                .build();
    }

    }







