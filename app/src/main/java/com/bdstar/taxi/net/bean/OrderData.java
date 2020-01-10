package com.bdstar.taxi.net.bean;

/**
 * {
 *         "code": 100,
 *         "message": "operation successful",
 *         "object": {
 *             "isok": 1,
 *             "id": "187a4d6d8b144fee9a6a59ead242aa8f20191222165645" --------------->该id为此行程单唯一id
 *         }
 */
public class OrderData {
    private Order object;

    public Order getObject() {
        return object;
    }

    public void setObject(Order object) {
        this.object = object;
    }

    public static class Order{
        private int isok;
        private String id;

        public int getIsok() {
            return isok;
        }

        public void setIsok(int isok) {
            this.isok = isok;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
