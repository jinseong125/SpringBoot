package org.shark.boot07.user.controller;

import java.util.List;
import java.util.Map;

import org.shark.boot07.common.dto.PageDTO;
import org.shark.boot07.user.dto.UserDTO;
import org.shark.boot07.user.dto.response.ResponseUserDTO;
import org.shark.boot07.user.exception.ErrorResponseDTO;
import org.shark.boot07.user.exception.UserNotFoundException;
import org.shark.boot07.common.util.PageUtil;
import org.shark.boot07.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserApiController {
	private final UserService userService;
	private final PageUtil pageUtil;
	
//    @ExceptionHandler(UserNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponseDTO handleUserNotFound(UserNotFoundException ex) {
//        return ErrorResponseDTO.builder().
//        		status(404).
//        		errorMessage(ex.getMessage())
//        		.build();
//    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException2 (UserNotFoundException ex) {
    	ErrorResponseDTO dto =  ErrorResponseDTO.builder()
       		.errorCode("UE-100")
       		.errorMessage(ex.getMessage())
       		.build();
    	return ResponseEntity.status(404).body(dto);        		
    }
    
    
    @PutMapping("/{uid}")
    public ResponseEntity<ResponseUserDTO> update(@PathVariable(value = "uid") Long uid, @RequestBody UserDTO user) {
    	ResponseUserDTO updatedUser = ResponseUserDTO.builder()
    			.status(200)
    			.message("회원 정보가 수정되었습니다.")
    			.results(Map.of("updatedUser", userService.updateUser(user, uid))).build();
    	return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
    


    
    /*
     * Postman 요청 시 주의사항
     * 
     * 1. x-www-form-urlencoded 형식으로 데이터를 보내는 경우 (폼을 사용하는 경우)
     *    create(UserDTO user)
     * 2. raw 형식 중 JSON 데이터를 보내는 경우
     *    create(@RequestBody UserDTO user)
     * 
     * 
     * */
    
    /*
     * @PostMapping: HTTP POST 요청을 처리하는 매핑 어노테이션입니다.
	   consumes = "application/json":
       이 메서드는 **"application/json" 타입(즉, JSON 형식)**의 요청만 받을 수 있다는 뜻입니다.
     * 클라이언트(예: Postman, 프론트엔드, 앱 등)가 서버로 POST 요청을 보낼 때
       **HTTP 헤더의 Content-Type이 application/json**이어야만
       해당 컨트롤러 메서드가 실행됩니다.
     * 
     * */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ResponseUserDTO> create(@RequestBody UserDTO user) {
        ResponseUserDTO response = ResponseUserDTO.builder()
            .status(201)
            .message("회원 등록 성공")
            .results(Map.of("createdUser", userService.createUser(user)))
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 응답 코드
    }
    
    @DeleteMapping("/{uid}")
    public ResponseEntity<ResponseUserDTO> delete(@PathVariable(value = "uid") Long uid) {
    	userService.deleteUser(uid);
    	ResponseUserDTO dto = ResponseUserDTO.builder()
    										.status(200)
    										.message("회원 정보가 삭제되었습니다.")
    										.build();
    	return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/{uid}")
    public ResponseEntity<ResponseUserDTO> detail(@PathVariable(value = "uid") Long uid) {
    	
    	ResponseUserDTO dto = ResponseUserDTO.builder()
    			                                   .status(200)
    			                                   .message("회원 조회 성공")
    			                                   .results(Map.of("foundUser", userService.getUserById(uid)))
    			                                   .build();
    	return ResponseEntity.ok(dto); 
    }
    
    @GetMapping
    public ResponseEntity<ResponseUserDTO> list(
        @RequestParam(value = "page", defaultValue="1") int page,
        @RequestParam(value="size", defaultValue= "20") int size,
        @RequestParam(value="sort", defaultValue="DESC") String sort) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setSize(size);

        //long totalItem = userService.countUsers(); // int → long
        //pageDTO.setTotalItem(totalItem);
        //pageUtil.calculatePaging(pageDTO);

        List<UserDTO> users = userService.getUserList(pageDTO, sort);

        ResponseUserDTO response = ResponseUserDTO.builder()
            .status(200)
            .message("회원 목록 조회 성공")
            .results(Map.of("users", users, "paging", pageDTO))
            .build();

        return ResponseEntity.ok(response);
    }
}
