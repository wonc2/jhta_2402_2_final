package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.example.jhta_2402_2_final.exception.types.productCompany.CompanySourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProduceSourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProductCompanyOrderProcessException;
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

    /* CompanySource Table */

    /* 생산품 등록 및 재고 등록 테이블 리스트 가져옴 */
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
            throw new CompanySourceException("이미 등록된 제품 입니다.", HttpStatus.BAD_REQUEST);
        }
        // 빈 값 검사
        if (sourceId == null && (sourceName == null || sourceName.isEmpty() || sourceName.isBlank())) {
            throw new CompanySourceException("빈 값 입력 안됩니다.", HttpStatus.BAD_REQUEST);
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

    /* 등록된 생산품 Delete */
    @Transactional
    public Map<String, Object> deleteSourceFromCompany(String companyName, String companySourceId) {
        productCompanyDao.deleteSourceFromCompany(companySourceId);
        return findAll(companyName);
    }

    /* 등록된 상품 생산 -> 창고에 적재 */
    @Transactional
    public List<Map<String, Object>> produceSource(String companyName, Map<String ,Object> paramData) {
        String sourceQuantityStr = (String) paramData.get("sourceQuantity");
        if (sourceQuantityStr.isBlank()) {
            throw new ProduceSourceException("입력된 값이 없습니다", HttpStatus.BAD_REQUEST);
        }
        try {
            int sourceQuantity = Integer.parseInt(sourceQuantityStr);
            if (sourceQuantity < 1 ) {
                throw new ProduceSourceException("1 이상만 등록할 수 있습니다.", HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            throw new ProduceSourceException("정수만 입력할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
        productCompanyDao.produceSource(paramData);
        return productCompanyDao.getWarehouseSources(companyName);
    }

    /* 재료 가격 수정 */
    @Transactional
    public List<Map<String, Object>> sourcePriceUpdate(String companyName, String companySourceId, Map<String, Object> paramData) {
        if (paramData.get("sourcePrice").equals(paramData.get("oldPrice"))) {
            throw new CompanySourceException("가격 변동 사항이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        paramData.put("companySourceId", companySourceId);
        productCompanyDao.sourcePriceUpdate(paramData);
        productCompanyDao.sourcePriceHistory(paramData);
        return productCompanyDao.getWarehouseSources(companyName);
    }


    /* Source Warehouse Table */

    /* 생산 창고 리스트 다 가져옴 */
    public List<Map<String, Object>> getWarehouseSources(String companyName) {
        // { "produceDate", "sourceWarehouseId", "sourceQuantity", "sourceName" }
        return productCompanyDao.getWarehouseSources(companyName);
    }


    /* Order Table */

    /* 주문 리스트 가져옴 */
    public List<Map<String, Object>> getProductOrderList(String companyName, Map<String, Object> paramData) {
        paramData.put("companyName", companyName);
        // values: { orderId, sourceName, sourcePrice, quantity, totalPrice, orderDate, orderStatus }
        return productCompanyDao.getProductOrderList(paramData);
    }

    // 재료 검색 셀렉트 리스트
    public List<String> selectAllCompanySource(String companyName) {
        return productCompanyDao.selectAllCompanySource(companyName);
    }

    /* 주문 처리 프로세스 */
    @Transactional
    public List<Map<String, Object>> orderProcess(String companyName, Map<String, Object> paramData) {
        // 필요값: { orderId, orderStatus }
        productCompanyDao.orderProcess(paramData); // product_order 상태 업데이트 -> 입고대기
        productCompanyDao.orderLog(paramData); // product_order_log 인서트
        // 필요값: { sourceQuantity, sourcePriceId }
        productCompanyDao.outboundSource(paramData);
        // 위에서 출고 연산 수행한후 창고의 재료 재고가 < 0 일시 롤백
        if (productCompanyDao.getSourceQuantityFromWarehouse((String) paramData.get("sourcePriceId")) < 0){
            throw new ProductCompanyOrderProcessException("적재량이 모자람~", HttpStatus.BAD_REQUEST);
        }

        return getProductOrderList(companyName, paramData);
    }

    public List<ProductCompanyChartDto> getChart(String companyName) {
        return productCompanyDao.getChart(companyName);
    }
    public List<Map<String, Object>> orderChart(String companyName, Map<String, Object> paramData) {
        paramData.put("companyName", companyName);
        return productCompanyDao.orderChart(paramData);
    }
}
