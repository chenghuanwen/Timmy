package com.dgkj.tianmi.bean;

import java.util.List;

/** gson 格式化 主题实体类
 * Created by Android004 on 2017/3/15.
 */

public class Them2 {

    /**
     * dataset : [{"id":6,"plate":{"announcement":"[\"公告1\",\"公告2\",\"公告3\",\"公告4\",\"公告5\"]",
     * "iconUrl":"http://bbs.vivo.com.cn/template/vivo2015/images/forumicon/121.gif","id":2,"lastPostingTime":1482983374518,"plateName":"第二个版块","plateType":{"id":1,"plateTypeName":"第一个版块类型"}},"themeName":"第六个主题","time":1482983312518},{"id":7,"plate":{"announcement":"[\"公告1\",\"公告2\",\"公告3\",\"公告4\",\"公告5\"]","iconUrl":"http://bbs.vivo.com.cn/template/vivo2015/images/forumicon/121.gif","id":2,"lastPostingTime":1482983374518,"plateName":"第二个版块","plateType":{"id":1,"plateTypeName":"第一个版块类型"}},"themeName":"第七个主题","time":1482983312518},{"id":8,"plate":{"announcement":"[\"公告1\",\"公告2\",\"公告3\",\"公告4\",\"公告5\"]",
     * "iconUrl":"http://bbs.vivo.com.cn/template/vivo2015/images/forumicon/121.gif","id":2,"lastPostingTime":1482983374518,"plateName":"第二个版块","plateType":{"id":1,"plateTypeName":"第一个版块类型"}},"themeName":"第八个主题","time":1482983312518},{"id":9,"plate":{"announcement":"[\"公告1\",\"公告2\",\"公告3\",\"公告4\",\"公告5\"]","iconUrl":"http://bbs.vivo.com.cn/template/vivo2015/images/forumicon/121.gif","id":2,"lastPostingTime":1482983374518,"plateName":"第二个版块","plateType":{"id":1,"plateTypeName":"第一个版块类型"}},"themeName":"第九个主题","time":1482983312518}]
     * status : 1000
     */

    private String status;
    private List<DatasetBean> dataset;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DatasetBean> getDataset() {
        return dataset;
    }

    public void setDataset(List<DatasetBean> dataset) {
        this.dataset = dataset;
    }

    public static class DatasetBean {
        /**
         * id : 6
         * plate : {"announcement":"[\"公告1\",\"公告2\",\"公告3\",\"公告4\",\"公告5\"]",
         * "iconUrl":"http://bbs.vivo.com.cn/template/vivo2015/images/forumicon/121.gif","id":2,"lastPostingTime":1482983374518,"plateName":"第二个版块","plateType":{"id":1,"plateTypeName":"第一个版块类型"}}
         * themeName : 第六个主题
         * time : 1482983312518
         */

        private int id;
        private PlateBean plate;
        private String themeName;
        private long time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public PlateBean getPlate() {
            return plate;
        }

        public void setPlate(PlateBean plate) {
            this.plate = plate;
        }

        public String getThemeName() {
            return themeName;
        }

        public void setThemeName(String themeName) {
            this.themeName = themeName;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public static class PlateBean {
            /**
             * announcement : ["公告1","公告2","公告3","公告4","公告5"]
             * iconUrl : http://bbs.vivo.com.cn/template/vivo2015/images/forumicon/121.gif
             * id : 2
             * lastPostingTime : 1482983374518
             * plateName : 第二个版块
             * plateType : {"id":1,"plateTypeName":"第一个版块类型"}
             */

            private String announcement;
            private String iconUrl;
            private int id;
            private long lastPostingTime;
            private String plateName;
            private PlateTypeBean plateType;

            public String getAnnouncement() {
                return announcement;
            }

            public void setAnnouncement(String announcement) {
                this.announcement = announcement;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getLastPostingTime() {
                return lastPostingTime;
            }

            public void setLastPostingTime(long lastPostingTime) {
                this.lastPostingTime = lastPostingTime;
            }

            public String getPlateName() {
                return plateName;
            }

            public void setPlateName(String plateName) {
                this.plateName = plateName;
            }

            public PlateTypeBean getPlateType() {
                return plateType;
            }

            public void setPlateType(PlateTypeBean plateType) {
                this.plateType = plateType;
            }

            public static class PlateTypeBean {
                /**
                 * id : 1
                 * plateTypeName : 第一个版块类型
                 */

                private int id;
                private String plateTypeName;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getPlateTypeName() {
                    return plateTypeName;
                }

                public void setPlateTypeName(String plateTypeName) {
                    this.plateTypeName = plateTypeName;
                }
            }
        }
    }
}
