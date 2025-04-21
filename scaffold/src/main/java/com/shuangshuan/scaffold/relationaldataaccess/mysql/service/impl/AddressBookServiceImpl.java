package com.shuangshuan.scaffold.relationaldataaccess.mysql.service.impl;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.dto.UserAddressBookDto;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.entity.AddressBook;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.entity.User;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.mapper.AddressBookRepository;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.mapper.AddressBookSpecs;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.mapper.UserRepository;
import com.shuangshuan.scaffold.relationaldataaccess.mysql.service.AddressBookService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AddressBookServiceImpl implements AddressBookService {

    Logger logger = LoggerFactory.getLogger(AddressBookServiceImpl.class);
    // todo CrudRepository的扩展接口是PagingAndSortingRepository，它提供了额外的方法来使用分页和排序的抽象来检索实体。
    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private UserRepository userRepository;

    public static final Integer EACH_INSERT = 2;

    public static final Integer TOTAL_INSERT_DATA = 10;

    @Override
    public AddressBook save(AddressBook addressBook) {

        if (Optional.ofNullable(addressBook.getId()).isPresent()) {
            addressBook.setUpdateTime(LocalDateTime.now());
            addressBook.setUpdateUser(11111111l);
            return addressBookRepository.save(addressBook);
        }
        addressBook.setCreateTime(LocalDateTime.now());
        addressBook.setUpdateTime(LocalDateTime.now());
        // todo 这里的createUser都写成了1111111这个工号，之后将登录功能集成进来之后，可以获得当前用户，写入当前用户的工号
        addressBook.setCreateUser(1111111l);
        addressBook.setUpdateUser(1111111l);
        return addressBookRepository.save(addressBook);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressBookRepository.findById(id).orElse(null);

        // todo 这里直接返回null是否不好？有没有可能使用Optional进行优化 一定要对返回值为null做一个专业的整改

    }

    @Override
    public List<AddressBook> listByUserId(Long userId) {
//        AddressBook addressBook = new AddressBook();
//        addressBook.setUserId(userId);
//        Example<AddressBook> example = Example.of(addressBook);
//        return addressBookRepository.findAll(example);
        return addressBookRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        addressBookRepository.deleteById(id);
    }


    @Override
    public UserAddressBookDto getAllUserInfo(Long id) {
        UserAddressBookDto userAddressBookDto = new UserAddressBookDto();
        Optional<User> optionalUser = userRepository.findById(id);
        // Specification实现动态查询，Root即root 获取实体类具体属性, CriteriaBuilder即cb 拼接查询条件的，拼接好查询条件之后，通过 CriteriaQuery即query 实现查询
        if (optionalUser.isPresent()) {
//            Specification<AddressBook> queryCondition = new Specification<AddressBook>() {
//
//                @Override
//                public Predicate toPredicate(Root<AddressBook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                    List<Predicate> predicateList = new ArrayList<>();
//                    if (id != null) {
//                        predicateList.add(criteriaBuilder.equal(root.get("userId"), id));
//                    }
//                    // 这里只是尝试下复杂查询是否真实可用，直接用了数字
//                    predicateList.add(criteriaBuilder.like(root.get("consignee"), "%云志%"));
//                    predicateList.add(criteriaBuilder.between(root.get("id"), 2, 5));
//                    return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
//
//                }
//
//            };

            Sort sort = Sort.by(Sort.Direction.DESC, "updateTime").and(Sort.by(Sort.Direction.ASC, "id"));
            List<AddressBook> addressBookList = addressBookRepository.findAll(AddressBookSpecs.combinedSpecification(id,"云志"), sort);

            userAddressBookDto.setAddressBooklist(addressBookList);
            return userAddressBookDto;
        }

        return null;
    }

    @Override
    public Page<AddressBook> getUserInfoByPage(int pageNum, int pageSize) {
//        AddressBook addressBook = new AddressBook();
//        addressBook.setUserId(userId);
//        Example<AddressBook> example = Example.of(addressBook);
//        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime").and(Sort.by(Sort.Direction.ASC, "id"));
//        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
//        Page<AddressBook> addressBookPage = addressBookRepository.findAll(example, pageable);
////         JpaRepositoryImplementation<T, ID> 继承 JpaSpecificationExecutor<T>
////         JpaSpecificationExecutor<T> 里面有 Page<T> findAll(Specification<T> spec, Pageable pageable);方法 可以写复杂的查询条件
////         JpaRepository<T, ID> extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T>
////         QueryByExampleExecutor<T> 里面有<S extends T> Page<S> findAll(Example<S> example, Pageable pageable); 可以写简单点的查询方法
//        return addressBookPage;
        return addressBookRepository.findAll(PageRequest.of(pageNum, pageSize));
    }

    @Override
    public List<String> getAllConsignee() {
        return addressBookRepository.getAllConsignee();
    }

    @Override
    public void testSyncInsertDatas() {

        // 插入2万条数据，for循环插入，一次插入1千条
        for (int i = 0; i < TOTAL_INSERT_DATA / EACH_INSERT; i++) {
            List<AddressBook> addressBookList = new ArrayList<>();
            for (int j = 0; j < EACH_INSERT; j++) {

                AddressBook addressBook = randomCreateAddressBook();
                addressBookList.add(addressBook);

            }

            List<AddressBook> addressBooks = addressBookRepository.saveAll(addressBookList);
            if (addressBooks.size() == EACH_INSERT) {
                logger.info("第" + i + "个" + EACH_INSERT + "地址插入成功");
            }

        }
        logger.info(TOTAL_INSERT_DATA + "个地址插入成功");

    }

    public AddressBook randomCreateAddressBook() {
        AddressBook addressBook = new AddressBook();
        // 生成一个20位随机数

        addressBook.setUserId(new Random().nextLong(900000000) + 100000000);

        addressBook.setConsignee(new String[]{"云志", "云超", "大栓子", "大霜子", "霜霜", "栓栓"}[new Random().nextInt(6)]);

        addressBook.setSex(new String[]{"0", "1"}[new Random().nextBoolean() ? 1 : 0]);
        addressBook.setPhone("15" + String.valueOf(new Random().nextInt(900000000) + 100000000));

        addressBook.setProvinceCode(String.valueOf(new Random().nextInt(9000)));
        addressBook.setProvinceName(new String[]{"河南省", "河北省", "陕西省", "山西省", "江西省", "广东省"}[new Random().nextInt(6)]);

        addressBook.setCityCode(String.valueOf(new Random().nextInt(9000)));
        addressBook.setCityName(new String[]{"新乡市", "北京市", "深圳市", "上海市", "洛阳市", "南京市"}[new Random().nextInt(6)]);

        addressBook.setDistrictCode(String.valueOf(new Random().nextInt(9000)));
        addressBook.setDistrictName(new String[]{"上城区", "下沙区", "昌平区", "房山区", "大兴区", "通州区"}[new Random().nextInt(6)]);

        addressBook.setDetail(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + "真是一个好地方！" + LocalDateTime.now());

        addressBook.setLabel(addressBook.getProvinceCode() + "-" + addressBook.getCityCode() + "-" + addressBook.getDistrictCode());

        addressBook.setIsDefault(new Integer[]{0, 1}[new Random().nextBoolean() ? 1 : 0]);
        addressBook.setCreateUser(new Random().nextLong(900000000) + 100000000);
        addressBook.setUpdateUser(addressBook.getCreateUser());
        addressBook.setCreateTime(LocalDateTime.now());
        addressBook.setUpdateTime(LocalDateTime.now());
        addressBook.setIsDeleted(new Integer[]{0, 1}[new Random().nextBoolean() ? 1 : 0]);
        return addressBook;
    }

    @Override
    // @Async
    public Integer testAsyncInsertDatas() {
        int actualtotal = 0;

        for (int i = 0; i < TOTAL_INSERT_DATA / EACH_INSERT; i++) {
            List<AddressBook> addressBookList = new ArrayList<>();
            for (int j = 0; j < EACH_INSERT; j++) {
                AddressBook addressBook = randomCreateAddressBook();
                addressBookList.add(addressBook);
            }
            actualtotal += EACH_INSERT;

            List<AddressBook> addressBooks = addressBookRepository.saveAll(addressBookList);
            if (addressBooks.size() == EACH_INSERT) {
                logger.info("异步第" + i + "个" + EACH_INSERT + "地址插入成功");
            }

        }
        logger.info(TOTAL_INSERT_DATA + "个地址异步插入成功");
        if (actualtotal != TOTAL_INSERT_DATA) {
            throw new RuntimeException("批量异步插入数据失败");
        }


        return actualtotal;
    }
}
