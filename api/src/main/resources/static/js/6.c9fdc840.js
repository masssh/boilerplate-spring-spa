(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[6],{"8e58":function(e,a,s){},"9c70":function(e,a,s){"use strict";var t=s("8e58"),r=s.n(t);r.a},f833:function(e,a,s){"use strict";s.r(a);var t=function(){var e=this,a=e.$createElement,s=e._self._c||a;return s("q-page",[s("div",{staticClass:"row justify-center"},[s("div",{staticClass:"col-6"},[s("q-card",[s("q-card-section",{staticClass:"center"},[s("div",{staticClass:"text-h5 text-center q-my-lg"},[e._v("Reset Password")]),s("ResetUserPassword")],1)],1)],1)])])},r=[],n=function(){var e=this,a=this,s=a.$createElement,t=a._self._c||s;return t("q-form",{ref:"form",staticClass:"q-gutter-md center"},[t("q-input",{attrs:{label:"* Email","lazy-rules":"",rules:[function(e){return!!e||"Email is required"},function(e){return/.+@.+\..+/.test(e)||"E-mail must be valid"}]},model:{value:a.email,callback:function(e){a.email=e},expression:"email"}}),t("q-input",{attrs:{label:"* Password",type:"password","lazy-rules":"",rules:[function(e){return!!e||"Password is required"}]},model:{value:a.password,callback:function(e){a.password=e},expression:"password"}}),t("q-input",{attrs:{label:"* Confirm Password",type:"password","lazy-rules":"",rules:[function(e){return!!e||"Password is required"},function(a){return e.password===a||"Password not matched"}]},model:{value:a.passwordConfirm,callback:function(e){a.passwordConfirm=e},expression:"passwordConfirm"}}),t("q-btn",{attrs:{label:"Reset Password","no-caps":""},on:{click:a.update}})],1)},i=[],o=s("967e"),c=s.n(o),l=(s("96cf"),s("fa84")),u=s.n(l),d={components:{},data:function(){return{email:null,password:null,passwordConfirm:null,verificationHash:null}},created:function(){var e=new URL(location.href);e.searchParams.has("q")&&(this.verificationHash=e.searchParams.get("q"))},methods:{update:function(){var e=this;return u()(c.a.mark((function a(){var s,t,r;return c.a.wrap((function(a){while(1)switch(a.prev=a.next){case 0:if(s=e.email,t=e.password,r=e.verificationHash,!e.$refs.form.validate()){a.next=11;break}return a.prev=2,a.next=5,e.$store.dispatch("resetPassword",{email:s,password:t,verificationHash:r});case 5:e.$router.push("/"),a.next=11;break;case 8:a.prev=8,a.t0=a["catch"](2),e.$q.dialog({title:"Alert",message:"Failed to reset email"});case 11:case"end":return a.stop()}}),a,null,[[2,8]])})))()}}},f=d,p=(s("9c70"),s("2877")),m=s("eebe"),w=s.n(m),h=s("0378"),v=s("27f9"),q=s("9c40"),b=Object(p["a"])(f,n,i,!1,null,"1c95abda",null),P=b.exports;w()(b,"components",{QForm:h["a"],QInput:v["a"],QBtn:q["a"]});var C={components:{ResetUserPassword:P},data:function(){return{verificationHash:null}},created:function(){var e=new URL(location.href);e.searchParams.has("q")&&(this.verificationHash=e.searchParams.get("q"))}},x=C,k=s("9989"),g=s("f09f"),y=s("a370"),H=Object(p["a"])(x,t,r,!1,null,null,null);a["default"]=H.exports;w()(H,"components",{QPage:k["a"],QCard:g["a"],QCardSection:y["a"]})}}]);