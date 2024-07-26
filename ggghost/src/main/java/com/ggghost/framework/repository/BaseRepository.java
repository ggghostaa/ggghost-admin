package com.ggghost.framework.repository;

import com.ggghost.framework.svo.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-11
 * @Description:
 * @Version: 1.0
 */
@NoRepositoryBean
public interface BaseRepository <E extends AbstractEntity> extends JpaRepository<E, Serializable>, JpaSpecificationExecutor<E> {
}
