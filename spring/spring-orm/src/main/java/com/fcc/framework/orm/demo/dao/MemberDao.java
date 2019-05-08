package com.fcc.framework.orm.demo.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.fcc.framework.orm.BaseDaoSupport;
import com.fcc.framework.orm.QueryRule;
import com.fcc.framework.orm.demo.entity.Member;

/**
 * Created by 傅泉明.
 */
@Repository
public class MemberDao extends BaseDaoSupport<Member,Long> {

    @Override
    protected String getPKColumn() {
        return "id";
    }

    @Resource(name="dataSource")
    public void setDataSource(DataSource dataSource){
        super.setDataSourceReadOnly(dataSource);
        super.setDataSourceWrite(dataSource);
    }


    public List<Member> selectAll() throws  Exception{
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.andLike("name","Tom%");
        return super.select(queryRule);
    }
}
