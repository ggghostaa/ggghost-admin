package com.ggghost.framework.svo;

import java.io.Serializable;

public interface IEntity<ID extends Serializable> {
    ID getId();
    void setId(ID id);
}
