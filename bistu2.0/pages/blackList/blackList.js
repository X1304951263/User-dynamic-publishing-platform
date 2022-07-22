const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    flag: 0,
    list: []
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
      url: app.globalData.URL + '/user/get/lockedUser',
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
      url: app.globalData.URL + '/user/get/informedUser',
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
  add:function(e){
    var that = this
    var index = e.currentTarget.dataset.index
    var str = {
      'openId': that.data.list[index].openId,
      'avatar': that.data.list[index].avatar,
      'nickName': that.data.list[index].nickName
    }
    var s = JSON.stringify(str)
    wx.navigateTo({
      url: '../blackReason/blackReason?info=' + s,
    })
  },
  unlock:function(e){
    wx.showLoading({
      title: '正在解封!',
    })
    var index = e.currentTarget.dataset.index
    wx.request({
      url: app.globalData.URL + '/user/unlock/blackUser',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      data:{
        'openId': this.data.list[index].openId
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
          wx.hideLoading({
            success: (res) => {},
          })
          wx.showToast({
            title: '解封成功！',
            icon: 'success'
          })
          this.getData1()
        }else{
          wx.hideLoading({
            success: (res) => {},
          })
          wx.showToast({
            title: '解封失败！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.hideLoading({
          success: (res) => {},
        })
        wx.showToast({
          title: '解封失败！',
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