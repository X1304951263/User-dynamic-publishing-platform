const app = getApp()
Page({
  data: {
    userInfo: ''
  },
  onShow(){
    this.setData({
      userInfo: wx.getStorageSync('userInfo')
    })
  },

  toVirify(){
    wx.navigateTo({
      url: '/pages/virify/virify'
    })
  },
  infoShow(event){
    wx.navigateTo({
      url: '/pages/personalInfo/personalInfo'
    })
  },

  getAdmins(){
    wx.navigateTo({
      url: '/pages/manager/manager',
    })
  },
  getBlackList(){
    wx.navigateTo({
      url: '/pages/blackList/blackList',
    })
  },
  //申请管理员
  applyManager(){
    wx.showModal({
      title: '申请成为管理员？',
      cancelColor: 'cancelColor',
      success: res=>{
        if(res.confirm){
          wx.request({ 
            url: app.globalData.URL + '/user/apply/manager',
            header: {
              'content-type': 'application/json',
              'token': wx.getStorageSync('token')
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
                  title: '申请成功！',
                  icon: 'success'
                })
              }else{
                wx.showToast({
                  title: '申请失败！',
                  icon: 'error'
                })
              }
            },
            fail: res=>{
              wx.showToast({
                title: '申请失败！',
                icon: 'error'
              })
            }
          })
        }
      }
    })
  },
  getPhone(){
    wx.showModal({
      title: '微信：13891549166',
      cancelColor: 'cancelColor',
    })
  },

  logout(){
    wx.showModal({
      title: '您将退出本程序',
      cancelColor: 'cancelColor',
      success: res=>{
        if(res.confirm){
          wx.clearStorage({
            success: (res) => {
              wx.navigateTo({
                url: '/pages/login/login',
              })
            },
          })
        }
      }
    })
  }
})
