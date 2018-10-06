package com.weixin.sell.repository;

import com.weixin.sell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest(){
        ProductCategory productCategory = repository.findOne(1);
        System.out.println(productCategory.toString());
    }

    @Test
    public void saveTest(){
        ProductCategory productCategory = new ProductCategory("女生最爱",3);
        //productCategory.setCategoryName("女生最爱");
        //productCategory.setCategoryType(3);

        ProductCategory result = repository.save(productCategory); // 返回 类型 也是 productCategory

        //断言
        Assert.assertNotNull(result); // 断言 result 不是 null
        //Assert.assertNotEquals(null, result); // 和上一行 等价
    }

    @Test
    @Transactional // 事务注解，这跟 service 层 的事务 不同。service 层 的事务 是 在代码 发生 异常时，回滚，之前 在 数据库 留下的数据 都清空。
                   // 测试 里 的 事务，是为了 不在数据库 里 留下 测试 的痕迹，即 测试完，立即 删除 所有 测试数据（立即 回滚）。
    public void saveTest02(){
        ProductCategory productCategory = new ProductCategory("男生最爱",4);

        ProductCategory result = repository.save(productCategory);

        Assert.assertNotNull(result);
    }

    @Test
    public void updateTest(){ // spring data jpa 的更新，其实 还是调用 save 方法，但是 必须指定 主键，然后 用 新的 对象信息 覆盖 以前的 表 内容。
        ProductCategory productCategory = new ProductCategory("男生最爱",3);
        productCategory.setCategoryId(2);
        productCategory.setCategoryName("男生最爱");
        productCategory.setCategoryType(3);

        repository.save(productCategory);
    }

    @Test
    public void updateTest02(){
        // 先 查询出来
        ProductCategory productCategory = repository.findOne(2);
        // 再去 修改
        productCategory.setCategoryType(3);
        /**
         * 这里 虽然 修改了 类目类型，但是 没有修改 updateTime字段。
         * 因为 repository.findOne(2) 查出来的 对象 包含 updateTime字段 ，而 代码 又没有去 修改 updateTime字段，所以 还是用 原来
         * 的 updateTime 值 去覆盖 数据库表 的内容。
         *
         * 但是 数据库 设置 updateTime 字段 就是 想让 数据库自己去更新，而不是 用代码去更新。
         * 解决办法 是 在 ProductCategory 类 之前 加 @DynamicUpdate
         */

        //productCategory.setCategoryName("女生最爱");

        repository.save(productCategory); // save 方法 触发更新 的 机制 是，productCategory 对象 和 原来 主键2 对应 的 对象 有字段值 不同，
                                          // 如果 原来 categoryType 就是10，现在 productCategory.setCategoryType(10) ，并且没有 其它的改变，那么 是不会 更新的，
                                          // 即使 是 updateTime 字段（加了 @DynamicUpdate 的情况下） 也不会自动更新。
    }


    @Test
    public void findByCategoryTypeInTest(){

        List<Integer> list = Arrays.asList(2, 3, 4);

        List<ProductCategory> result = repository.findByCategoryTypeIn(list);

        Assert.assertNotEquals(0,result.size());
    }






}