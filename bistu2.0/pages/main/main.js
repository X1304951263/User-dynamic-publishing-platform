const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [],
    surface: {},
    list2: [],
    loveUrl: '../../images/love.png',
    loveUrl2: '../../images/love1.png',
    idx: 0,   //动态或者文案下标  0代表动态，1代表切换至文案
    index: -1  // 封面id
  },

  getComment:function(e){
    var index = e.currentTarget.dataset.index
    console.log(this.data.list[index])
    var str= JSON.stringify(this.data.list[index]);
    var id = this.data.surface.id
    wx.navigateTo({
      url: '../comment/comment?article=' + str + '&id=' + id,
    })
  },
  viewSurface(){
    var list = []
    list.push(this.data.surface.img)
    wx.previewImage({
      current: list[0],
      urls: list
    })
  },
  getArticles(){
    this.setData({
      idx: 0
    })
  },
  getSurfaces(){
    this.setData({
      idx: 1
    })
    wx.request({
      url: app.globalData.URL + '/article/get/allSurface',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      method: 'POST',
      success: res => {
        console.log(res.data.data)
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        this.setData({
          list2: res.data.data
        })
      }
    })
  },
  //点赞
  love: function(e){
    //console.log(e.currentTarget.dataset.index)
    var index = e.currentTarget.dataset.index;
    var p = this.data.list[index].isLove
    var list = this.data.list
    var token = wx.getStorageSync('token')
    wx.request({
      url: app.globalData.URL + '/article/love',
      data:{
        'articleId': list[index].id,
        'id': this.data.surface.id
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
          if(p == 0){
            list[index].isLove = 1
            list[index].love++
            this.setData({
              list: list
            })
          }else{
            list[index].isLove = 0
            list[index].love--
            this.setData({
              list: list
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
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },
  toUser:function(e){
    var index = e.currentTarget.dataset.index
    var that = this
    wx.navigateTo({
      url: '../personalPage/personalPage?openId=' + that.data.list[index].openId,
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if(this.data.index == -1){
      this.getData()
    }else{
      var id = this.data.index
      var token = wx.getStorageSync('token')
      wx.request({
        url: app.globalData.URL + '/article/getById',
        header: {
          'content-type': 'application/json',
          'token': token
        },
        method: 'POST',
        data:{
          'id': id
        },
        success: res => {
          if(res.statusCode == 800){
            wx.clearStorage()
            wx.redirectTo({
              url: '/pages/login/login',
            })
            return 
          }
          this.setData({
            list: res.data.data.articles,
          })
        },
        fail: res=>{
          wx.showToast({
            title: '获取数据失败!',
            icon: 'error'
          })
        }
      })
    }
  },
  getArticlesById: function(e){
    var index = e.currentTarget.dataset.index
    var id = this.data.list2[index].id
    var token = wx.getStorageSync('token')
    wx.request({
      url: app.globalData.URL + '/article/getById',
      header: {
        'content-type': 'application/json',
        'token': token
      },
      method: 'POST',
      data:{
        'id': id
      },
      success: res => {
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        this.setData({
          list: res.data.data.articles,
          surface: res.data.data.surface,
          idx: 0,
          index: id
        })
      },
      fail: res=>{
        wx.showToast({
          title: '获取数据失败!',
          icon: 'error'
        })
      }
    })
  },

  getData(){
    var token = wx.getStorageSync('token')
    wx.request({
      url: app.globalData.URL + '/article/get',
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
        this.setData({
          list: res.data.data.articles,
          surface: res.data.data.surface
        })
      }
    })
  },
  previewImgs1: function(e){
    var index = e.currentTarget.dataset.index;
    var list = this.data.list[index].imgs;
    wx.previewImage({
      current: list[0],
      urls: list
    })
  },
  previewImgs2: function(e){
    var index = e.currentTarget.dataset.index;
    var list = this.data.list[index].imgs;
    wx.previewImage({
      current: list[1],
      urls: list
    })
  },
  previewImgs3: function(e){
    var index = e.currentTarget.dataset.index;
    var list = this.data.list[index].imgs;
    wx.previewImage({
      current: list[2],
      urls: list
    })
  },






  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

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