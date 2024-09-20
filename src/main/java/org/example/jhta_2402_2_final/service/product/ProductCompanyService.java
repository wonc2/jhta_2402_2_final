package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.example.jhta_2402_2_final.exception.types.productCompany.CompanySourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProduceSourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProductCompanyAccessException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProductCompanyOrderProcessException;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.example.jhta_2402_2_final.model.dto.productCompany.ProductCompanyChartDto;
import org.example.jhta_2402_2_final.model.dto.productCompany.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
    private boolean isSchedulerActive = false;


    // 유저 인증
    public String getCompanyIdByUserId(String userId) {
        return productCompanyDao.getCompanyIdByUserId(userId)
                .orElseThrow(() -> new ProductCompanyAccessException("회사 정보를 찾을 수 없습니다.", HttpStatus.FORBIDDEN));
    }
    public String getCompanyNameByUserId(String userId) {
        return productCompanyDao.getCompanyNameByUserId(userId)
                .orElseThrow(() -> new ProductCompanyAccessException("회사 정보를 찾을 수 없습니다.", HttpStatus.FORBIDDEN));
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
            throw new CompanySourceException("실패: 이미 등록된 제품 입니다.", HttpStatus.CONFLICT);
        }
        // : 제품 이름으로 공백이나 null 값 입력 불가능
        if (sourceId == null && (sourceName == null || sourceName.isEmpty() || sourceName.isBlank())) {
            throw new CompanySourceException("실패: 빈 값 입력 안됩니다.", HttpStatus.BAD_REQUEST);
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

        messagingTemplate.convertAndSend("/topic/product/company/" + addSourceDto.getCompanyId(), "updated");
    }

    /* 등록된 생산품 Update ( 가격 수정 ) */
    @Transactional
    public void sourcePriceUpdate(String companyId, SourcePriceUpdateDto updateDto) {
        int price = productCompanyDao.getSourcePriceById(updateDto.getCompanySourceId());
        if (updateDto.getOldPrice() != price){
            throw new CompanySourceException("실패: 가격 수정중 다른 사용자에 의해 가격이 수정 되었음", HttpStatus.CONFLICT);
        }
        if (updateDto.getSourcePrice() == updateDto.getOldPrice()) {
            throw new CompanySourceException("수정된 사항이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        productCompanyDao.sourcePriceUpdate(updateDto);
        productCompanyDao.sourcePriceHistory(updateDto);

        messagingTemplate.convertAndSend("/topic/product/company/" + companyId, "updated");
    }

    /* 등록된 생산품 Delete */
    @Transactional
    public void deleteSourceFromCompany(String companySourceId) {
        try {
            productCompanyDao.deleteSourceFromCompany(companySourceId);
        } catch (DataIntegrityViolationException e) {
            throw new CompanySourceException("이미 삭제된 품목", HttpStatus.CONFLICT);
        }
    }

    /* 등록된 상품 생산 -> 창고에 적재 */
    @Transactional
    public void produceSource(String companyId, CompanySourceStackDto sourceStackDto) {
        String sourcePriceId = sourceStackDto.getSourcePriceId();
        int checkQuantity =  sourceStackDto.getCheckQuantity();
        int warehouseSourceQuantity = productCompanyDao.getSourceQuantityFromWarehouse(sourcePriceId);

        if (checkQuantity != warehouseSourceQuantity){
            throw new ProduceSourceException("실패: 재고 등록중 다른 사용자에 의해 먼저 입고된 기록이 존재합니다. 확인후 재시도", HttpStatus.CONFLICT);
        }

        productCompanyDao.produceSource(sourceStackDto);

        messagingTemplate.convertAndSend("/topic/product/company/" + companyId, "updated");
    }


    /* Source Warehouse Table */

    /* 생산 창고 리스트 다 가져옴 */
    public List<ProductCompanyWarehouseDto> getWarehouseSources(ProductCompanySearchOptionDto paramData) {
        return productCompanyDao.getWarehouseSources(paramData);
    }
    @Transactional
    public void deleteWarehouseProduceLog(String companyId,String sourceWarehouseId) {
        Map<String, Object> warehouseMap = productCompanyDao.getSourcePriceIdBySourceWarehouseId(sourceWarehouseId);
        String sourcePriceId = warehouseMap.get("sourcePriceId").toString();
        int checkQuantity = (int) warehouseMap.get("quantity");
        int sourceStockBalance = productCompanyDao.getSourceQuantityFromWarehouse(sourcePriceId);
        if (checkQuantity > sourceStockBalance){
            throw new RuntimeException();
        }
        productCompanyDao.deleteWarehouseProduceLog(sourceWarehouseId);
        messagingTemplate.convertAndSend("/topic/product/company/" + companyId, "updated");
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

    /* 주문 처리 프로세스 */ // lock, unlock, process
    @Transactional
    public void orderProcessLock(ProductCompanyOrderProcessDto orderProcessDto){
        try {
            productCompanyDao.orderStatusTempInsert(orderProcessDto);
            isSchedulerActive = true; // 스케줄러 활성화
        } catch (DuplicateKeyException e) {
            throw new ProductCompanyOrderProcessException("다른 사용자가 해당 주문을 처리 중입니다.", HttpStatus.CONFLICT);
        }
        messagingTemplate.convertAndSend("/topic/product/company/" + orderProcessDto.getCompanyId(), "updated");
    }
    public void unlockOrder(String companyId, String orderId){
        productCompanyDao.orderStatusTempDel(orderId);
        messagingTemplate.convertAndSend("/topic/product/company/" + companyId, "updated");
    }
    public void updateOrderTime(String orderId) {
        productCompanyDao.updateOrderTime(orderId);
    }
    // 매 10초 마다 오래된 임시 상태 삭제 ( 60000 = 1 min ) ( 업데이트된지 15초 이상인거 삭제: INTERVAL 15 SECOND )
    // order_temp 비어있으면 비활성화 ( 시간마다 if 문만 체크함 )
    @Scheduled(fixedRate = 10000)
    public void cleanupExpiredOrderLocks() {
        if (isSchedulerActive) {
            if (productCompanyDao.checkOrderLocks()) { // order_temp 테이블 뭐라도 있으면 true
                checkAndDeleteExpiredOrderLocks(); // 만료된거 있으면 true -> delete
            } else {
                // order_temp 테이블 비어있으면 이 스케줄러 디비 접근 비활성화
                isSchedulerActive = false;
            }
        }
    }
    @Scheduled(fixedRate = 7200000) // 2시간 마다 체크
    public void cleanupExpiredOrderLocksEveryTwo() {
        checkAndDeleteExpiredOrderLocks();
    }
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
            throw new ProductCompanyOrderProcessException("실패: 재고가 부족하여 주문을 처리 할 수 없음", HttpStatus.BAD_REQUEST);
        }

        productCompanyDao.outboundSource(orderProcessDto); // SOURCE_WAREHOUSE quantity 갯수 업데이트
        productCompanyDao.orderProcess(orderProcessDto); // product_order 상태 업데이트 -> 입고대기
        productCompanyDao.orderLog(orderProcessDto); // product_order_log insert
        productCompanyDao.orderStatusTempDel(orderProcessDto.getOrderId()); // order_status_temp delete
        messagingTemplate.convertAndSend("/topic/product/company/" + orderProcessDto.getCompanyId(), "updated");
    }

    /* 차트 */
    public ProductCompanyChartResponseDto getChart(ProductCompanySearchOptionDto paramData) {
        List<ProductCompanyChartDto> warehouseChart = productCompanyDao.getChart(paramData.getCompanyId());
        List<ProductCompanyChartDto> orderChart = productCompanyDao.orderChart(paramData);
        ProductCompanyChartResponseDto responseChartDto = ProductCompanyChartResponseDto.builder()
                .warehouseChart(warehouseChart).salesChart(orderChart)
                .build();
        return responseChartDto;
    }

    /* 메서드 */
    // 만료된 order_temp 있는지 체크하고 있으면 삭제
    private void checkAndDeleteExpiredOrderLocks() {
        if (productCompanyDao.checkExpiredOrderLocks()) {
            productCompanyDao.deleteExpiredOrderLocks();
            messagingTemplate.convertAndSend("/topic/product/company", "updated");
        }
    }
}
