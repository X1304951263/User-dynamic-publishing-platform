const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    info: {
      openId: 'o2QnI5YIRgDmtpuC53RyonoiitTk',
      avatar: 'http://127.0.0.1:8081/avatar/1649941483012o2QnI5YIRgDmtpuC53RyonoiitTk.jpg',
      nickName: '一曲红尘烟雨梦住',
      gender: 1,
      campus: 3,
      signature: '一曲红尘烟雨梦此生何惧风儿啥，悲白发留不住。'
    },
    openId: ''
  },
  //举报
  inform(){
    wx.navigateTo({
      url: '../inform/inform?openId=' + this.data.info.openId,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      openId: wx.getStorageSync('userInfo').openId
    })
    var openId = options.openId
    wx.request({
      url: app.globalData.URL + '/user/get/userPage',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      method: 'POST',
      data:{
        'openId': openId
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
          info: res.data.data
        })
      }
    })
  },
  previewImgs(){
    var list = []
    list.push(this.data.info.avatar)
    wx.previewImage({
      current: list[0],
      urls: list
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