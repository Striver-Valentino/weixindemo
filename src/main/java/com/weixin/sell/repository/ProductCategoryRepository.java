package com.weixin.sell.repository;

import com.weixin.sell.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ProductCategory 的 Repository（资源库），其实 就是 DAO ，换个名字而已。
 * spring data jpa 里 DAO 是叫 Repository 的。
 *
 *
 *
 * Repository（资源库）：通过用来访问领域对象的一个类似集合的接口，在领域与数据映射层之间进行协调。这个叫法就类似于我们通常所说的DAO，在这里，我们就按照这一习惯把数据访问层叫Repository
 *    Spring Data给我们提供几个Repository，基础的Repository提供了最基本的数据访问功能，其几个子接口则扩展了一些功能。它们的继承关系如下：
 *    Repository： 仅仅是一个标识，表明任何继承它的均为仓库接口类，方便Spring自动扫描识别
 *    CrudRepository： 继承Repository，实现了一组CRUD相关的方法
 *    PagingAndSortingRepository： 继承CrudRepository，实现了一组分页排序相关的方法
 *    JpaRepository： 继承PagingAndSortingRepository，实现一组JPA规范相关的方法
 *    JpaSpecificationExecutor： 比较特殊，不属于Repository体系，实现一组JPA Criteria查询相关的方法
 *    我们自己定义的XxxxRepository需要继承JpaRepository，这样我们的XxxxRepository接口就具备了通用的数据访问控制层的能力。
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> { // 泛型 <对应的实体类,实体类主键类型>

    /**
     * 相当于 select * from product_category where category_type in (?)
     * @param categoryTypeList
     * @return
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList); // 根据 类目编号 list 查找 类目 list





}
