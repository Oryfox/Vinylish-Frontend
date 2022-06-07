import router from "../router/index";
import Cookies from "js-cookie";

let baseurl = import.meta.env.VITE_HOST;

async function get(uri) {
    if (Cookies.get("token") === undefined) {
        router.push("/login")
    } else
        return fetch(baseurl + uri, {
            headers: {
                "token": Cookies.get("token")
            }
        }).catch(error => console.log(error))
}

async function post(uri, body) {
    if (Cookies.get("token") === undefined) {
        router.push("/login")
    } else
        return fetch(baseurl + uri, {
            headers: {
                "token": Cookies.get("token"),
                "Content-Type": "application/json"
            },
            method: "POST",
            body: body
        })
}

async function put(uri, body) {
    if (Cookies.get("token") === undefined) {
        router.push("/login")
    } else
        return fetch(baseurl + uri, {
            headers: {
                "token": Cookies.get("token"),
                "Content-Type": "application/json"
            },
            method: "PUT",
            body: body
        })
}


async function hDelete(uri) {
    if (Cookies.get("token") === undefined) {
        router.push("/login")
    } else
        return fetch(baseurl + uri, {
            headers: {
                "token": Cookies.get("token")
            },
            method: "DELETE"
        })
}

export default {
    getRecords() {
        return get("record")
    },
    getRecord(id) {
        return get("record?id=" + id)
    },
    postRecord(record) {
        return post("record", JSON.stringify(record))
    },
    putRecord(record) {
        return put("record", JSON.stringify(record))
    },
    deleteRecord(id) {
        return hDelete("record?id=" + id)
    },
    login(login) {
        return fetch(baseurl + "user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(login)
        })
    },
    register(register) {
        return fetch(baseurl + "user", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(register)
        })
    },
    logout() {
        return put("user/logout", null)
    },
    getAutoRecord(artist, title) {
        return get("record/auto?artist=" + encodeURI(artist) + "&title=" + encodeURI(title))
    }
}