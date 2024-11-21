package com.otsira.service;

import com.otsira.entity.AddressBook;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 地址簿相关功能的 service 层接口
 * @create: 2024/11/20 21:42
 */
public interface AddressBookService {
    int insertAddressBook(AddressBook addressBook);
    List<AddressBook> queryAllAddressBooks();
    AddressBook queryDefaultAddressBook();
    int updateDefaultAddressBook(Long id);
    AddressBook queryAddressBookById(Long id);
    int updateAddressBook(AddressBook addressBook);
    int deleteAddressBookById(Long id);
}
