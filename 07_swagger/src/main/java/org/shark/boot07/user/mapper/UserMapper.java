package org.shark.boot07.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shark.boot07.user.dto.UserDTO;

@Mapper
public interface UserMapper {
    long selectUserCount();
	List<UserDTO> selectUserList( @Param("offset")int offset,   @Param("size")int size, @Param("SORT")String sort);
	UserDTO selectUserById(Long uid);
	int insertUser(UserDTO user);
	int updateUser(UserDTO user);
	int deleteUser(Long uid);
	long countAll(); // 추가

}
