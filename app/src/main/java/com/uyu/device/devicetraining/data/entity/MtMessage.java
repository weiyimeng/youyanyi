package com.uyu.device.devicetraining.data.entity;

/**
 * Created by windern on 2015/12/2.
 */
public class MtMessage {

    /**
     * 消息类型
     */
    public MT mt;
    /**
     * 消息体
     */
    public Message msg = new Message();


    public class Message{
        /**
         * 标题
         */
        public String title;

        /**
         * 链接
         */
        public String link;

        /**
         * 摘要
         */
        public String brief;

        /**
         * 显示图标
         */
        public String img;

        @Override
        public String toString() {
            return "Message{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", brief='" + brief + '\'' +
                    ", img='" + img + '\'' +
                    '}';
        }
    }

    /**
     * 消息类型
     */
    public enum MT {
        MT_LOG_OUT("踢出"), MT_CONTENT("连接");

        private String name;

        private MT(String name) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return "MqttMsg{" +
                "mt=" + mt +
                ", msg=" + msg +
                '}';
    }
}
