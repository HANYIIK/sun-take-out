package com.otsira.controller;

import com.otsira.dto.AddressBookDTO;
import com.otsira.entity.AddressBook;
import com.otsira.result.Result;
import com.otsira.service.AddressBookService;
import com.otsira.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 地址簿相关功能的 controller 层
 * @create: 2024/11/20 21:41
 */
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "地址簿相关功能")
public class AddressBookController {
    private AddressBookService addressBookService;

    @Autowired
    public void setAddressBookService(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @ApiOperation("查询所有地址簿")
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        log.info("用户 id-{} 请求查询所有地址簿", UserContext.getUserId());
        return Result.success(addressBookService.queryAllAddressBooks());
    }

    @ApiOperation("根据id查询地址簿")
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBookById(@PathVariable Long id) {
        log.info("用户 id-{} 请求查询地址簿 id-{}", UserContext.getUserId(), id);
        return Result.success(addressBookService.queryAddressBookById(id));
    }

    @ApiOperation("查询默认地址簿")
    @GetMapping("/default")
    public Result<AddressBook> defaultAddressBook() {
        log.info("用户 id-{} 请求查询默认地址簿", UserContext.getUserId());
        return Result.success(addressBookService.queryDefaultAddressBook());
    }

    @ApiOperation("新增地址簿")
    @PostMapping
    public Result<Object> addAddressBook(@RequestBody AddressBookDTO addressBookDTO) {
        log.info("用户 id-{} 请求新增地址簿: {}", UserContext.getUserId(), addressBookDTO);
        AddressBook addressBook = AddressBook.builder()
                .userId(UserContext.getUserId())
                .consignee(addressBookDTO.getConsignee())
                .phone(addressBookDTO.getPhone())
                .sex(addressBookDTO.getSex())
                .provinceCode(addressBookDTO.getProvinceCode())
                .provinceName(addressBookDTO.getProvinceName())
                .cityCode(addressBookDTO.getCityCode())
                .cityName(addressBookDTO.getCityName())
                .districtCode(addressBookDTO.getDistrictCode())
                .districtName(addressBookDTO.getDistrictName())
                .detail(addressBookDTO.getDetail())
                .label(addressBookDTO.getLabel())
                .isDefault(0)
                .build();
        int insert = addressBookService.insertAddressBook(addressBook);
        if (insert <= 0) {
            return Result.error("新增地址簿失败");
        }
        return Result.success();
    }

    @ApiOperation("修改地址簿")
    @PutMapping
    public Result<Object> updateAddressBook(@RequestBody AddressBookDTO addressBookDTO) {
        log.info("用户 id-{} 请求修改地址簿: {}", UserContext.getUserId(), addressBookDTO);
        AddressBook addressBook = AddressBook.builder()
                .id(addressBookDTO.getId())
                .userId(UserContext.getUserId())
                .consignee(addressBookDTO.getConsignee())
                .phone(addressBookDTO.getPhone())
                .sex(addressBookDTO.getSex())
                .provinceCode(addressBookDTO.getProvinceCode())
                .provinceName(addressBookDTO.getProvinceName())
                .cityCode(addressBookDTO.getCityCode())
                .cityName(addressBookDTO.getCityName())
                .districtCode(addressBookDTO.getDistrictCode())
                .districtName(addressBookDTO.getDistrictName())
                .detail(addressBookDTO.getDetail())
                .label(addressBookDTO.getLabel())
                .build();
        int update = addressBookService.updateAddressBook(addressBook);
        if (update <= 0) {
            return Result.error("修改地址簿失败");
        }
        return Result.success();
    }

    @ApiOperation("设置默认地址簿")
    @PutMapping("/default")
    public Result<Object> setDefaultAddressBook(@RequestBody AddressBook addressBook) {
        log.info("用户 id-{} 请求设置地址簿 id-{} 为默认地址", UserContext.getUserId(), addressBook.getId());
        int update = addressBookService.updateDefaultAddressBook(addressBook.getId());
        if (update <= 0) {
            return Result.error("设置默认地址簿失败");
        }
        return Result.success();
    }

    @ApiOperation("删除地址簿")
    @DeleteMapping
    public Result<Object> deleteAddressBook(@RequestParam Long id) {
        log.info("用户 id-{} 请求删除地址簿 id-{}", UserContext.getUserId(), id);
        int delete = addressBookService.deleteAddressBookById(id);
        if (delete <= 0) {
            return Result.error("删除地址簿失败");
        }
        return Result.success();
    }
}
