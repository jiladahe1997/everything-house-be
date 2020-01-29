package hello.model.Login;

import hello.model.Common.Status;
import lombok.Getter;
import lombok.Setter;

public class UserInfoVo {
    @Getter Status status=new Status();
    @Setter @Getter String nickname;
    @Setter @Getter String figureurl;
}
