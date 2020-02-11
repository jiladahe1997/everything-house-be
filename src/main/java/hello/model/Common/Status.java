package hello.model.Common;

import lombok.Getter;
import lombok.Setter;

public class Status {
    private Integer code;
    private String msg;

   public Integer getCode() {
      return code;
   }

   public String getMsg() {
      return msg;
   }

   public void setCode(Integer code) {
      this.code = code;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }
}
