package cn.magicalsheep.wbclient.data.local

import cn.magicalsheep.wbclient.data.Comment

object LocalCommentDataProvider {

    val blog1_comments = listOf(
        Comment(
            id = 1,
            author = "Bob",
            content = "Great blog",
            goods = 2,
            createTime = "2 mins ago"
        ),
        Comment(
            id = 2,
            author = "Alice",
            content = "Wow cool blog",
            goods = 5,
            createTime = "3 mins ago"
        ),
        Comment(
            id = 3,
            author = "Mike",
            content = "Funny mud pee",
            goods = 0,
            createTime = "12 mins ago"
        ),
        Comment(
            id = 4,
            author = "John",
            content = "How did u do that",
            goods = 2,
            createTime = "2 hours ago"
        ),
        Comment(
            id = 5,
            author = "Tiger",
            content = "I just want to say something long long long long long long long long long long long to test",
            goods = 8,
            createTime = "3 hours ago"
        )
    )

    val blog2_comments = listOf(
        Comment(
            id = 6,
            author = "Sheep",
            content = "Let's say something cool",
            goods = 0,
            createTime = "10 mins ago"
        ),
        Comment(
            id = 7,
            author = "Dog",
            content = "Another comment",
            goods = 12,
            createTime = "2 hours ago"
        ),
        Comment(
            id = 8,
            author = "Cat",
            content = "I am cat",
            goods = 89,
            createTime = "3 hours ago"
        )
    )

    val allComments = HashMap<Int, List<Comment>>()

    init {
        allComments[1] = blog1_comments
        allComments[2] = blog2_comments
    }
}