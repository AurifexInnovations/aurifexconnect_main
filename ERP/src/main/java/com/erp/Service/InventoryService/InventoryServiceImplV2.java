package com.erp.Service.InventoryService;

import com.erp.Dto.Request.InventoryRequestV2;
import com.erp.Dto.Request.InventoryUpdateRequestV2;
import com.erp.Dto.Response.*;
import com.erp.Dto.VarientDto;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.Inventory.InventoryMapper;
import com.erp.Model.*;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import com.erp.Repository.Inventory.InventoryV2DocumentRepository;
import com.erp.Repository.Service.ServiceRepository;
import com.erp.Repository.Tax.TaxRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.inerfaces.S3StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.bcel.generic.StackInstruction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InventoryServiceImplV2 implements InventoryServiceV2 {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final BranchRepository branchRepository;
    private final TaxRepository taxRepository;
    private final InventoryRepositoryV2 inventoryRepositoryV2;
    private final InventoryMapper inventoryMapper;
    private final ServiceRepository serviceRepository;
    private final UserIdentity userIdentity;
    private final UserRepository userRepository;
    private final S3StorageService s3StorageService;
    private final S3Presigner s3Presigner;
    private final InventoryV2DocumentRepository inventoryV2DocumentRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ResultDto<InventoryResponseV2> addInventory(String inventoryRequest, MultipartFile[] files) throws JsonProcessingException {
        List<InventoryResponseV2> responseV2List = new ArrayList<>();

        InventoryRequestV2 request = objectMapper.readValue(inventoryRequest, InventoryRequestV2.class);

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found!!"));

        Tax tax = taxRepository.findById(request.getTaxId())
                .orElseThrow(() -> new TaxNotFoundException("Tax Not Found!!"));

        if (request.isRentable()) {
            InventoryV2 inventory = new InventoryV2();

            inventory.setItemName(request.getItemName());
            inventory.setItemDescription(request.getItemDescription());
            inventory.setLowStockThreshold(request.getLowStockThreshold());

            inventory.setBranch(branch);

            inventory.setBrandName(request.getBrandName());
            inventory.setProductCategories(request.getProductCategories());
            inventory.setHsnCode(request.getHsnCode());
            inventory.setSkuCode(request.getSkuCode());
            inventory.setEan(request.getEan());
            inventory.setReturnable(request.isReturnable());
            inventory.setProductStatus(request.getProductStatus());
            inventory.setActive(request.isActive());

            inventory.setTax(tax);

            inventory.setRentable(true);
            inventory.setDefaultRentalRate(request.getDefaultRentalRate());
            inventory.setDefaultDepositAmount(request.getDefaultDepositAmount());
            inventory.setInsuranceValue(request.getInsuranceValue());
            inventory.setRentalRateFrequency(request.getRentalRateFrequency());
            inventory.setRentalProductQuantity(request.getRentalProductQuantity());
            inventory.setRentalProductStatus(request.getRentalProductStatus());

            InventoryV2 saved = inventoryRepositoryV2.save(inventory);

            inventoryFileUploading(files, saved);

            InventoryResponseV2 responseV2 = inventoryMapper.ToInventoryResponseV2(saved);
            responseV2.setBranchId(branch.getBranchId());
            responseV2.setTaxId(tax.getId());

            List<String> s3Keys = toRespectiveUrls(saved);
            responseV2.setDocumentsUrls(s3Keys);

            responseV2List.add(responseV2);

        } else {
            for (VarientDto varient : request.getVariants()) {
                InventoryV2 inventory = new InventoryV2();

                inventory.setItemName(request.getItemName());
                inventory.setItemDescription(request.getItemDescription());
                inventory.setLowStockThreshold(request.getLowStockThreshold());

                inventory.setBranch(branch);

                inventory.setBrandName(request.getBrandName());
                inventory.setProductCategories(request.getProductCategories());
                inventory.setHsnCode(request.getHsnCode());
                inventory.setSkuCode(request.getSkuCode());
                inventory.setEan(request.getEan());
                inventory.setReturnable(request.isReturnable());
                inventory.setProductStatus(request.getProductStatus());
                inventory.setActive(request.isActive());

                inventory.setTax(tax);

                inventory.setStockQuantity(varient.getStockQuantity());
                inventory.setSellingPriceType(varient.getSellingPriceType());
                inventory.setSellingPrice(varient.getSellingPrice());
                inventory.setPurchasePriceType(varient.getPurchasePriceType());
                inventory.setPurchasePrice(varient.getPurchasePrice());
                inventory.setUnitType(varient.getUnitType());
                inventory.setUnitTypeValue(varient.getUnitTypeValue());
                inventory.setMeasurement(varient.getMeasurement());
                inventory.setMeasurementType(varient.getMeasurementType());
                inventory.setExpiryDate(varient.getExpiryDate());
                inventory.setActive(true);

                inventory.setItemId(null);
                InventoryV2 saved = inventoryRepositoryV2.save(inventory);

                inventoryFileUploading(files, saved);

                InventoryResponseV2 responseV2 = inventoryMapper.ToInventoryResponseV2(saved);
                responseV2.setBranchId(branch.getBranchId());
                responseV2.setTaxId(tax.getId());

                List<String> s3Keys = toRespectiveUrls(saved);
                responseV2.setDocumentsUrls(s3Keys);

                responseV2List.add(responseV2);
            }
        }

        ResultDto<InventoryResponseV2> resultDto = new ResultDto<>();
        resultDto.setResults(responseV2List);
        resultDto.setCount(responseV2List.size());
        return resultDto;
    }

    private void inventoryFileUploading(MultipartFile[] files, InventoryV2 saved){

        if (files == null || files.length == 0) return;
        List<FileUploadResponse> list = s3StorageService.uploadFile(files, "inventory");
        List<InventoryV2Document> documents = new ArrayList<>();

        for(FileUploadResponse fileUploadResponse : list){
            InventoryV2Document document = new InventoryV2Document();
            document.setDocumentName(fileUploadResponse.getFileName());
            document.setDocumentUrl(fileUploadResponse.getS3Key());
            document.setInventoryV2(saved);
            documents.add(document);
        }
        inventoryV2DocumentRepository.saveAll(documents);
    }

    @Override
    public InventoryResponseV2 deleteInventory(long itemId) {
        InventoryV2 inventoryV2 = inventoryRepositoryV2.findById(itemId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found !!"));

        for (com.erp.Model.Service s : inventoryV2.getServices()) {
            s.getInventories().remove(inventoryV2);
        }

        inventoryV2.getServices().clear();

        serviceRepository.saveAll(inventoryV2.getServices());

        InventoryResponseV2 inventoryResponseV2 = inventoryMapper.ToInventoryResponseV2(inventoryV2);
        inventoryResponseV2.setTaxId(inventoryV2.getTax().getId());
        inventoryResponseV2.setBranchId(inventoryV2.getBranch().getBranchId());

        inventoryRepositoryV2.delete(inventoryV2);

        return inventoryResponseV2;
    }

    @Override
    public InventoryResponseV2 updateInventory(InventoryUpdateRequestV2 request) {
        InventoryV2 updated = inventoryRepositoryV2.findById(request.getItemId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found !!"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found"));

        Tax tax = taxRepository.findById(request.getTaxId())
                .orElseThrow(() -> new TaxNotFoundException("Tax Not Found"));

        InventoryV2 newInventory = inventoryMapper.toInventoryV2(request);
        inventoryMapper.convertToEntityToUpdated(newInventory, updated);

        updated.setBranch(branch);
        updated.setTax(tax);

        inventoryRepositoryV2.save(updated);

        InventoryResponseV2 responseV2 = inventoryMapper.ToInventoryResponseV2(updated);
        responseV2.setBranchId(updated.getBranch().getBranchId());
        responseV2.setTaxId(updated.getTax().getId());
        return responseV2;
    }

    @Override
    public ResultDto<InventoryResponseV2> getAll() {
        List<InventoryV2> inventories = inventoryRepositoryV2.findAll();

        List<InventoryResponseV2> inventoryResponseV2s = new ArrayList<>();
        for(InventoryV2 response : inventories){
            InventoryResponseV2 inventoryResponseV2 = inventoryMapper.ToInventoryResponseV2(response);
            inventoryResponseV2.setBranchId(response.getBranch().getBranchId());
            inventoryResponseV2.setTaxId(response.getTax().getId());
            inventoryResponseV2s.add(inventoryResponseV2);
        }

        ResultDto<InventoryResponseV2> resultDto = new ResultDto<>();
        resultDto.setCount(inventoryResponseV2s.size());
        resultDto.setResults(inventoryResponseV2s);
        return resultDto;
    }

    @Override
    public ResultDto<InventoryResponseV2> getAllBranchWise() {
        GenericUser genericUser = userIdentity.getCurrentUser();

        User user = userRepository.findByEmail(genericUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        List<InventoryResponseV2> responseV2s = new ArrayList<>();
        for(InventoryV2 s : inventoryRepositoryV2.findByBranch_BranchId(user.getBranch().getBranchId())){
            InventoryResponseV2 inventoryResponseV2 = inventoryMapper.ToInventoryResponseV2(s);
            inventoryResponseV2.setBranchId(s.getBranch().getBranchId());
            inventoryResponseV2.setTaxId(s.getTax().getId());
            responseV2s.add(inventoryResponseV2);
        }
        ResultDto<InventoryResponseV2> resultDto = new ResultDto<>();

        resultDto.setResults(responseV2s != null ? responseV2s : List.of());
        resultDto.setCount(responseV2s != null ? responseV2s.size() : 0);

        return resultDto;
    }

    @Override
    public ResultDto<DropDown> getDropDown() {
        GenericUser genericUser = userIdentity.getCurrentUser();

        User user = userRepository.findByEmail(genericUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        List<DropDown> list = new ArrayList<>();
        for(InventoryV2 inventoryV2 : inventoryRepositoryV2.findByBranch_BranchIdAndRentableFalseAndActiveTrue(user.getBranch().getBranchId())){
            list.add(new DropDown(inventoryV2.getItemId(), inventoryV2.getItemName()));
        }

        ResultDto<DropDown> resultDto = new ResultDto<>();

        resultDto.setCount(list.size());
        resultDto.setResults(list);

        return resultDto;
    }

    private String generatePresignedUrl(String s3Key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(p -> p
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));

        return presignedRequest.url().toString();
    }

    private List<String> toRespectiveUrls(InventoryV2 inventoryV2){
        List<String> files = inventoryV2DocumentRepository.findDocumentUrlsByItemId(inventoryV2.getItemId());
        List<String> imageUrls = files.stream()
                .map(this::generatePresignedUrl)
                .toList();

        return imageUrls;
    }

    @Override
    public ResultDto<DropDown> getDropDownEquipment() {

        GenericUser genericUser = userIdentity.getCurrentUser();

        User user = userRepository.findByEmail(genericUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        List<DropDown> list = new ArrayList<>();
        for(InventoryV2 inventoryV2 : inventoryRepositoryV2.findByBranch_BranchIdAndRentableTrueAndActiveTrue(user.getBranch().getBranchId())){
            list.add(new DropDown(inventoryV2.getItemId(), inventoryV2.getItemName()));
        }

        ResultDto<DropDown> resultDto = new ResultDto<>();

        resultDto.setCount(list.size());
        resultDto.setResults(list);

        return resultDto;

    }

    @Override
    public InventoryResponseV2 getInventoryById(Long id) {
        InventoryV2 inventoryV2 = inventoryRepositoryV2.findByItemIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Not Found"));

        InventoryResponseV2 inventoryResponseV2 = inventoryMapper.ToInventoryResponseV2(inventoryV2);
        List<String> urls = toRespectiveUrls(inventoryV2);

        inventoryResponseV2.setDocumentsUrls(urls);
        inventoryResponseV2.setBranchId(inventoryV2.getBranch().getBranchId());
        inventoryResponseV2.setTaxId(inventoryV2.getTax().getId());

        return inventoryResponseV2;
    }

    @Override
    public InventoryFormResponse getInventoryFormById(Long id) {
        InventoryV2 inventoryV2 = inventoryRepositoryV2.findByItemIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Not Found"));

        InventoryFormResponse response = new InventoryFormResponse();

        response.setItemId(inventoryV2.getItemId());
        response.setSkuCode(inventoryV2.getSkuCode());
        response.setSellingPrice(inventoryV2.getSellingPrice());
        response.setTaxRate(inventoryV2.getTax().getTaxRate());
        response.setItemName(inventoryV2.getItemName());

        List<String> urls = toRespectiveUrls(inventoryV2);
        response.setDocumentUrls(urls);

        return response;
    }
}
