package com.shuangshuan.scaffold.relationaldataaccess.mysql.controller;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.common.PageResult;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.common.ResponseResult;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.dto.AddressBookDto;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.dto.UserAddressBookDto;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.entity.AddressBook;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AddressBookController", description = "AddressBookController")
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    Logger logger = LoggerFactory.getLogger(AddressBookController.class);



    @Autowired
    private AddressBookService addressBookService;

    @Operation(summary = "save addressBookDto", description = "save addressBookDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "save addressBookDto successfully"),
            @ApiResponse(responseCode = "500", description = "save addressBookDto failed")
    })
    @PostMapping("/save")
    public ResponseResult<AddressBook> save(@Parameter(description = "addressBookDto")
                                        @RequestBody @Valid AddressBookDto addressBookDto, BindingResult bindingResult) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookDto, addressBook);
        return ResponseResult.success(addressBookService.save(addressBook));
    }

    @Operation(summary = "Get AddressBook by ID", description = "Get AddressBook details by providing  ID")


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AddressBook details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "AddressBook not found")
    })
    @GetMapping("/getById/{id}")
    public ResponseResult<AddressBook> get(@Parameter(name = "id", description = "ID", required = true) @PathVariable Long id) {
        logger.info("Hello world.收拾收拾"+id);
        AddressBook addressBook = addressBookService.getById(id);
        return ResponseResult.success(addressBook);
    }


    /**
     * 查询指定用户的全部地址
     */
    @Operation(summary = "Get AddressBooks by ID", description = "Get AddressBooks  by providing  ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AddressBooks details retrieved successfully")
    })
    @GetMapping("/list/{userId}")
    public ResponseResult<List<AddressBook>> listByUserId(
            @Parameter(name = "userId", description = "userId", required = true)
            @PathVariable Long userId) {

        // TODO 这里是直接写得SQL，难道它本身没有这样的方法吗？
        //SQL:select * from address_book where user_id = ? order by update_time desc
        return ResponseResult.success(addressBookService.listByUserId(userId));
    }

    /**
     * 根据ID删除地址信息
     */
    @Operation(summary = "Delete AddressBook", description = "Delete AddressBook by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "AddressBook deleted successfully"),
            @ApiResponse(responseCode = "404", description = "AddressBook not found")
    })
    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Long> deleteById(
            @Parameter(name = "id", description = "ID", required = true)
            @PathVariable Long id) {
        addressBookService.deleteById(id);
        return ResponseResult.success(id,"delete addressBook successfully");
    }

    /**
     * 根据id修改地址信息
     */
    @Operation(summary = "Update AddressBook", description = "Update existing AddressBook details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AddressBook details updated successfully"),
            @ApiResponse(responseCode = "404", description = "AddressBook not found")
    })
    @PutMapping("/update")
    public ResponseResult<AddressBook> update(@Parameter(description = "addressBookDto") @RequestBody AddressBook addressBook) {
        // 第一次调试的时候   2024-03-24T19:07:10.108+08:00  WARN 18112 --- [Scaffold] [nio-8080-exec-7] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotReadableException:
        // JSON parse error: Cannot construct instance of `com.integration.scaffold.relationaldataaccess.mysql.entity.AddressBook` (although at least one Creator exists): no String-argument constructor/factory
        // method to deserialize from String value ('id')]  -------postman发请求的JSON格式写错了
        return ResponseResult.success(addressBookService.save(addressBook));
    }


    /**
     * 根据userid此用户的所有地址信息
     */
    @Operation(summary = "Get UserAddressBookDto by ID", description = "Get UserAddressBookDto details by providing  ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UserAddressBookDto details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "UserAddressBookDto not found")
    })
    @GetMapping("/userInfo/{userId}")
    public ResponseResult<UserAddressBookDto> getAllUserInfo(
            @Parameter(name = "userId", description = "userId", required = true)
            @PathVariable Long userId) {
        return ResponseResult.success(addressBookService.getAllUserInfo(userId));
    }

    /**
     * 查询所有地址信息 需要分页
     */
    @Operation(summary = "Get AddressBookDtos by pageNum and pageSize", description = "Get ddressBookDtos details by pageNum and pageSize")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AddressBookDto details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "AddressBookDto not found")
    })
    @GetMapping("/addressBookInfoByPage")
    public ResponseResult<Page<AddressBook>> getUserInfoByPage(
            @Parameter(name = "page", description = "page", required = true)
            @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(name = "pagesize", description = "pagesize", required = true)
            @RequestParam(value = "pagesize", defaultValue = "10") int pagesize) {
        Page<AddressBook> addressBookPage = addressBookService.getUserInfoByPage( page-1, pagesize);
        return ResponseResult.success(addressBookPage, "get address book details successfully");
    }

    /**
     * 查询所有的收货人
     */
    @Operation(summary = "Get getAllConsignee", description = "Get getAllConsignee")
    @GetMapping("/consignee")
    public ResponseResult<List<String>> getAllConsignee() {
        return ResponseResult.success(addressBookService.getAllConsignee());
    }


}
