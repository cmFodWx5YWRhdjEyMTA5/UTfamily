package com.ufotech.ufo.utfamily.model;

import com.google.gson.Gson;

/**
 *  -----------------------------------------------------------------------------------------
 *  @author ：   UFO（陳俊融）Github：https://github.com/jyunrong
 *  @date ：   2018/5/24    下午 03:21
 *  @description ：   檢查APP版本的 POJO
 *  -----------------------------------------------------------------------------------------
 */

/* 巢狀 JSON 第一層 */
public class ApkVersion {

    /**
     * {"data":{"content":{"apkName":"UTfamily","serverVersion":2,"updateUrl":"http://192.168.31.188/UFO/UTfamily/element/file/UTfamily.apk","upgradeInfo":"有新功能哦！要不要嘗試一下?\n就不告訴你們我更新了什麼-。-\n\n----------萬能的分割線--------- --\n\n(ㄒoㄒ) 被老闆打了一頓，還是來告訴你吧：\n1.下架商品誤買了？恩。。。我搞了點小動作就不會出現了\n2 .側邊欄、彈框優化—— 這個你自己去探索吧，總得留點懸念嘛-。-\n","versionCode":2,"whetherForce":false}}}
     */

    private DataBean data;
    private static String msg;
    private static int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /* 巢狀 JSON 第二層 */
    public static class DataBean {
        private static int id;
        private static String api_key;
        private ContentBean content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getApi_key() {
            return api_key;
        }

        public void setApi_key(String api_key) {
            this.api_key = api_key;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        /* 巢狀 JSON 第三層 */
        public static class ContentBean {
            private String apkName;
            private int versionCode;
            private int serverVersion;
            private Boolean whetherForce;
            private String updateUrl;
            private String upgradeInfo;

            public String getApkName() {
                return apkName;
            }

            public void setApkName(String apkName) {
                this.apkName = apkName;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public int getServerVersion() {
                return serverVersion;
            }

            public void setServerVersion(int serverVersion) {
                this.serverVersion = serverVersion;
            }

            public Boolean getWhetherForce() {
                return whetherForce;
            }

            public void setWhetherForce(Boolean whetherForce) {
                this.whetherForce = whetherForce;
            }

            public String getUpdateUrl() {
                return updateUrl;
            }

            public void setUpdateUrl(String updateUrl) {
                this.updateUrl = updateUrl;
            }

            public String getUpgradeInfo() {
                return upgradeInfo;
            }

            public void setUpgradeInfo(String upgradeInfo) {
                this.upgradeInfo = upgradeInfo;
            }
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
