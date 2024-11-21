package com.otsira.service.impl;

import com.otsira.entity.AddressBook;
import com.otsira.mapper.AddressBookMapper;
import com.otsira.service.AddressBookService;
import com.otsira.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 地址簿相关功能的 service 层接口的默认实现类
 * @create: 2024/11/20 21:43
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class DefaultAddressBookService implements AddressBookService {
    private AddressBookMapper addressBookMapper;

    @Autowired
    public void setAddressBookMapper(AddressBookMapper addressBookMapper) {
        this.addressBookMapper = addressBookMapper;
    }

    /**
     * 新增地址簿
     * @param addressBook 地址簿实体类(包含 userId)
     * @return 新增结果
     */
    @Override
    public int insertAddressBook(AddressBook addressBook) {
        return addressBookMapper.insert(addressBook);
    }

    /**
     * 查询某用户的所有地址簿
     * @return 地址簿列表
     */
    @Override
    public List<AddressBook> queryAllAddressBooks() {
        return addressBookMapper.queryAllAddressBooks(UserContext.getUserId());
    }

    /**
     * 查询某用户的默认地址簿
     * @return 默认地址簿
     */
    @Override
    public AddressBook queryDefaultAddressBook() {
        return addressBookMapper.queryDefaultAddressBook(UserContext.getUserId());
    }

    /**
     * 更新某用户的默认地址簿
     * @param id 地址簿 id
     * @return 更新结果
     */
    @Override
    public int updateDefaultAddressBook(Long id) {
        int update;
        // 1.把所有地址都设置为非默认地址
        update = addressBookMapper.updateIsDefaultByUserId(UserContext.getUserId());

        // 2.把指定 id 的地址设置为默认地址
        update += addressBookMapper.updateIsDefaultById(id);
        return update;
    }

    /**
     * 根据 id 查询地址簿
     * @param id 地址簿 id
     * @return 地址簿
     */
    @Override
    public AddressBook queryAddressBookById(Long id) {
        return addressBookMapper.queryAddressBookById(id, UserContext.getUserId());
    }

    /**
     * 更新地址簿
     * @param addressBook 地址簿实体类
     * @return 更新结果
     */
    @Override
    public int updateAddressBook(AddressBook addressBook) {
        return addressBookMapper.updateByPrimaryKeySelective(addressBook);
    }

    /**
     * 删除地址簿
     * @param id 地址簿 id
     * @return 删除结果
     */
    @Override
    public int deleteAddressBookById(Long id) {
        return addressBookMapper.deleteByPrimaryKey(id);
    }
}
