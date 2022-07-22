const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    openId: '',
    word: '',
  },
  getAccount(e){
    this.setData({
        openId: e.detail.value
    })
  },
  getWord(e){
    this.setData({
        word: e.detail.value
    })
  },
  loginOnWx(){
    wx.getSetting({
      success:res=>{
        console.log(res.authSetting["scope.userInfo"])  //该用户是否已经登录注册过此小程序
        if(res.authSetting["scope.userInfo"]){
          wx.login({
            timeout: 1000,
            success: res => {
              wx.showLoading({
                title: '登录验证中',
              })
              var code = res.code
              wx.request({
                url: app.globalData.URL + '/login/wechat',
                data: {
                  code: code,
                },
                header: {
                  'content-type': 'application/json' 
                },
                method: 'POST',
                success: res => {
                  if(res.data.code == 200){
                    wx.switchTab({
                      url: '/pages/main/main',
                    })
                    wx.setStorageSync('userInfo', res.data.data.userInfo)
                    wx.setStorageSync('token', res.data.data.token)
                  }else if(res.data.code == 888){
                    wx.showModal({
                      title: '您的账号已被封禁，请联系超级管理员：13891549166',
                      cancelColor: 'cancelColor',
                    })
                  }else{
                    wx.showToast({
                      title: '登录失败！',
                      icon: 'error'
                    })
                  }
                },
                fail:res=>{
                  fail: res=>{
                    wx.showToast({
                      title: '连接超时！',
                      icon: 'error'
                    })
                  }
                },
                complete:res=>{
                  wx.hideLoading({
                    success: (res) => {},
                  })
                }
              })
            },
            fail:res=>{
              wx.showToast({
                title: '授权失败！',
                icon: 'error'
              })
            }
          })
        }else{
          var code;
          var avatarUrl ;
          var nickName;
          var gender ;
          wx.getUserProfile({
            desc: '授权登录',
            success: res=>{
              avatarUrl = res.userInfo.avatarUrl
              nickName = res.userInfo.nickName
              gender = res.userInfo.gender
              wx.login({
                timeout: 1000,
                success: res => {
                  wx.showLoading({
                    title: '登录验证中',
                  })
                  code = res.code
                  var that = this
                  wx.request({
                    url: app.globalData.URL + '/login/firstWechat',
                    data: {
                      code: code,
                      avatar: avatarUrl,
                      nickName: nickName,
                      gender: gender
                    },
                    header: {
                      'content-type': 'application/json' 
                    },
                    method: 'POST',
                    success:res=> {
                      console.log(res)
                      wx.hideLoading({
                        success: (res) => {},
                      })
                      if(res.data.code == 200){
                        wx.switchTab({
                          url: '/pages/main/main',
                        })
                        wx.setStorageSync('userInfo', res.data.data.userInfo)
                        wx.setStorageSync('token', res.data.data.token)
                      }else if(res.data.code == 888){
                        wx.showModal({
                          title: '您的账号已被封禁，请联系超级管理员：13891549166',
                          cancelColor: 'cancelColor',
                        })
                      }else{
                        wx.showToast({
                          title: '授权登录失败！',
                          icon: 'error'
                        })
                      }
                    }
                  })
                },
                fail: res=>{
                  wx.showToast({
                    title: '连接超时！',
                    icon: 'error'
                  })
                }
              })
            }
          })
        }

      }
    })
  },
  register(){
    wx.navigateTo({
      url: '/pages/register/register',
    })
  },
  login(){
    if(!this.data.openId || !this.data.word){
      wx.showToast({
        title: '账密不能为空！',
        icon: 'error'
      })
      return;
    }
    wx.showLoading({
      title: '登录中...',
    })
    wx.request({
      url: app.globalData.URL + '/login/account',
      data: {
        openId: this.data.openId,
        word: this.data.word
      },
      header: {
        'content-type': 'application/json' 
      },
      method: 'POST',
      success: res => {
        if(res.data.code == 200){
          wx.setStorageSync('userInfo', res.data.data.userInfo)
          wx.setStorageSync('token', res.data.data.token)
          wx.switchTab({
            url: '/pages/main/main',
          })
        }else if(res.data.code == 501){
          wx.showToast({
            title: '账号或密码错误！',
            icon: 'error'
          })
        }else if(res.data.code == 888){
          wx.showModal({
            title: '您的账号已被封禁，请联系超级管理员：13891549166',
            cancelColor: 'cancelColor',
          })
        }else{
          wx.showToast({
            title: '服务出错！',
            icon: 'error'
          })
        }
      },
      fail: res=>{
        wx.showToast({
          title: '连接超时！',
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