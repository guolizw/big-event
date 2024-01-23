package com.zw.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    @NotNull(groups = update.class)
    private Integer id;//主键ID
    @NotEmpty//(groups = {add.class,update.class})
    private String categoryName;//分类名称
    @NotEmpty//(groups = {add.class,update.class})
    private String categoryAlias;//分类别名
    @JsonIgnore
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间


    //主键id并不是任意时刻都要去检验不为空 所以需要分组检验
    //如果没有设置分组 那么为默认 分组可以继承
//    public interface add {
//
//    }
//    public interface update{
//
//    }
    public interface add extends Default {

    }
    public interface update extends Default{

    }

}
