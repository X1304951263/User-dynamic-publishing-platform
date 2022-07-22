const app = getApp()
Page({
  data: {
    color: 5,
    imgPaths: [],
    imgLength: 0,
    count: 1,
    content: '',
    openId: ''
  },
  handleChange:function(e){
    this.setData({
      content: e.detail.value
    })
  },
  addImg(){
    if(this.data.count == 0){
      return 
    }
    wx.chooseMedia({
      count: this.data.count,
      mediaType: ['image'],
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      camera: 'back',
      success: res=> {
        var paths = []
        if(res.tempFiles.length > 0){
          paths = this.data.imgPaths
          for(var i = 0; i < res.tempFiles.length; i++){
            paths.push(res.tempFiles[i].tempFilePath)
          }
          var count = this.data.count - res.tempFiles.length
          this.setData({
            imgPaths: paths,
            imgLength: paths.length,
            count: count
          })
        }
      }
    })
  },
  delImg: function(e){
    var index = e.currentTarget.dataset.index;
    var list = this.data.imgPaths
    list.splice(index, 1);
    var c = this.data.count + 1
    this.setData({
      imgPaths: list,
      count: c
    })
  },
  previewImgs: function(e){
    var index = e.currentTarget.dataset.index;
    var list = this.data.imgPaths;
    wx.previewImage({
      current: list[index],
      urls: list
    })
  },
  submit(){
    var content = this.data.content.trim()
    if(content == null || content.length == 0){
      wx.showToast({
        title: '内容不能为空！',
        icon: 'error'
      })
      return 
    }
    var token = wx.getStorageSync('token')
    var base64 = ''
    if(this.data.imgPaths.length > 0){
      console.log('1532456465')
      var base = wx.getFileSystemManager().readFileSync(this.data.imgPaths[0],'base64')
      base64 = String('data:image/png;base64,' + base)
    }
    wx.request({ 
      url: app.globalData.URL + '/user/inform/user',
      data: {
        'openId': this.data.openId,
        'content': this.data.content,
        'imgBase64': base64,
      },
      header: {
        'content-type': 'application/json',
        'token': token
      },
      method: 'POST',
      success: res=>{
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        if(res.data.code == 200){
          wx.showToast({
            title: '已举报！',
            icon: 'success'
          })
          this.setData({
            count: 1,
            content: '',
            imgPaths: [],
            imgLength: 0
          })
        }else{
          wx.showToast({
            title: '举报失败！',
            icon: 'error'
          })
        }
      }
    })
  },
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var openId = options.openId
    this.setData({
      openId: openId
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