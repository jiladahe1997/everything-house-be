package hello.model.Login;

import lombok.Data;

@Data public class UserInfo {
    //状态码
    Integer ret;
    //消息
    String msg;
    Integer is_lost;
    //用户昵称
    String nickname;
    //大小为30×30像素的QQ空间头像URL。
    String figureurl;
    //大小为50×50像素的QQ空间头像URL。
    String figureurl_1;
    //大小为100×100像素的QQ空间头像URL。
    String figureurl_2;
    //大小为40×40像素的QQ头像URL。
    String figureurl_qq_1;
    //大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
    String figureurl_qq_2;
    String figureurl_qq;
    String figureurl_type;
    String is_yellow_vip;
    String vip;
    String yellow_vip_level;
    String level;
    String is_yellow_year_vip;
    //性别。 如果获取不到则默认返回"男"
    String gender;
    Integer gender_type;
    String province;
    String city;
    String year;
    String constellation;
}
