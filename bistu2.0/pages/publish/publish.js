const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    color: 5,
    imgPaths: [],
    imgLength: 0,
    count: 3,
    content: ''
  },
  handleChange:function(e){
    this.setData({
      content: e.detail.value
    })
  },
  chooseColor(){
    wx.showActionSheet({
      itemList: ['橙色','黄色','绿色','青色','蓝色','黑色'],
      success: res=>{
        if(res.tapIndex != this.data.color ){
          this.setData({
              color: res.tapIndex 
          }),
          wx.request({
            url: 'url',
          })
        }
      }
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
    console.log(index)
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
    var base64 = []
    for(var i = 0; i < this.data.imgPaths.length; i++){
      var base = wx.getFileSystemManager().readFileSync(this.data.imgPaths[i],'base64')
      var a = String('data:image/png;base64,' + base)
      base64[i] = a
    }
    //console.log(base64[0])
    wx.showLoading({
      title: '提交中...',
    })
    wx.request({ 
      url: app.globalData.URL + '/user/submit/article',
      data: {
        'content': this.data.content,
        'imgBase64': base64,
        'color': this.data.color
      },
      header: {
        'content-type': 'application/json',
        'token': token
      },
      method: 'POST',
      success: res=>{
        wx.hideLoading({
          success: (res) => {},
        })
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        //res.data = JSON.parse(res.data)
        if(res.data.code == 200){
          wx.showToast({
            title: '已提交！',
            icon: 'success'
          })
          this.setData({
            color: 5,
            imgPaths: [],
            imgLength: 0,
            count: 3,
            content: ''
          })
        }else{
          wx.showToast({
            title: '提交失败！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.hideLoading({
          success: (res) => {},
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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