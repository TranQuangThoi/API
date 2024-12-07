package com.techmarket.api.repository;

import com.techmarket.api.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Long>, JpaSpecificationExecutor<Images> {

    List<Images> findAllByProductVariantId(Long id);
}
