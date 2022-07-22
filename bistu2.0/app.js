// app.js
App({
  onLaunch() {
    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
  },
  globalData: {
    //URL: 'http://43.138.41.252:8081'
    URL: 'http://127.0.0.1:8080'
  }
})
