package com.shuangshuan.scaffold.relationaldataaccess.mysql.mapper;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.entity.AddressBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AddressBookRepository extends JpaRepository<AddressBook, Long>, JpaSpecificationExecutor<AddressBook> {

//    DISTINCT can be tricky and not always producing the results you expect.这里使用 @Query
    @Query(value = "select distinct a.consignee from AddressBook a ")
    List<String> getAllConsignee();

    List<AddressBook> findByUserId(Long userId);

    Page<AddressBook> findByUserId(Long userId, PageRequest pageRequest);


}
