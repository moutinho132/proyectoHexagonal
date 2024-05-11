package com.martzatech.vdhg.crmprojectback.domains.chat.repositories;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageFileRepository extends JpaRepository<FileEntity, Integer>,
        JpaSpecificationExecutor<FileEntity> {

    @Query(value = "SELECT tf.* " +
            " FROM vdhg_db.t_customers tc \n" +
            "          inner join t_person_file tpf on tpf.person_id=tc.person_id \n" +
            "          inner join t_files tf on tf.id =tpf.file_id where tc.id=:id",nativeQuery = true)
    List<FileEntity> findAllCustomerPersonFile(@Param("id") Integer id, Pageable pageable);

    Optional<FileEntity> findByUrl(@Param("url") String url);

    @Query(value = "SELECT tf.* " +
            " FROM vdhg_db.t_customers tc \n" +
            "          inner join t_person_file tpf on tpf.person_id=tc.person_id \n" +
            "          inner join t_files tf on tf.id =tpf.file_id where tc.id=:id",nativeQuery = true)
    List<FileEntity> findAllCustomerPersonFileNotPageable(@Param("id") Integer id);

    /*@Query(value = "SELECT tf.* FROM t_files tf \n" +
            "inner join t_order_file tof on tof.file_id =tf.id \n" +
            "inner join t_bookings_offers tor on tor.id =tof.order_id \n" +
            "where tor.id =:id",nativeQuery = true)
    List<FileEntity> findAllOrderFile(@Param("id") Integer id);*/

    /*@Query(value = "SELECT tf.* FROM t_files tf \n" +
            "inner join t_offer_file tof on tof.file_id =tf.id \n" +
            "inner join t_bookings_offers tor on tor.id =tof.offer_id \n" +
            "where tor.id =:id",nativeQuery = true)
    List<FileEntity> findAllOfferFile(@Param("id") Integer id);*/

    @Query(value = "SELECT tf.* FROM t_files tf \n" +
            "inner join t_chat_files tcf on tcf.file_id  =tf.id \n" +
            "inner join t_chat_messages tcm on tcm.id =tcf.message_id \n" +
            "where tcm.id =:id",nativeQuery = true)
    List<FileEntity> findAllChatMessageFile(@Param("id") Integer id);

    @Query(value = "SELECT tf.* FROM t_files tf \n" +
            "inner join t_products_files tpf on tpf.file_id=tf.id  \n" +
            "inner join t_products tp on tp.id=tpf.product_id \n" +
            "where tp.id =:id",nativeQuery = true)
    List<FileEntity> findAllProductFile(@Param("id") Integer id);

    @Query(value = "SELECT tf.* FROM t_files tf \n" +
            "left join t_bookings_products_files tpf on tpf.file_id =tf.id\n" +
            "left join t_bookings_product tbp on tbp.id =tpf.bocking_product_id \n" +
            "where  tbp.id  =:id",nativeQuery = true)
    List<FileEntity> findAllProductPreOfferFile(@Param("id") Integer id);

    @Query(value = "SELECT tf.* FROM t_files tf \n" +
            "left join t_bookings_products_files tpf on tpf.file_id =tf.id\n" +
            "left join t_bookings_product tbp on tbp.id =tpf.bocking_product_id \n" +
            "where  tbp.id  =:id",nativeQuery = true)
    List<FileEntity> findAllProductOfferFile(@Param("id") Integer id);


    @Query(value = "SELECT tf.* FROM t_files tf \n" +
            "inner join t_vendor_files tvf on tvf.file_id =tf.id \n" +
            "inner join t_vendors tv on tv.id =tvf.vendor_id  \n" +
            "where tv.id =:id",nativeQuery = true)
    List<FileEntity> findAllVendorFile(@Param("id") Integer id);

}
