const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [],
    idx: 0
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
  surface(){
    wx.redirectTo({
      url: '/pages/surface/surface',
    })
  },
  getData(){
    wx.request({
      url:app.globalData.URL + '/manager/get/articles',//服务器接口
      method: 'POST',
      header: {
        'content-type': 'application/form-data',
        'token': wx.getStorageSync('token')
      },
      success: res=> {
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        if(res.data.code == 200){
          this.setData({
            list: res.data.data.list
          })
        }else{
          wx.showToast({
            title: '加载失败!',
            icon: 'error'
          })
        }
      },
      fail: res=> {
        wx.showToast({
          title: '加载失败!',
          icon: 'error'
        })
      }
    })
  },
  getDat(){
    wx.request({
      url:app.globalData.URL + '/manager/get/articled',//服务器接口
      method: 'POST',//这句话好像可以不用
      header: {
        'content-type': 'application/form-data',
        'token': wx.getStorageSync('token')
      },
      success: res=> {
        if(res.statusCode == 800){
          wx.clearStorage()
          wx.redirectTo({
            url: '/pages/login/login',
          })
          return 
        }
        if(res.data.code == 200){
          this.setData({
            list: res.data.data.list
          })
        }else{
          wx.showToast({
            title: '加载失败!',
            icon: 'error'
          })
        }
      },
      fail: res=> {
        wx.showToast({
          title: '加载失败!',
          icon: 'error'
        })
      }
    })
  },
  getArticle(){
    this.setData({
      idx: 0
    })
    this.getData()
  },
  getChoosedArticles(){
    this.setData({
      idx: 1
    })
    this.getDat()
  },
  hide: function(e){
    var index = e.currentTarget.dataset.index;
    /* console.log(this.data.list[index])
    console.log(this.data.list[index].id) */
    wx.showModal({
      title: '确认屏蔽该动态？',
      success: res=>{
        if (res.confirm) {
          //console.log('用户点击确定')
          var token = wx.getStorageSync('token')
          wx.request({ 
            url: app.globalData.URL + '/manager/hide/article',
            header: {
              'content-type': 'application/json',
              'token': token
            },
            method: 'POST',
            data: {
              'id': this.data.list[index].id
            },
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
                  title: '屏蔽成功!',
                  icon: 'success'
                })
                this.getDat()
              }else if(res.data.code == 501){
                wx.showToast({
                  title: '屏蔽失败!',
                  icon: 'error'
                })
              }else{
                wx.showToast({
                  title: '服务异常!',
                  icon: 'error'
                })
              }
            },
            fail: res=>{
              wx.showToast({
                title: '屏蔽失败!',
                icon: 'error'
              })
            }
          })
        } 
      }
    })
  },
  setRed: function(e){
    var index = e.currentTarget.dataset.index;
    wx.showModal({
      title: '确认标红该动态？',
      success: res=>{
        if (res.confirm) {
          var token = wx.getStorageSync('token')
          wx.request({ 
            url: app.globalData.URL + '/manager/setRed/article',
            header: {
              'content-type': 'application/json',
              'token': token
            },
            method: 'POST',
            data: {
              'id': this.data.list[index].id
            },
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
                  title: '操作成功!',
                  icon: 'success'
                })
                this.getDat()
              }else if(res.data.code == 501){
                wx.showToast({
                  title: '操作失败!',
                  icon: 'error'
                })
              }else{
                wx.showToast({
                  title: '服务异常!',
                  icon: 'error'
                })
              }
            },
            fail: res=>{
              wx.showToast({
                title: '操作失败!',
                icon: 'error'
              })
            }
          })
        } 
      }
    })
  },
  pub: function(e){
    var index = e.currentTarget.dataset.index;
    wx.showModal({
      title: '确认发布该动态？',
      success: res=>{
        if (res.confirm) {
          var token = wx.getStorageSync('token')
          wx.request({ 
            url: app.globalData.URL + '/manager/choose/article',
            header: {
              'content-type': 'application/json',
              'token': token
            },
            method: 'POST',
            data: {
              'id': this.data.list[index].id
            },
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
                  title: '发布成功!',
                  icon: 'success'
                })
                this.getData()
              }else if(res.data.code == 503){
                this.getData()
              }else if(res.data.code == 501){
                wx.showToast({
                  title: '发布失败!',
                  icon: 'error'
                })
              }else{
                wx.showToast({
                  title: '服务异常!',
                  icon: 'error'
                })
              }
            },
            fail: res=>{
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
  del: function(e){
    var index = e.currentTarget.dataset.index;
    console.log(this.data.list[index])
    wx.showModal({
      title: '确认删除该动态？',
      success: res=>{
        if (res.confirm) {
          wx.request({ 
            url: app.globalData.URL + '/manager/del/article',
            header: {
              'content-type': 'application/json',
              'token': wx.getStorageSync('token')
            },
            method: 'POST',
            data: {
              'id': this.data.list[index].id
            },
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
                  title: '删除成功!',
                  icon: 'success'
                })
                this.getData()
              }else if(res.data.code == 501){
                wx.showToast({
                  title: '删除失败!',
                  icon: 'error'
                })
              }else{
                wx.showToast({
                  title: '服务异常!',
                  icon: 'error'
                })
              }
            },
            fail: res=>{
              wx.showToast({
                title: '删除失败!',
                icon: 'error'
              })
            }
          })
        } 
      }
    })
  },
  del1: function(e){
    var index = e.currentTarget.dataset.index;
    wx.showModal({
      title: '确认删除该动态？',
      success: res=>{
        if (res.confirm) {
          var token = wx.getStorageSync('token')
          wx.request({ 
            url: app.globalData.URL + '/manager/del/article',
            header: {
              'content-type': 'application/json',
              'token': token
            },
            method: 'POST',
            data: {
              'id': this.data.list[index].id
            },
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
                  title: '删除成功!',
                  icon: 'success'
                })
                this.getDat()
              }else if(res.data.code == 501){
                wx.showToast({
                  title: '删除失败!',
                  icon: 'error'
                })
              }else{
                wx.showToast({
                  title: '服务异常!',
                  icon: 'error'
                })
              }
            },
            fail: res=>{
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
  toUser:function(e){
    var index = e.currentTarget.dataset.index
    var that = this
    wx.navigateTo({
      url: '../personalPage/personalPage?openId=' + that.data.list[index].openId,
    })
  },
  previewImgs1: function(e){
    var index = e.currentTarget.dataset.index;
    var list1 = this.data.list[index].imgs;
    wx.previewImage({
      current: list1[0],
      urls: list1
    })
  },
  previewImgs2: function(e){
    var index = e.currentTarget.dataset.index;
    var list1 = this.data.list[index].imgs;
    wx.previewImage({
      current: list1[1],
      urls: list1
    })
  },
  previewImgs3: function(e){
    var index = e.currentTarget.dataset.index;
    var list1 = this.data.list[index].imgs;
    wx.previewImage({
      current: list1[2],
      urls: list1
    })
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