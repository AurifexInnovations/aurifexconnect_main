package com.erp.Mapper.Inventory;


import com.erp.Dto.Request.ProductRequest;
import com.erp.Model.Inventory;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Inventory toEntity(ProductRequest dto) {
        if (dto == null) {
            return null;
        }

        Inventory entity = new Inventory();
        entity.setItemId(dto.getItemId());
        entity.setItemName(dto.getItemName());
        entity.setBrandName(dto.getBrandName());
        entity.setProductCategories(dto.getCategories());
        entity.setHsnCode(dto.getHsnCode());
        entity.setSkuCode(dto.getSkuCode());
        entity.setEan(dto.getEan());
        entity.setReturnable(dto.isReturnable());
        entity.setTaxId(dto.getTaxId());
        entity.setProductStatus(dto.getProductStatus());
        entity.setActive(Boolean.TRUE);
        entity.setBranchId(dto.getBranchId());
        return entity;
    }

    public void updateProductFromRequest(ProductRequest dto, Inventory entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setItemName(dto.getItemName());
        entity.setBrandName(dto.getBrandName());
        entity.setProductCategories(dto.getCategories());
        entity.setHsnCode(dto.getHsnCode());
        entity.setSkuCode(dto.getSkuCode());
        entity.setEan(dto.getEan());
        entity.setReturnable(dto.isReturnable());
        entity.setTaxId(dto.getTaxId());
        entity.setActive(Boolean.TRUE);
        entity.setProductStatus(dto.getProductStatus());
        entity.setBranchId(dto.getBranchId());
    }
}
