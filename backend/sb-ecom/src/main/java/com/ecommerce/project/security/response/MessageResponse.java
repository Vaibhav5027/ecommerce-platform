package com.ecommerce.project.security.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponse {
  String msg;

  public MessageResponse(String msg) {
    this.msg = msg;
  }
}
