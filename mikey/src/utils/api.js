import axios from 'axios'
axios.defaults.withCredentials=true;

export const postKeyValueRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `http://localhost:8081${url}`,
        data: params,
        transformRequest: [function (data) {
            let ret = '';
            for (let i in data) {
                ret += encodeURIComponent(i) + '=' + encodeURIComponent(data[i]) + '&'
            }
            return ret;
        }],
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
}
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `http://localhost:8081${url}`,
        data: params,
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'encoding': 'UTF-8',
            'token': sessionStorage.getItem("token")
        }
    })
}
export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `http://localhost:8081${url}`,
        data: params,
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'encoding': 'UTF-8',
            'token': sessionStorage.getItem("token")
        }
    })
}
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `http://localhost:8081${url}`,
        data: params,
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'encoding': 'UTF-8',
            'token': sessionStorage.getItem("token")
        }
    })
}
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `http://localhost:8081${url}`,
        data: params,
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'encoding': 'UTF-8',
            'token': sessionStorage.getItem("token")
        }
    })
}
export const deleteSelectionRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `http://localhost:8081${url}`,
        data: params,
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'encoding': 'UTF-8',
            'Authorization': sessionStorage.getItem("token")
        }
    })
}

