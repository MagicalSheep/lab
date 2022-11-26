package cn.magicalsheep.expressserver.repository

import cn.magicalsheep.expressserver.entity.pojo.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User?, Long?> {

    fun findUserByAccount(account: String): User?
}