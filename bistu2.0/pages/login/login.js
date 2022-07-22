const app = getApp();
Page({
  data: {
    userInfo: '',
    idx: 3
  },
  onLoad: function (options) {
    var idx = Math.round(Math.random()*6 + 1)
    this.setData({
      idx: idx
    })
  },
  login(){
    wx.navigateTo({
      url: '/pages/account/account',
    })
  },
  login1(){
    wx.getSetting({
      success:res=>{
        //console.log(res.authSetting["scope.userInfo"])  //该用户是否已经授权登录注册过此小程序
        if(res.authSetting["scope.userInfo"]){   // 已授权
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
                  wx.showToast({
                    title: '连接超时！',
                    icon: 'error'
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
              //console.log(res)
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
                      if(res.data.code == 200){
                        wx.redirectTo({
                          url: '/pages/personalInfo/personalInfo'
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
          })
        }

      }
    })
  },
  start(){
    wx.switchTab({
      url: '/pages/user/user',
    })
  },
})