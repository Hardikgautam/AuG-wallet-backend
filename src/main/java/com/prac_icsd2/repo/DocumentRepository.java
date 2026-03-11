package com.prac_icsd2.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prac_icsd2.enums.DocType;
import com.prac_icsd2.model.CustomerDocuments_Path;

public interface DocumentRepository extends JpaRepository<CustomerDocuments_Path, Integer> {

    List<CustomerDocuments_Path> findByCustomer_CustomerIdAndStatusTrue(int customerId);

    Optional<CustomerDocuments_Path> findByCustomer_CustomerIdAndDocnameAndStatusTrue(int customerId, DocType docType);

    Optional<CustomerDocuments_Path> findByDocumentidAndStatusTrue(int documentId);

}