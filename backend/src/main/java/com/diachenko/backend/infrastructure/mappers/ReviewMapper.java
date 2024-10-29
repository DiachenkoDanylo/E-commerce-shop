package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    27.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.core.entities.Order;
import com.diachenko.backend.core.entities.Review;
import com.diachenko.backend.core.services.ItemServiceImpl;
import com.diachenko.backend.core.services.OrderServiceImpl;
import com.diachenko.backend.dtos.ReviewDto;
import com.diachenko.backend.dtos.ReviewPayload;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemServiceImpl.class, OrderServiceImpl.class})
public interface ReviewMapper {

    @Mapping(target = "item", source = "itemId", qualifiedByName = "mapItem")
    @Mapping(target = "order", source = "orderId", qualifiedByName = "mapOrder")
    @Mapping(target = "createdAt", ignore = true)
    Review toReview(ReviewDto reviewDto, @Context ItemServiceImpl itemService, @Context OrderServiceImpl orderService);

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "createdAt", source = "createdAt")
    ReviewDto toReviewDto(Review review);

    @Named("mapItem")
    default Item mapItem(Long itemId, @Context ItemServiceImpl itemService) {
        return itemId != null ? itemService.findItemById(itemId) : null;
    }

    @Named("mapOrder")
    default Order mapOrder(Long orderId, @Context OrderServiceImpl orderService) {
        return orderId != null ? orderService.getOrderById(orderId) : null;
    }

    List<ReviewDto> toReviewDtoList(List<Review> reviewList);

    List<Review> toReviewList(List<ReviewDto> reviewDtoList);

    void updateReview(@MappingTarget Review target, Review source);

    ReviewDto toReviewDtoFromPayload(ReviewPayload reviewPayload);

}
