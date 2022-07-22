const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    content: '',
    url: '',
  },

  submit(){
    wx.showModal({
      title: '确认发布本期表白墙?',
      success: res=>{
        if (res.confirm) {
          if(!this.data.url){
            wx.showToast({
              title: '图片不能为空!',
              icon: 'error'
            })
            return
          }
          wx.showLoading({
            title: '正在发布',
          })
          var url = this.data.url
          var content = this.data.content
          this.setData({
            content: '',
            url: ''
          })
          var token = wx.getStorageSync('token')
          wx.uploadFile({
            filePath: url,
            name: 'img',
            url: app.globalData.URL + '/manager/push/articles',
            header: {
              'content-type': 'multipart/form-data',
              'token': token
            },
            formData:{
              'content': content
            },
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
              if(res.data.code == 200){
                this.setData({
                  content: '',
                  url: ''
                })
                wx.showToast({
                  title: '发布成功!',
                  icon: 'success'
                })
              }
            },
            fail:res=>{
              wx.hideLoading({
                success: (res) => {},
              })
              wx.showToast({
                title: '发布失败!',
                icon: 'error'
              })
            }
          })
        }
      }
    })
  },
  handleChange:function(e){
    this.setData({
      content: e.detail.value
    })
  },
  addImg(){
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      camera: 'back',
      success: res=> {
        if(res.tempFiles.length > 0){
          this.setData({
            url: res.tempFiles[0].tempFilePath
          })
        }
      }
    })
  },
  delImg: function(e){
    this.setData({
      url: ''
    })
  },
  previewImgs(){
    var list = []
    list.push(this.data.url)
    wx.previewImage({
      current: list[0],
      urls: list
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