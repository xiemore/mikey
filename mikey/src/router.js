import { createRouter, createWebHistory } from 'vue-router'
import {postRequest} from "@/utils/api";

//路由数组
const routes =
    [{
        path: '/chat',
        name: 'chat',
        meta: {isAuth: false},
        component: () => import('./views/Chat')
    },{
        path: '/',
        name: ' home',
        meta: { isAuth: false },
        component: () => import('./views/HomePage')
    },{
        path: '/login:before_url',
        name: ' login',
        meta: { isAuth: false },
        component: () => import('./views/LoginPage')
    },{
        path: '/good_detail:id',
        name: ' good_detail',
        meta: { isAuth: false },
        component: () => import('./views/GoodDetail'),
    },{
        path: '/user_detail:user_name',
        name: ' user_detail',
        meta: { isAuth: false },
        component: () => import('./views/UserDetail')
    },{
        path: '/home_page',
        name: ' home_page',
        meta: { isAuth: true},
        component: () => import('./views/UserHomePage'),
        children:[
            {
                path: '/home_page/change_pwd',
                name: ' change_pwd',
                meta: { isAuth: true},
                component: () => import('./components/ChangePwd')
            },
            {
                path: '/home_page/my_goods',
                name: ' my_goods',
                meta: { isAuth: true},
                component: () => import('./components/MyGoods')
            },
            {
                path: '/home_page/my_orders',
                name: ' my_orders',
                meta: { isAuth: true},
                component: () => import('./components/MyOrders')
            },
            {
                path: '/home_page/add_good',
                name: ' add_good',
                meta: { isAuth: true},
                component: () => import('./components/AddGood')
            },
            {
                path: '/home_page/my_wants',
                name: ' my_wants',
                meta: {isAuth: true},
                component: () => import('./components/MyWants')
            },
            {
                path: '/home_page/my_base_order',
                name: ' my_base_order',
                meta: {isAuth: true},
                component: () => import('./components/MyBaseOrder')
            },
            {
                path: '/home_page/hello',
                name: ' hello',
                meta: {isAuth: true},
                component: () => import('./components/Hello')
            }
        ]
    }
    ]
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes //上面的路由数组
})


//路由守卫
router.beforeEach((to, from, next) => {
    if ( to.meta.isAuth ){
        let code = 400;
        postRequest("/user/selectUrlsByUser", JSON.stringify({url_name: to.path})).then(resp => {
            if (resp.data["code"] === 200)
                code = 200;
            if ( code === 200 ){
                next();
            }
            else{
                alert("权限不足,请先登录");
                next({name: ' login', params: {before_url: to.path}});
            }
        });
    }
    else{
        next();
    }
})
export default router
