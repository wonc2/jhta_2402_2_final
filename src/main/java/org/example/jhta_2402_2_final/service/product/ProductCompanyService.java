package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.example.jhta_2402_2_final.exception.types.productCompany.CompanySourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProduceSourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProductCompanyOrderProcessException;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyChartDto;
import org.example.jhta_2402_2_final.model.dto.productCompany.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCompanyService {
    private final ProductCompanyDao productCompanyDao;
    private final SimpMessagingTemplate messagingTemplate;

    // todo:
    //  1. 소켓: 작업중 접근 금지 걸기, 웹소켓 적용한거 코드 이해하기
    //  2. 예외처리: 가격 수정 예외처리 해야함 (음수, 0 x), 동시성문제 처리 안한거 체크
    //  3. test: TDD 작성하기
    //  4. model: 생산품 등록 로직 파라미터 map -> dto
    //  5. js: 차트 초기화 두개 나눠져있는데 합칠 수 있는지 확인, 라벨 제대로 못쓰고 있음 확인

    // 유저 인증
    public String getCompanyIdByUserId(String userId) {
        return productCompanyDao.getCompanyIdByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("생산 업체 직원이 아니거나 권한이 없음"));
    }

    /* CompanySource Table */

    /* 생산품 등록 및 재고 등록 테이블 리스트 가져옴 */
    public CompanySourceTableDto findAll(String companyId) {

        List<CompanySourceDto> companySourceList = productCompanyDao.getSourcesByCompanyName(companyId);
        List<SourceDto> sources = productCompanyDao.getAllSources(companyId);

        CompanySourceTableDto response = CompanySourceTableDto.builder()
                .companySourceList(companySourceList).sources(sources).build();

        return response;
    }

    /*  생산업체 생산품 목록에 등록 (실제 생산 x, 생산품 등록임) */
    @Transactional
    public void addSourceToCompany(AddSourceDto addSourceDto) {
        // 필요한 값 가져오기
        String sourceName = addSourceDto.getSourceName();
        String sourceId = addSourceDto.getSourceId();

        // : 생산품 등록 중복 검사 한 업체에 동일한 이름을 가진 제품 중복 등록 불가능 + 동시에 등록시 누군가 먼저 등록해도 불가능
        if (productCompanyDao.checkDuplicateCompanySource(addSourceDto)) {
            throw new CompanySourceException("이미 등록된 제품 입니다.", HttpStatus.CONFLICT);
        }
        // : 제품 이름으로 공백이나 null 값 입력 불가능
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
            addSourceDto = addSourceDto.toBuilder().sourceId(sourceId).build();
        }
        // 생산품 등록
        productCompanyDao.addSourceToCompany(addSourceDto);
    }

    /* 등록된 생산품 Update ( 가격 수정 ) */
    @Transactional
    public void sourcePriceUpdate(SourcePriceUpdateDto updateDto) {
        int price = productCompanyDao.getSourcePriceById(updateDto.getCompanySourceId());
        if (updateDto.getOldPrice() != price){
            throw new CompanySourceException("가격 수정 실패: 수정도중 값에 변경사항 있음", HttpStatus.CONFLICT);
        }
        if (updateDto.getSourcePrice() == updateDto.getOldPrice()) {
            throw new CompanySourceException("수정된 사항이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        productCompanyDao.sourcePriceUpdate(updateDto);
        productCompanyDao.sourcePriceHistory(updateDto);
    }

    /* 등록된 생산품 Delete */
    @Transactional
    public void deleteSourceFromCompany(String companySourceId) {
        try {
            productCompanyDao.deleteSourceFromCompany(companySourceId);
        } catch (DataIntegrityViolationException e) {
            throw new CompanySourceException("현재 참조된 데이터가 있으면 삭제 안됨 수정 예정 ~", HttpStatus.CONFLICT);
        }
    }

    /* 등록된 상품 생산 -> 창고에 적재 */
    @Transactional
    public void produceSource(Map<String, Object> paramData) {
        String sourceQuantityStr = (String) paramData.get("sourceQuantity");
        String sourcePriceId = (String) paramData.get("sourcePriceId");


        // : 빈값 입력 제한
        if (sourceQuantityStr.isBlank()) {
            throw new ProduceSourceException("입력된 값이 없습니다", HttpStatus.BAD_REQUEST);
        }
        try {
            int sourceQuantity = Integer.parseInt(sourceQuantityStr);
            // : 1 미만 숫자 입력 제한
            if (sourceQuantity < 1) {
                throw new ProduceSourceException("1 이상만 등록할 수 있습니다.", HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            // : 문자나 기타 등등 입력 제한
            throw new ProduceSourceException("정수만 입력할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
        int checkQuantity =  Integer.parseInt(paramData.get("checkQuantity").toString());

        if (checkQuantity != productCompanyDao.getSourceQuantityFromWarehouse(sourcePriceId)){
            throw new ProduceSourceException("재고 등록중 값이 변경되었습니다", HttpStatus.CONFLICT);
        }
        productCompanyDao.produceSource(paramData);
        messagingTemplate.convertAndSend("/topic/charts", new ChartUpdateMessage("차트가 업데이트되었습니다."));
    }


    /* Source Warehouse Table */

    /* 생산 창고 리스트 다 가져옴 */
    public List<ProductCompanyWarehouseDto> getWarehouseSources(String companyId) {
        // { "produceDate", "sourceWarehouseId", "sourceQuantity", "sourceName" }
        return productCompanyDao.getWarehouseSources(companyId);
    }


    /* Order Table */

    /* 주문 리스트 가져옴 */
    public List<ProductCompanyOrderDto> getProductOrderList(ProductCompanySearchOptionDto searchOptionDto) {
        // values: { orderId, sourceName, sourcePrice, quantity, totalPrice, orderDate, orderStatus }
        return productCompanyDao.getProductOrderList(searchOptionDto);
    }

    // 재료 검색 셀렉트 리스트
    public List<String> selectAllCompanySource(String companyId) {
        return productCompanyDao.selectAllCompanySource(companyId);
    }

    /* 주문 처리 프로세스 */
    @Transactional
    public void orderProcess(ProductCompanyOrderProcessDto orderProcessDto) {
        int sourceQuantity = orderProcessDto.getSourceQuantity();
        int orderStatus = productCompanyDao.getOrderStatus(orderProcessDto.getOrderId());
        int sourceStockBalance = productCompanyDao.getSourceQuantityFromWarehouse(orderProcessDto.getSourcePriceId());

        // : 서로 다른 유저가 동시에 주문 처리하는거 막기
        if (orderStatus != 1) {
            throw new ProductCompanyOrderProcessException("이미 처리된 주문 입니다", HttpStatus.CONFLICT);
        }
        // : 창고 적재량은 음수로 갈 수 없음
        if (sourceStockBalance - sourceQuantity < 0) {
            throw new ProductCompanyOrderProcessException("적재량이 모자람~", HttpStatus.BAD_REQUEST);
        }


        // 필요값: { sourceQuantity, sourcePriceId }
        productCompanyDao.outboundSource(orderProcessDto); // SOURCE_WAREHOUSE quantity 갯수 업데이트
        // 필요값: { orderId, orderStatus }
        productCompanyDao.orderProcess(orderProcessDto); // product_order 상태 업데이트 -> 입고대기
        productCompanyDao.orderLog(orderProcessDto); // product_order_log insert

    }

    public List<ProductCompanyChartDto> getChart(String companyId) {
        return productCompanyDao.getChart(companyId);
    }

    public List<ProductCompanyChartDto> orderChart(ProductCompanySearchOptionDto searchOptionDto) {
        return productCompanyDao.orderChart(searchOptionDto);
    }
}
