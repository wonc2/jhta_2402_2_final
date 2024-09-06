package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.example.jhta_2402_2_final.exception.types.addCompanySourceException;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyChartDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCompanyService {
    private final ProductCompanyDao productCompanyDao;
    private final ProductCompanyUtil productCompanyUtil;

    /* 필요한 리스트 전부 가져옴 */
    public Map<String, Object> findAll(String companyName){
        productCompanyUtil.getCompanyId(companyName);
        Map<String,Object> responseData = new HashMap<>();
        // productList values: { companySourceId, sourceName, sourcePrice, totalQuantity }
        responseData.put("companySourceList",productCompanyDao.getSourcesByCompanyName(companyName));
        responseData.put("sources", productCompanyDao.getAllSources(companyName));
        // responseData values: { List<Map>, List<SourceDto> }
        return responseData;
    }

    /*  생산업체 생산품 목록에 등록 (실제 생산 x, 생산품 등록임) */
    @Transactional
    public Map<String, Object> addSourceToCompany(String companyName, Map<String ,Object> paramData) {
        // 필요한 값 가져오기
        String companyId = productCompanyDao.getCompanyIdByName(companyName);
        paramData.put("companyId", companyId);
        String sourceName = (String) paramData.get("sourceName");
        String sourceId = (String) paramData.get("sourceId");

        // 중복 검사
        if (productCompanyDao.checkDuplicateCompanySource(paramData)) {
            throw new addCompanySourceException("이미 등록된 제품 입니다.", HttpStatus.BAD_REQUEST);
        }
        // 빈 값 검사
        if (sourceId == null && (sourceName == null || sourceName.isEmpty() || sourceName.isBlank())) {
            throw new addCompanySourceException("빈 값 입력 안됩니다.", HttpStatus.BAD_REQUEST);
        }
        // 셀렉트로 sourceId 가져왔으면 if문 스킵 else -> sourceName 으로 sourceId 가져옴 없으면 SOURCE 테이블에 등록
        if (sourceId == null) {
            sourceId = productCompanyDao.getSourceIdByName(sourceName);
            if (sourceId == null) {
                sourceId = UUID.randomUUID().toString();
                productCompanyDao.addSource(sourceId, sourceName);
            }
        }
        paramData.put("sourceId", sourceId);
        // paramData: { companyId, sourceId, sourceName, sourcePrice }
        // 필요값: { companyId, sourceId, sourcePrice }
        productCompanyDao.addSourceToCompany(paramData);
        return findAll(companyName);
    }

    /* 등록된 상품 생산 업체에서 삭제 */
    @Transactional
    public Map<String, Object> deleteSourceFromCompany(String companyName, String companySourceId) {
        productCompanyDao.deleteSourceFromCompany(companySourceId);
        return findAll(companyName);
    }

    /* 생산 창고 리스트 다 가져옴 */
    public List<Map<String, Object>> getWarehouseSources(String companyName) {
        // { "produceDate", "sourceWarehouseId", "sourceQuantity", "sourceName" }
        return productCompanyDao.getWarehouseSources(companyName);
    }

    /* 등록된 상품 생산 -> 창고에 적재 */
    @Transactional
    public List<Map<String, Object>> produceSource(String companyName, Map<String ,Object> paramData) {
        Object sourceQuantityObj = paramData.get("sourceQuantity");
        if (sourceQuantityObj == null) {
            throw new RuntimeException("값이 없습니다 ~");
        }
        try {
            int sourceQuantity = Integer.parseInt(sourceQuantityObj.toString());
            if (sourceQuantity <= 0 ) {
                throw new RuntimeException("한 개 이상 입력해야함 ~");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("정수만 입력하세요 ~");
        }
        productCompanyDao.produceSource(paramData);
        return productCompanyDao.getWarehouseSources(companyName);
    }

    public List<Map<String, Object>> sourcePriceUpdate(String companyName, String companySourceId, Map<String, Object> paramData) {
        paramData.put("companySourceId", companySourceId);
        productCompanyDao.sourcePriceUpdate(paramData);
        return productCompanyDao.getWarehouseSources(companyName);
    }

    public List<Map<String, Object>> getProductOrderList(String companyName, Map<String, Object> paramData) {
        paramData.put("companyName", companyName);

        // product_order 상태 하드코딩 -> 리스트로 내려오게 해야함 ?

        // values: { orderId, sourceName, sourcePrice, quantity, totalPrice, orderDate, orderStatus }
        return productCompanyDao.getProductOrderList(paramData);
    }

    @Transactional
    public List<Map<String, Object>> orderProcess(String companyName, Map<String, Object> paramData) {
        // 필요값: { orderId, orderStatus }
        productCompanyDao.orderProcess(paramData); // product_order 상태 업데이트 -> 입고대기
        productCompanyDao.orderLog(paramData); // product_order_log 인서트
        // 필요값: { sourceQuantity, sourcePriceId }
        productCompanyDao.outboundSource(paramData);
        // 위에서 출고 연산 수행한후 창고의 재료 재고가 < 0 일시 롤백
        if (productCompanyDao.getSourceQuantityFromWarehouse((String) paramData.get("sourcePriceId")) < 0){
            throw new RuntimeException("적재량이 모자람~");
        }

        return getProductOrderList(companyName, paramData);
    }

    public List<ProductCompanyChartDto> getChart(String companyName) {
        return productCompanyDao.getChart(companyName);
    }
}
