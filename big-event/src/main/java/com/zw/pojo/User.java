package com.zw.pojo;


//注意包的区别
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class User {
    @NotNull //为属性添加校验 该校验需要在具体的对象上注解validation才会生效
    private Integer id;//主键ID
    private String username;//用户名
    @JsonIgnore  //让springmvc在将当前对象转换成json字符串的时候将其忽略不转换
    private String password;//密码

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;//昵称

    @NotEmpty
    @Email
    private String email;//邮箱
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
