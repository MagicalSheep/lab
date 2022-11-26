package cn.magicalsheep.expressserver.entity.pojo

import cn.magicalsheep.expressserver.entity.dto.UserDto
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "user")
class User(
    @Column(name = "account", nullable = false, unique = true)
    var account: String,
    @Column(name = "pwd", nullable = false)
    var pwd: String,
    @Column(name = "role", nullable = false)
    var role: String,
    @Column(name = "name", nullable = true)
    var name: String? = null,
    @Column(name = "address", nullable = true)
    var address: String? = null,
    @Column(name = "phone", nullable = true)
    var phone: String? = null,
    @Column(name = "gender", nullable = true)
    var gender: String? = null
) : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private val id: Long? = null

    @ManyToMany
    @JoinTable(
        name = "package_access",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "package_id")]
    )
    var accessPackages: MutableSet<Package> = mutableSetOf()

    constructor(user: UserDto) : this(
        user.account,
        user.pwd!!,
        "default",
        user.name,
        user.address,
        user.phone,
        user.gender
    )

    fun updateFromDto(source: UserDto) {
        this.pwd = source.pwd ?: this.pwd
        this.name = source.name ?: this.name
        this.address = source.address ?: this.address
        this.phone = source.phone ?: this.phone
        this.gender = source.gender ?: this.gender
    }
}