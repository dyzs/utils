package com.dyzs.app.entity;

import java.util.List;

/**
 * @author maidou, created on 2018/2/28.
 */

public class RetrofitSampleBean {

    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultBean> getResults() {
        return results;
    }

    public void setResults(List<ResultBean> results) {
        this.results = results;
    }

    private List<ResultBean> results;


    public static class ResultBean{
        /**
         * copy from website
         * _id : 5827f41b421aa911d3bb7ecb
         * createdAt : 2016-11-13T13:03:23.38Z
         * desc : 独立全端开发的开源小作：简诗 2.0
         * images : ["http://img.gank.io/b6be7a85-4035-437f-9521-65593fdbc48e"]
         * publishedAt : 2016-11-14T11:36:49.680Z
         * source : web
         * type : Android
         * url : https://www.v2ex.com/t/320154
         * used : true
         * who : wingjay
         */
        private String id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
