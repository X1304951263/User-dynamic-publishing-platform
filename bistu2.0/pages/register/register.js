const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    sendCode: '获取验证码',
    flag: false,
    snsMsgWait: 60,
    code: '',
    openId: '',
    word: '',
    email: ''
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
  getEmail(e){
    this.setData({
        email: e.detail.value
    })
  },
  getCode(e){
    this.setData({
        code: e.detail.value
    })
  },

  // 发送验证码
  sendCode: function() {  
    // 60秒后重新获取验证码
    if(!this.data.openId || !this.data.word || !this.data.email){
      wx.showToast({
        title: '参数不能为空！',
        icon: 'error'
      })
      return;
    }
    if(this.data.openId.length < 6 || this.data.openId.length > 14){
      wx.showToast({
        title: '账号出错！',
        icon: 'error'
      })
      return;
    }
    if(this.data.word.length < 6 || this.data.word.length > 14){
      wx.showToast({
        title: '密码出错！',
        icon: 'error'
      })
      return;
    }
    /* 需要使用/   /转义符 来转义正则表达式，否则出错 */
    if(!(/^[a-z0-9A-Z]+$/.test(this.data.openId))){
      wx.showToast({
        title: '账号出错！',
        icon: 'error'
      })
      return;
    }
    if(!(/^[a-z0-9A-Z]+$/.test(this.data.word))){
      wx.showToast({
        title: '密码出错！',
        icon: 'error'
      })
      return;
    }
    if(!(/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(this.data.email))){
      wx.showToast({
        title: '邮箱出错！',
        icon: 'error'
      })
      return;
    }
    var inter = setInterval(function() {
      this.setData({
        flag: true,
        sendCode: this.data.snsMsgWait + 's后重发',
        snsMsgWait: this.data.snsMsgWait - 1
      });
      if (this.data.snsMsgWait < 0) {
        clearInterval(inter)
        this.setData({
          sendCode: '获取验证码',
          snsMsgWait: 60,
          flag: false
       });
      }
    }.bind(this), 1000);
    /* 参数合格，注册 */
    wx.showLoading({
      title: '正在发送',
    })
    wx.request({
      url: app.globalData.URL + '/login/virifyCode',
      data: {
        email: this.data.email,
      },
      header: {
        'content-type': 'application/json' 
      },
      method: 'POST',
      success: res => {
        if(res.data.code == 200){
          wx.showToast({
            title: '发送成功！',
            icon: 'success'
          })
        }else{
          wx.showToast({
            title: '发送失败！',
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

  // 注册账号
  register(){
    if(!this.data.openId || !this.data.word || !this.data.email){
      wx.showToast({
        title: '参数不能为空！',
        icon: 'error'
      })
      return;
    }
    if(this.data.openId.length < 6 || this.data.openId.length > 14){
      wx.showToast({
        title: '账号出错！',
        icon: 'error'
      })
      return;
    }
    if(this.data.word.length < 6 || this.data.word.length > 14){
      wx.showToast({
        title: '密码出错！',
        icon: 'error'
      })
      return;
    }

    /* 需要使用/   /转义符 来转义正则表达式，否则出错 */
    if(!(/^[a-z0-9A-Z]+$/.test(this.data.openId))){
      wx.showToast({
        title: '账号出错！',
        icon: 'error'
      })
      return;
    }
    if(!(/^[a-z0-9A-Z]+$/.test(this.data.word))){
      wx.showToast({
        title: '密码出错！',
        icon: 'error'
      })
      return;
    }
    if(!(/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(this.data.email))){
      wx.showToast({
        title: '邮箱出错！',
        icon: 'error'
      })
      return;
    }
    if(!(/[0-9]+/.test(this.data.code))){
      wx.showToast({
        title: '验证码错误！',
        icon: 'error'
      })
      return;
    }
    /* 参数合格，注册 */
    wx.showLoading({
      title: '注册中',
    })
    wx.request({
      url: app.globalData.URL + '/login/register',
      data: {
        openId: this.data.openId,
        email: this.data.email,
        word: this.data.word,
        code: this.data.code,
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
        }else if(res.data.code == 502){
          wx.showToast({
            title: '该邮箱已注册！',
            icon: 'error'
          })
        }else if(res.data.code == 501){
          wx.showToast({
            title: '验证码错误！',
            icon: 'error'
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