package org.shark.boot09;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shark.boot09.board.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class ApplicationTests {

  //----- 스프링 부트에 의해 자동 설정되는 Redis Bean 목록 중
  //      StringRedisTemplate 빈이 존재하고 이 빈은 RedisTemplate<String, String>을 상속 받은 형태
  //      public class StringRedisTemplate extends RedisTemplate<String, String> {}
  //      따라서, 스프링 부트가 자동 설정한 StringRedisTemplate과 사용자 정의 RedisTemplate<String, String>은 동일 타입이므로
  //      빈의 이름이 일치해야 주입이 가능합니다.
  
  @Autowired
  private RedisTemplate<String, String> redisTemplateString;  // 같은 타입이면 하나만 있어도 가져올수잇음.
  
  private final String key = "test:redis";
  private final String value = "In Memory NoSQL Database";
  
	@Test
	@DisplayName("Redis에 값 저장하기")
	void saveTest() {
	  ValueOperations<String, String> ops = redisTemplateString.opsForValue();
	  ops.set(key, value);  //----- 저장
	}
	
	@Test
	@DisplayName("Redis에 저장된 값 조회하기")
	void findTest() {
	  ValueOperations<String, String> ops = redisTemplateString.opsForValue();
	  String value = ops.get(key);   //----- 조회
	  Assertions.assertEquals(this.value, value);
	}

	@Test
	@DisplayName("Redis에 저장된 값 삭제하기")
	void deleteTest() {
	  redisTemplateString.delete(key);  //----- 삭제
	  ValueOperations<String, String> ops = redisTemplateString.opsForValue();
	  String value = ops.get(key);  //----- 조회
	  Assertions.assertNull(value);
	  
	}
	
	@Test
	@DisplayName("TTL 테스트하기")
	void ttlTest() {
	  ValueOperations<String, String> ops = redisTemplateString.opsForValue();
	  ops.set(key, value, 30, TimeUnit.SECONDS);  //----- SECONDS로 하면 초가된다는 것을 의미.
	  try {
      Thread.sleep(5000);  // 5초 멈추기
    } catch (Exception e) {
      e.printStackTrace();
    }
	  long remainTtl = redisTemplateString.getExpire(key);  // 남은 TTL 반환
	  log.info("남은 TTL:{}", remainTtl);
	}
	
	@Autowired
	private RedisTemplate<String, BoardDTO> redisTemplateBoard;
	
	@Test
	void boardSaveTest() {
	  BoardDTO value = new BoardDTO();
	  value.setBid(1L);
	  value.setTitle("테스트 제목");
	  value.setContent("테스트 내용");
	  value.setCreatedAt(LocalDateTime.now().toString());
	   ValueOperations<String, BoardDTO> ops = redisTemplateBoard.opsForValue();
	   ops.set("board:1", value, 10, TimeUnit.SECONDS);
	}
	
}
