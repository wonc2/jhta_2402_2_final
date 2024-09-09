package org.example.jhta_2402_2_final.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.member.MemberDto;

@Mapper
public interface MemberDao {
    MemberDto findById(String userId);
    int signin(MemberDto memberDto);
    int insertUser(MemberDto memberDto);
    int insertRole(String id);
    MemberDto selectUserWithRole(String username);
    boolean checkUserIdExists(String userId);
    String getProductMemberId(String userEmail);
    int insertProductCompanyMember(String userId ,String productCompanyId);

}
