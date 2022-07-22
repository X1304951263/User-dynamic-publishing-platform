const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    info: {
      openId: '123',
      avatar: 'http://127.0.0.1:8081/avatar/1650372748885o2QnI5YIRgDmtpuC53RyonoiitTk.png',
      nickName: '一曲红尘'
    },
    list: [
      {
        content: '一晌贪欢sdf sdf sdf dsfsd f',
        img: 'http://127.0.0.1:8081/avatar/1650372748885o2QnI5YIRgDmtpuC53RyonoiitTk.png'
      },
      {
        content: '一晌贪欢',
        img: 'http://127.0.0.1:8081/avatar/1650372748885o2QnI5YIRgDmtpuC53RyonoiitTk.png'
      },
      {
        content: '一晌贪欢',
        img: 'http://127.0.0.1:8081/avatar/1650372748885o2QnI5YIRgDmtpuC53RyonoiitTk.png'
      }
    ]
  },
  del(){
    wx.request({
      url: app.globalData.URL + '/user/del/informedUser',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      data:{
        'openId': this.data.info.openId
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
          wx.showToast({
            title: '删除成功！',
            icon: 'success'
          })
          this.sleep(1)
          wx.navigateTo({
            url: '../blackList/blackList',
          })
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
  lock(){
    wx.request({
      url: app.globalData.URL + '/user/lock/informedUser',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      data:{
        'openId': this.data.info.openId
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
          wx.showToast({
            title: '封禁成功！',
            icon: 'success'
          })
          this.sleep(1)
          wx.redirectTo({
            url: '../blackList/blackList',
          })
        }else{
          wx.showToast({
            title: '封禁失败！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '封禁失败！',
          icon: 'error'
        })
      }
    })
  },
  toUser(){
    wx.navigateTo({
      url: '../personalPage/personalPage?openId=' + this.data.info.openId,
    })
  },
  sleep: function(e) {
    for(var t = Date.now(); Date.now() - t <= e;);
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var item = JSON.parse(options.info)
    this.setData({
      info: item
    })
    wx.request({
      url: app.globalData.URL + '/user/get/informReason',
      header: {
        'content-type': 'application/json',
        'token': wx.getStorageSync('token')
      },
      data:{
        'openId': item.openId
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
        }else{
          wx.showToast({
            title: '获取数据失败！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '获取数据失败！',
          icon: 'error'
        })
      }
    })
  },
  
  previewImgs: function(e){
    var index = e.currentTarget.dataset.index;
    var img = this.data.list[index].img;
    var list = []
    list.push(img)
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