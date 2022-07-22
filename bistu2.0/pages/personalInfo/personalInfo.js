const app = getApp();
Page({
  data: {
    openid: '',
    avatar: '',
    nickName: '',
    newName: '',
    gender: '',
    city: '',
    signature: '',
    newSignature: '',
    auth: '',
    hiddenmodalput: true,
    hiddenmodalput1: true,
  },
  setAvatar(){
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      camera: 'back',
      success: res=> {
        wx.showLoading({
          title: '上传中...',
        })
        var info = wx.getStorageSync('userInfo')
        var token = wx.getStorageSync('token')
        wx.uploadFile({
          url: app.globalData.URL + '/user/set/avatar',
          method: 'POST',
          filePath: res.tempFiles[0].tempFilePath,
          header: {
            'content-type': 'multipart/form-data;charset=UTF-8',
            'token': token
          },
          name: 'file',
          success: res1=>{
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
            res1.data = JSON.parse(res1.data)
            if(res1.data.code == 200){
              this.setData({
                avatar: res1.data.data.avatar
              })
              info.avatar = res1.data.data.avatar
              wx.setStorageSync('userInfo', info)
            }else{
              wx.showToast({
                title: '修改失败！',
                icon: 'error'
              })
            }
          },
          fail: function () {
            wx.showToast({
              title: '修改失败！',
              icon: 'error'
            })
          }
        })
      }
    })
  },

  chooseSex() {
    var token
    var info
    wx.showActionSheet({
      itemList: ['男','女'],
      success: res=>{
        if(res.tapIndex != this.data.gender ){
          var index = res.tapIndex
          token = wx.getStorageSync('token')
          wx.request({
            url: app.globalData.URL + '/user/set/gender',
            data: {
              gender: res.tapIndex,
            },
            header: {
              'content-type': 'application/json',
              'token': token
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
                  title: '修改成功！',
                  icon: 'success'
                })
                this.setData({
                  gender: index
                }),
                info = wx.getStorageSync('userInfo')
                info.gender = index
                wx.setStorageSync('userInfo', info)
              }else{
                wx.showToast({
                  title: '修改失败！',
                  icon: 'error'
                })
              }
            },
            fail:res=>{
              wx.showToast({
                title: '修改失败！',
                icon: 'error'
              })
            }
          })
        }
      },
    })
  },

  setCity() {
    var token
    var info
    wx.showActionSheet({
      itemList: ['小营','清河','沙河','健翔桥'],
      success: res=>{
        if(res.tapIndex != this.data.city ){
          var index = res.tapIndex
          token = wx.getStorageSync('token')
          wx.request({
            url: app.globalData.URL + '/user/set/campus',
            data: {
              campus: res.tapIndex,
            },
            header: {
              'content-type': 'application/json',
              'token': token
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
                  title: '修改成功！',
                  icon: 'success'
                })
                this.setData({
                  city: index
              }),
                info = wx.getStorageSync('userInfo')
                info.campus = index
                wx.setStorageSync('userInfo', info)
              }else{
                wx.showToast({
                  title: '修改失败！',
                  icon: 'error'
                })
              }
            },
            fail:res=>{
              wx.showToast({
                title: '修改失败！',
                icon: 'error'
              })
            }
          })
        }
      },
    })
  },

  setNickName(){
    this.setData({
        hiddenmodalput: false,
        newName: this.data.nickName,
    })
  },
  setSignature(){
    this.setData({
        hiddenmodalput1: false,
        newSignature: this.data.signature
    })
  },

  getInputValue(e){
    this.setData({
        newName: e.detail.value
    })
  },
  getInputValue1(e){
    this.setData({
        newSignature: e.detail.value
    })
  },

  confirm(){
    this.setData({
      hiddenmodalput: true,
    })
    wx.showLoading({
      title: '修改中',
    })
    var token = wx.getStorageSync('token')
    wx.request({
      url: app.globalData.URL + '/user/set/nickName',
      data: {
        nickName: this.data.newName,
      },
      header: {
        'content-type': 'application/json',
        'token': token
      },
      method: 'POST',
      success: res => {
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
          wx.showToast({
            title: '修改成功！',
            icon: 'success'
          })
          this.setData({
            nickName: this.data.newName,
          })
          var info = wx.getStorageSync('userInfo')
          info.nickName = this.data.newName
          wx.setStorageSync('userInfo', info)
        }else{
          wx.showToast({
            title: '修改失败！',
            icon: 'error'
          })
        }
      },
      fail:res=>{
        wx.hideLoading({
          success: (res) => {},
        })
        wx.showToast({
          title: '修改失败！',
          icon: 'error'
        })
      }
    })
  },
  confirm1(){
    this.setData({
        hiddenmodalput1: true,
    })
    wx.showLoading({
      title: '修改中',
    })
    var token = wx.getStorageSync('token')
    wx.request({
      url: app.globalData.URL + '/user/set/signature',
      data: {
        signature: this.data.newSignature,
      },
      header: {
        'content-type': 'application/json',
        'token': token
      },
      method: 'POST',
      success: res => {
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
          wx.showToast({
            title: '修改成功！',
            icon: 'success'
          })
          this.setData({
            signature: this.data.newSignature,
          })
          var info = wx.getStorageSync('userInfo')
          info.signature = this.data.signature
          wx.setStorageSync('userInfo', info)
        }else{
          wx.showToast({
            title: '修改失败！',
            icon: 'error'
          })
        }
      },
      fail:res=>{
        wx.hideLoading({
          success: (res) => {},
        })
        wx.showToast({
          title: '修改失败！',
          icon: 'error'
        })
      }
    })
  },

  cancel(){
    this.setData({
        hiddenmodalput: true,
    })
  },
  cancel1(){
    this.setData({
        hiddenmodalput1: true,
    })
  },


  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    var userInfo = wx.getStorageSync('userInfo')
    this.setData({
      avatar: userInfo.avatar,
      nickName: userInfo.nickName,
      newName:userInfo.nickName,
      gender: userInfo.gender,
      city: userInfo.campus,
      signature: userInfo.signature,
      newSignature: userInfo.signature,
      auth: userInfo.auth,
    })
  },

})