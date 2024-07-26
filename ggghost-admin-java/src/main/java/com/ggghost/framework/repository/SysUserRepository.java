package com.ggghost.framework.repository;

import com.ggghost.framework.entity.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-11
 * @Description:
 * @Version: 1.0
 */
@Repository
public interface SysUserRepository extends BaseRepository<SysUser> {

    @Query(value = "select s from SysUser s where s.username = ?1 ")
    SysUser findUserByUsername(String username);

    @Query(value = "select nextval('sys.user_token_seq')")
    String getUserTokenSeq();
}
