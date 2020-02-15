package hello.model.Common;

import com.fasterxml.jackson.annotation.JsonView;
import hello.DTO.Result;
import hello.model.Video;
import lombok.Getter;
import lombok.Setter;

public class Status {
   @JsonView(Result.resultView.class)
    private Integer code;
   @JsonView(Result.resultView.class)
    private String msg;;

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
