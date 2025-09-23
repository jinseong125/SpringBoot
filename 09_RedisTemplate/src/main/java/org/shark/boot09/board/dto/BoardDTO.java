package org.shark.boot09.board.dto;

import java.time.LocalDateTime;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class BoardDTO {

  private Long bid;
  private String title;
  private String content;
  private String createdAt;
 
}
