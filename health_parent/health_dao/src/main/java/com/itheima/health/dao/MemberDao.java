package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

public interface MemberDao {

    Member findMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByRegTime(String regTime);

    Integer findTodayNewMember(String reportData);

    Integer findTotalMember();

    Integer findThisWeekNewMember(String day);
}
