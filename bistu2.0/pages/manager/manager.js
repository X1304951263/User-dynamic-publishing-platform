const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    flag: 0,
    list: [
      {
        avatar: '../../images/love.png',
        nickName: '一曲红梦'
      },
      {
        avatar: '../../images/love.png',
        nickName: '一曲红尘雨梦'
      },
      {
        avatar: '../../images/love.png',
        nickName: '一曲红尘烟雨梦'
      }
    ]
  },
  getApplyer(){
    this.setData({
      flag: 0
    })
    this.getData()
  },
  getManager(){
    this.setData({
      flag: 1
    })
    this.getData1()
  },
  getData1(){
    wx.request({
      url: app.globalData.URL + '/user/get/manager',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
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
          list: res.data.data
        })
      }
    })
  },
  getData(){
    wx.request({
      url: app.globalData.URL + '/user/get/applyer',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
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
          list: res.data.data
        })
      }
    })
  },
  del: function(e){
    var index = e.currentTarget.dataset.index
    var openId = this.data.list[index].openId
    wx.request({
      url: app.globalData.URL + '/user/del/manager',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      data:{
        'openId': openId
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
          this.getData1()
          wx.showToast({
            title: '取消成功！',
            icon: 'success'
          })
        }else{
          wx.showToast({
            title: '取消失败！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '取消失败！',
          icon: 'error'
        })
      }
    })
  },
  add:function(e){
    var index = e.currentTarget.dataset.index
    var openId = this.data.list[index].openId
    wx.request({
      url: app.globalData.URL + '/user/set/manager',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      data:{
        'openId': openId
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
          this.getData()
          wx.showToast({
            title: '申请成功！',
            icon: 'success'
          })
        }else{
          wx.showToast({
            title: '申请失败！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '申请失败！',
          icon: 'error'
        })
      }
    })
  },
  toUser:function(e){
    var index = e.currentTarget.dataset.index
    var that = this
    wx.navigateTo({
      url: '../personalPage/personalPage?openId=' + that.data.list[index].openId,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getData()
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