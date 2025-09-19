package org.shark.boot06.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.shark.boot06.user.dto.UserDTO;

@Mapper
public interface UserMapper {
  int selectUserCount();
  List<UserDTO> selectUserList(int offset, int size, String sort);
  UserDTO selectUserById(Long uid);
  int insertUser(UserDTO user);
  int updateUser(UserDTO user);
  int deleteUser(Long uid);
}
