package com.weixin.sell.dataobject;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 类目
 *
 * ProductCategory类 对应的表是 product_category，spring 会自动识别；但是 如果命名 没有规律 ，就不能识别了，需要 用 @Table 来注明。
 */
//@Table(name = "product_category")
@Entity // 注明 是 数据库表 映射 的 实体类 （现在用 spring data jpa 来 实现 DAO，不是 mybatis）
@DynamicUpdate // 让 可以由 数据库 自动更新 的 字段 依然 自动更新（updateTime） ，不受 更新操作 的 代码 的 影响。
@Data // 自动生成 get、set、toString 方法（并不是 每次 访问 ProductCategory 都生成一次 get、set的等方法，而是 在 编译、打成 jar/war包等 的时候 自动生成一次 就够了。java源文件 没有 get、set的等方法，但是 编译文件、jar包有，而 实际运行的是 编译文件 和 jar包）
//@Getter // 只是 自动生成 get
//@Setter // 只是 自动生成 set
public class ProductCategory {

    /** 类目id. */
    @Id // 注明 主键
    @GeneratedValue // 注明 自增
    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;

    // 为了使 createTime 和 updateTime 自动去 更新值，一般 不在实体类 写 这两个字段。只要不写，更新操作 的 代码 就不会考虑 这两个字段，两个字段 就 完全由 数据库 控制。
    //private Date createTime;

    //private Date updateTime;


    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
