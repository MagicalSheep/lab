package cn.magicalsheep.wbclient.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cn.magicalsheep.wbclient.data.Blog
import cn.magicalsheep.wbclient.data.User
import cn.magicalsheep.wbclient.data.repository.BlogRepository
import cn.magicalsheep.wbclient.data.repository.UserRepository
import cn.magicalsheep.wbclient.data.repository.impl.BlogRepositoryImpl
import cn.magicalsheep.wbclient.data.repository.impl.UserRepositoryImpl
import cn.magicalsheep.wbclient.ui.paging.BlogSource
import cn.magicalsheep.wbclient.ui.paging.MyBlogSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepositoryImpl(),
    private val blogRepository: BlogRepository = BlogRepositoryImpl()
) :
    ViewModel() {

    private val _loginUserState = MutableStateFlow(LoginUserState())
    val loginUserState: StateFlow<LoginUserState> = _loginUserState
    private val _isLogin = MutableStateFlow(false)
    val isLogin: StateFlow<Boolean> = _isLogin

    init {
        viewModelScope.launch {
            // get token from local storage
            val token: String? = null

            // if token is not null, initialize login user state
            if (token != null) {
                try {
                    val loginUser = userRepository.getUserByToken(token)
                    updateLoginUserState(loginUser)
                } catch (ex: Exception) {
                    // ignore
                }
            }
        }
    }

    private fun updateLoginUserState(user: User) {
        _isLogin.value = true
        _loginUserState.value.user = user
        _loginUserState.value.blogList = Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20)
        ) {
            MyBlogSource(user.name, blogRepository)
        }.flow.cachedIn(viewModelScope)
    }

    private fun resetLoginState() {
        _isLogin.value = false
        _loginUserState.value.user = null
        _loginUserState.value.blogList = null
    }

    fun logout() {
        userRepository.logout()
        resetLoginState()
    }

    suspend fun login(userName: String, pwd: String) {
        // login to get user token
        val token = userRepository.login(userName, pwd)

        // get user by token
        val loginUser = userRepository.getUserByToken(token)

        // update state
        updateLoginUserState(loginUser)
    }

    suspend fun register(userName: String, pwd: String) {
        userRepository.register(userName, pwd)
    }

}

data class LoginUserState(
    var user: User? = null,
    var blogList: Flow<PagingData<Blog>>? = null
)