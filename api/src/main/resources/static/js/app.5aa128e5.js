(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([[1],{0:function(e,t,n){e.exports=n("2f39")},"0047":function(e,t,n){},"2f39":function(e,t,n){"use strict";n.r(t);var r=n("967e"),a=n.n(r),o=(n("a481"),n("96cf"),n("fa84")),s=n.n(o),i=(n("7d6e"),n("e54f"),n("985d"),n("0047"),n("2b0e")),u=n("1f91"),c=n("42d2"),p=n("b05d"),l=n("436b");i["a"].use(p["a"],{config:{},lang:u["a"],iconSet:c["a"],plugins:{Dialog:l["a"]}});var f=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"q-app"}},[n("router-view")],1)},m=[],d={name:"App"},h=d,w=n("2877"),b=Object(w["a"])(h,f,m,!1,null,null,null),x=b.exports,v=n("2f62");i["a"].use(v["a"]);var k=function(){var e=new v["a"].Store({state:{initialized:!1,title:null,user:{login:!1,userHash:null,accessToken:null,role:null,userName:null,email:null}},mutations:{initialized:function(e){e.initialized=!0},setTitle:function(e,t){var n=t.title;e.title=n},login:function(e,t){var n=t.userHash,r=t.accessToken,a=t.role;e.user.login=!0,e.user.userHash=n,e.user.accessToken=r,e.user.role=a},logout:function(e){e.user.login=!1,e.user.userHash=null,e.user.accessToken=null,e.user.role=null},setUser:function(e,t){var n=t.userName,r=t.email;e.user.userName=n,e.user.email=r}},actions:{getToken:function(e){var t=e.commit;return s()(a.a.mark((function e(){var n,r,o,s,u;return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.get("/api/token");case 2:n=e.sent,r=n.data,o=r.userHash,s=r.accessToken,u=r.role,t("login",{userHash:o,accessToken:s,role:u});case 5:case"end":return e.stop()}}),e)})))()},login:function(e,t){var n=t.email,r=t.password;return s()(a.a.mark((function e(){var t;return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return t=new FormData,t.append("email",n),t.append("password",r),e.next=5,i["a"].prototype.$axios.post("/api/login",t);case 5:case"end":return e.stop()}}),e)})))()},signUp:function(e,t){var n=t.userName,r=t.email,o=t.password;return s()(a.a.mark((function e(){return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.post("/api/signUp",{userName:n,email:r,password:o});case 2:case"end":return e.stop()}}),e)})))()},logout:function(e){var t=e.commit;return s()(a.a.mark((function e(){return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.post("/api/logout");case 2:t("logout");case 3:case"end":return e.stop()}}),e)})))()},getUser:function(e){var t=e.commit;return s()(a.a.mark((function e(){var n,r,o,s;return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.get("/api/user");case 2:n=e.sent,r=n.data,o=r.userName,s=r.email,t("setUser",{userName:o,email:s});case 5:case"end":return e.stop()}}),e)})))()},updateUser:function(e,t){var n=t.userName,r=t.password;return s()(a.a.mark((function e(){return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.put("/api/user",{userName:n,password:r});case 2:case"end":return e.stop()}}),e)})))()},forgotPassword:function(e,t){var n=t.email;return s()(a.a.mark((function e(){return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.post("/api/password/forgot",{email:n});case 2:case"end":return e.stop()}}),e)})))()},resetPassword:function(e,t){var n=e.commit,r=t.email,o=t.password,u=t.verificationHash;return s()(a.a.mark((function e(){return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.post("/api/password/reset",{email:r,password:o,verificationHash:u});case 2:n("logout");case 3:case"end":return e.stop()}}),e)})))()},deleteUser:function(e){var t=e.commit;return s()(a.a.mark((function e(){return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,i["a"].prototype.$axios.delete("/api/user");case 2:t("logout");case 3:case"end":return e.stop()}}),e)})))()}},modules:{},strict:!1});return e},g=(n("7f7f"),n("8c4f")),y=[{path:"/",name:"",component:function(){return Promise.all([n.e(0),n.e(4)]).then(n.bind(null,"713b"))},meta:{public:!0},children:[{path:"",name:"Home",component:function(){return n.e(13).then(n.bind(null,"bc13b"))},meta:{public:!0}},{path:"login",name:"Sign In",component:function(){return Promise.all([n.e(0),n.e(3)]).then(n.bind(null,"4fc8"))},meta:{public:!0}},{path:"signUp",name:"Sign Up",component:function(){return Promise.all([n.e(0),n.e(7)]).then(n.bind(null,"89a8"))},meta:{public:!0}},{path:"password/reset",name:"Reset Password",component:function(){return Promise.all([n.e(0),n.e(6)]).then(n.bind(null,"f833"))},meta:{public:!0}},{path:"password/forgot",name:"Forgot Password",component:function(){return Promise.all([n.e(0),n.e(5)]).then(n.bind(null,"1613"))},meta:{public:!0}},{path:"dashboard",name:"Dashboard",component:function(){return n.e(12).then(n.bind(null,"ec95"))}},{path:"user",name:"User Profile",component:function(){return Promise.all([n.e(0),n.e(9)]).then(n.bind(null,"4336"))}},{path:"user/profile/edit",name:"User Profile Edit",component:function(){return Promise.all([n.e(0),n.e(10)]).then(n.bind(null,"80eb"))}},{path:"user/password/edit",name:"User Password Edit",component:function(){return Promise.all([n.e(0),n.e(8)]).then(n.bind(null,"bfd9"))}}]}];y.push({path:"*",component:function(){return Promise.all([n.e(0),n.e(11)]).then(n.bind(null,"e51e"))}});var P=y;i["a"].use(g["a"]);var U=function(e){var t=e.store,n=new g["a"]({scrollBehavior:function(){return{x:0,y:0}},routes:P,mode:"history",base:"/web/"});return n.beforeEach((function(e,n,r){function o(){return i.apply(this,arguments)}function i(){return i=s()(a.a.mark((function n(){var o;return a.a.wrap((function(n){while(1)switch(n.prev=n.next){case 0:if(o=t.state.user.login,"/"!==e.path||!o){n.next=4;break}return r({path:"/dashboard"}),n.abrupt("return");case 4:if(!e.meta.public&&!o){n.next=8;break}return t.commit("setTitle",{title:e.name}),r(),n.abrupt("return");case 8:t.commit("setTitle",{title:"Home"}),r("/");case 10:case"end":return n.stop()}}),n)}))),i.apply(this,arguments)}function u(e,t){return c.apply(this,arguments)}function c(){return c=s()(a.a.mark((function t(n,o){return a.a.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,n.commit("initialized"),t.next=4,n.dispatch("getToken");case 4:return t.next=6,n.dispatch("getUser");case 6:"/"===e.path?r("/dashboard"):o(),t.next=12;break;case 9:t.prev=9,t.t0=t["catch"](0),o();case 12:case"end":return t.stop()}}),t,null,[[0,9]])}))),c.apply(this,arguments)}var p=t.state.initialized;p?o():u(t,o)})),n},$=function(){return T.apply(this,arguments)};function T(){return T=s()(a.a.mark((function e(){var t,n,r;return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if("function"!==typeof k){e.next=6;break}return e.next=3,k({Vue:i["a"]});case 3:e.t0=e.sent,e.next=7;break;case 6:e.t0=k;case 7:if(t=e.t0,"function"!==typeof U){e.next=14;break}return e.next=11,U({Vue:i["a"],store:t});case 11:e.t1=e.sent,e.next=15;break;case 14:e.t1=U;case 15:return n=e.t1,t.$router=n,r={el:"#q-app",router:n,store:t,render:function(e){return e(x)}},e.abrupt("return",{app:r,store:t,router:n});case 19:case"end":return e.stop()}}),e)}))),T.apply(this,arguments)}var H=n("a925"),N={failed:"Action failed",success:"Action was successful"},z={"en-us":N};i["a"].use(H["a"]);var E=new H["a"]({locale:"en-us",fallbackLocale:"en-us",messages:z}),S=function(e){var t=e.app;t.i18n=E},A=n("bc3a"),D=n.n(A),V=D.a.create({withCredentials:!0});function q(){return C.apply(this,arguments)}function C(){return C=s()(a.a.mark((function e(){var t,n,r,o,s,u,c,p,l;return a.a.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,$();case 2:t=e.sent,n=t.app,r=t.store,o=t.router,s=!0,u=function(e){s=!1,window.location.href=e},c=window.location.href.replace(window.location.origin,""),p=[S,void 0],l=0;case 11:if(!(!0===s&&l<p.length)){e.next=29;break}if("function"===typeof p[l]){e.next=14;break}return e.abrupt("continue",26);case 14:return e.prev=14,e.next=17,p[l]({app:n,router:o,store:r,Vue:i["a"],ssrContext:null,redirect:u,urlPath:c});case 17:e.next=26;break;case 19:if(e.prev=19,e.t0=e["catch"](14),!e.t0||!e.t0.url){e.next=24;break}return window.location.href=e.t0.url,e.abrupt("return");case 24:return console.error("[Quasar] boot error:",e.t0),e.abrupt("return");case 26:l++,e.next=11;break;case 29:if(!1!==s){e.next=31;break}return e.abrupt("return");case 31:new i["a"](n);case 32:case"end":return e.stop()}}),e,null,[[14,19]])}))),C.apply(this,arguments)}i["a"].prototype.$axios=V,q()}},[[0,2,0]]]);