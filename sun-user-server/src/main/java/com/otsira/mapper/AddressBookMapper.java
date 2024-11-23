package com.otsira.mapper;

import com.otsira.entity.AddressBook;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 地址簿相关功能的 mapper 层接口
 * @create: 2024/11/20 21:44
 */
public interface AddressBookMapper extends Mapper<AddressBook> {

    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> queryAllAddressBooks(Long userId);

    @Select("select * from address_book where is_default = 1 and user_id = #{userId}")
    AddressBook queryDefaultAddressBook(Long userId);

    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    int updateIsDefaultByUserId(Long userId);

    @Update("update address_book set is_default = 1 where id = #{id}")
    int updateIsDefaultById(Long id);
}
