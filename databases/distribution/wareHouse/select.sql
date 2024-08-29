-- select {PK, wareHouseName, SourceName, ProductCompanyName, Quantity}


SELECT
    lws.LOGISTICS_WAREHOUSE_SOURCE_PK AS 'PK',
        lw.WAREHOUSE_NAME AS 'Warehouse Name',
        s.NAME AS 'Source Name',
        pc.NAME AS 'Product Company Name',
        lws.QUANTITY AS 'Quantity'
FROM
    LOGISTICS_WAREHOUSE lw
        JOIN
    LOGISTICS_WAREHOUSE_SOURCE lws ON lw.LOGISTICS_WAREHOUSE_PK = lws.LOGISTICS_WAREHOUSE_FK
        JOIN
    SOURCE s ON lws.SOURCE_FK = s.SOURCE_ID
        JOIN
    SOURCE_PRICE sp ON s.SOURCE_ID = sp.SOURCE_ID
        JOIN
    PRODUCT_COMPANY pc ON sp.PRODUCT_COMPANY_ID = pc.PRODUCT_COMPANY_ID
ORDER BY
    lw.WAREHOUSE_NAME, s.NAME;
