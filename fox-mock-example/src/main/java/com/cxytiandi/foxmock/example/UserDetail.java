package com.cxytiandi.foxmock.example;

import java.io.Serializable;
import java.util.Map;

/**
 * @作者 尹吉欢
 * @个人微信 jihuan900
 * @微信公众号 猿天地
 * @GitHub https://github.com/yinjihuan
 * @作者介绍 http://cxytiandi.com/about
 * @时间 2022-04-28 21:54
 */
public class UserDetail implements Serializable {

    private String name;

    private Map<String, UserAddress> addressMap;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddressMap(Map<String, UserAddress> addressMap) {
        this.addressMap = addressMap;
    }

    public Map<String, UserAddress> getAddressMap() {
        return addressMap;
    }

    public static class UserAddress implements Serializable {
        private String address;

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }
}
