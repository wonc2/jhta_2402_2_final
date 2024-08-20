SELECT
    u.USER_PK AS userPk,
    u.USER_NAME AS userName,
    u.USER_PASSWORD AS userPassword,
    u.USER_ID AS userId,
    u.USER_EMAIL AS userEmail,
    rn.ROLE_NAME AS roleName
FROM USERS u
         LEFT JOIN ROLE r ON u.USER_PK = r.USER_PK
         LEFT JOIN ROLE_NAME rn ON r.ROLE_NAME_PK = rn.ROLE_NAME_PK;