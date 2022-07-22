const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: 0,
    openId: '123',
    send: 0,
    p: 1,
    focus: false,
    holder: '添加评论......',
    item: {},
    content: '',
    content1: '',
    height: 0,
    openId_two: '',
    nickName_two: '',
    list: [],
    loveUrl: '../../images/love.png',
    loveUrl2: '../../images/love1.png',
  },
  del: function(e){
    console.log('删除按钮')
    var index = e.currentTarget.dataset.index
    var id = this.data.list[index].id
    var openId = this.data.list[index].openId_one
    if(openId != this.data.openId){
      return 0
    }
    var token = wx.getStorageSync('token')
    wx.request({
      url: app.globalData.URL + '/article/comment/del',
      header: {
        'content-type': 'application/json',
        'token': token
      },
      data:{
        'id': id,
        'articleId': this.data.item.id
      },
      method: 'POST',
      success: res => {
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        if(res.data.code == 200){
          var item = this.data.item
          item.comment--
          this.setData({
            item:item
          })
          this.getData()
        }else{
          wx.showToast({
            title: '删除失败！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '删除失败！',
          icon: 'error'
        })
      }
    })
    
  },
  send(){
    if(this.data.p == 1){
      console.log('评论')
      if(!this.data.content){
        console.log('未发送请求')
        return 0
      }
      var token = wx.getStorageSync('token')
      wx.request({
        url: app.globalData.URL + '/article/comment/insert',
        header: {
          'content-type': 'application/json',
          'token': token
        },
        data:{
          'nickName_one': this.data.nickName_one,
          'openId_two': '',
          'nickName_two': '',
          'content': this.data.content,
          'articleId': this.data.item.id
        },
        method: 'POST',
        success: res => {
          if(res.statusCode == 800){
            wx.clearStorage()
            wx.redirectTo({
              url: '/pages/login/login',
            })
            return 
          }
          if(res.data.code == 200){
            var item = this.data.item
            item.comment++
            this.getData()
            this.setData({
              content: '',
              holder: '添加评论',
              send: 0,
              item: item
            })
          }else{
            wx.showToast({
              title: '添加评论失败！',
              icon: 'error'
            })
          }
        },
        fail: res=>{
          wx.showToast({
            title: '添加评论失败！',
            icon: 'error'
          })
        }
      })
    }else{
      console.log('回复')
      if(!this.data.content1){
        return 0
      }
      var token = wx.getStorageSync('token')
      wx.request({
        url: app.globalData.URL + '/article/comment/insert',
        header: {
          'content-type': 'application/json',
          'token': token
        },
        data:{
          'nickName_one': this.data.nickName_one,
          'openId_two': this.data.openId_two,
          'nickName_two': this.data.nickName_two,
          'content': this.data.content1,
          'articleId': this.data.item.id
        },
        method: 'POST',
        success: res => {
          if(res.statusCode == 800){
            wx.clearStorage()
            wx.redirectTo({
              url: '/pages/login/login',
            })
            return 
          }
          if(res.data.code == 200){
            var item = this.data.item
            item.comment++
            this.getData()
            this.setData({
              content: '',
              content1: '',
              send: 0,
              item: item
            })
          }else{
            wx.showToast({
              title: '添加评论失败！',
              icon: 'error'
            })
          }
        },
        fail: res=>{
          wx.showToast({
            title: '添加评论失败！',
            icon: 'error'
          })
        }
      })
    } 
  },
  com: function(e){
    var index = e.currentTarget.dataset.index
    var openId = this.data.list[index].openId_one
    if(this.data.openId == openId){
      var openId_two = this.data.list[index].openId_two
      var name = this.data.list[index].nickName_two
      if(!openId_two){
        this.setData({
          focus: true,
          content: '',
          content1: '',
          send: 0,
          p: 1,
          holder: '添加评论......'
        })
      }else{
        var holder = '回复' + name
        this.setData({
          focus: true,
          holder: holder,
          content: '',
          content1: '',
          send: 0,
          openId_two: openId_two,
          nickName_two: name,
          p: 2
        })
      }
    }else{
      var openId_one = this.data.list[index].openId_one
      var name = this.data.list[index].nickName_one
      var holder = '回复' + name
      this.setData({
        focus: true,
        holder: holder,
        content: '',
        content1: '',
        openId_two: openId_one,
        send: 0,
        nickName_two: name,
        p:2
      })
    }
  },
  focus: function(e){
    var height = e.detail.height
    this.setData({
      height: height
    })
  },
  clear(){
    this.setData({
      holder: '添加评论......',
      content: '',
      p: 1,
      send: 0
    })
  },
  lur: function(e){
    if(this.data.p == 2){
      this.setData({
        focus: false,
        height: 0
      })
    }else{
      this.setData({
        focus: false,
        height: 0,
      })
    }
  },
  handleChange:function(e){
    if(this.data.p == 1){
      this.setData({
        content: e.detail.value.trim()
      })
    }else{
      this.setData({
        content1: e.detail.value.trim()
      })
    }
    if(e.detail.value.trim()){
      this.setData({
        send: 2
      })
    }else{
      this.setData({
        send: 0,
      })
    }
  },
  love(){
    var item = this.data.item
    var token = wx.getStorageSync('token')
    wx.request({
      url: app.globalData.URL + '/article/love',
      data:{
        'articleId': item.id,
        'id': this.data.id
      },
      header: {
        'content-type': 'application/json',
        'token': token
      },
      method: 'POST',
      success: res => {
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        if(res.data.code == 200){
          if(item.isLove == 0){
            item.isLove = 1
            item.love++
            this.setData({
              item: item
            })
          }else{
            item.isLove = 0
            item.love--
            this.setData({
              item: item
            })
          }
        }else if(res.data.code == 501){
          wx.showToast({
            title: '操作失败！',
            icon: 'error'
          })
        }else{
          wx.showToast({
            title: '服务异常！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '服务异常！',
          icon: 'error'
        })
      }
    })
    
  },
  getData(){
    var token = wx.getStorageSync('token')
    this.setData({
      nickName_one: wx.getStorageSync('userInfo').nickName,
      openId: wx.getStorageSync('userInfo').openId
    })
    wx.request({
      url: app.globalData.URL + '/article/comment/get',
      header: {
        'content-type': 'application/json',
        'token': token
      },
      data:{
        'articleId': this.data.item.id
      },
      method: 'POST',
      success: res => {
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        if(res.data.code == 200){
          this.setData({
            list: res.data.data
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '获取评论失败！',
          icon: 'error'
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var item = JSON.parse(options.article)
    var id = options.id
    this.setData({
      item: item,
      id: id
    })
    this.getData()
  },

  previewImgs1: function(e){
    var list = this.data.item.imgs;
    wx.previewImage({
      current: list[0],
      urls: list
    })
  },
  previewImgs2: function(e){
    var list = this.data.item.imgs;
    wx.previewImage({
      current: list[1],
      urls: list
    })
  },
  previewImgs3: function(e){
    var list = this.data.item.imgs;
    wx.previewImage({
      current: list[2],
      urls: list
    })
  },
  toUser(){
    var openId = this.data.item.openId
    wx.navigateTo({
      url: '../personalPage/personalPage?openId=' + openId,
    })
  },
  toUser1:function(e){
    var index = e.currentTarget.dataset.index
    var openId = this.data.list[index].openId_one
    wx.navigateTo({
      url: '../personalPage/personalPage?openId=' + openId,
    })
  },
  toUser2:function(e){
    var index = e.currentTarget.dataset.index
    var openId = this.data.list[index].openId_two
    wx.navigateTo({
      url: '../personalPage/personalPage?openId=' + openId,
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})