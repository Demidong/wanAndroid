package com.xd.demi.bean


/**
 * Created by demi on 2018/8/11 下午1:56.
 */

class Event(var date: Long, var dateStr: String, var completeDate: Long, var completeDateStr: String,var content:String,var title:String,var id :Long,var userId: Long,var type :Int,var status:Int)

class Data(var todoList: ArrayList<TodoData>, var doneList: ArrayList<TodoData>, var type: String)

class TodoData(var date: Long, var todoList: ArrayList<Event>)

class User(var username:String,var password:String,var email:String,var icon:String,var id : Long,var type:Long,var collectIds :ArrayList<String>)