package cn.magicalsheep.expressserver.repository

import cn.magicalsheep.expressserver.entity.pojo.Package
import org.springframework.data.jpa.repository.JpaRepository

interface PackageRepository : JpaRepository<Package?, Long?>