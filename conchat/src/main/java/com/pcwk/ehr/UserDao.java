package com.pcwk.ehr;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
public interface UserDao {
    List<UserVO> getAll(UserVO inVO) throws SQLException;
//  int update(UserVO inVO) throws SQLException;
    /*
     * 단건 등록
     * 
     * @param inVO
     * 
     * @return 1(성공)/0(실패)
     */
    int add(UserVO inVO) throws SQLException;
    /*
     * 단건 조회
     * 
     * @param inVO
     * 
     * @return outVO
     */
    UserVO get(UserVO inVO) throws SQLException, EmptyResultDataAccessException;
    int del(UserVO inVO) throws SQLException;
    int getCount(UserVO inVO) throws SQLException;
    int update(UserVO inVO) throws SQLException;
}