package org.shark.boot06;

import org.junit.jupiter.api.Test;
import org.shark.boot06.user.dto.UserDTO;
import org.shark.boot06.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private UserService userService;
	
	
	@Test
	void contextLoads() {
		
		UserDTO user = new UserDTO(null, "유저네임", "비밀번호", "닉네임", null);
		 userService.createUser(user);
		//UserDTO userResult = userService.getUserById(userSultUser.getUid());
		  // assertEquals(기대값, 실제값)
        //assertEquals(user.getUsername(), userResult.getUsername());
        //assertEquals(user.getNickname(), userResult.getNickname());
        // 비밀번호나 createdAt도 필요하면 추가 가능!
	}

}
